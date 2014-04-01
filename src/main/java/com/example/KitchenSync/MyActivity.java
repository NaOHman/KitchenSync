package com.example.KitchenSync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

public class MyActivity extends Activity {


    private MealListAdapter listAdapter;
    private ExpandableListView expListView;
    private Week week = null;
    private TextView dateDisplay;
    private String dayString;
    private Weekday weekday;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        createMenu();


        expListView = (ExpandableListView) findViewById(R.id.menu_expandable);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new WeekDataFetcher().execute(getString(R.string.cafe_url));
        } else {
            //network not connected do something
        }

    }



    //creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
        return true;
    }

    //on click listeners for menu items
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        item.getOrder();
        switch (item.getItemId()) {


            //day selection
            case R.id.mealOptionsMenu_Sunday:
                setDayValues(0);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Monday:
                setDayValues(1);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Tuesday:
                setDayValues(2);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Wednesday:
                setDayValues(3);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Thursday:
                setDayValues(4);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Friday:
                setDayValues(5);
                if(week != null) updateListData();
                return true;
            case R.id.mealOptionsMenu_Saturday:
                setDayValues(6);
                if(week != null) updateListData();
                return true;


            //filter selection
            case R.id.mealOptionsMenu_Vegan:
                //filter.setRestriction(Restriction.values()[position]); TODO merge changes to be able to use filter
                return true;
            case R.id.mealOptionsMenu_Pescetarian:
                //TODO
                return true;
            case R.id.mealOptionsMenu_Vegetarian:
                //TODO
                return true;
            case R.id.mealOptionsMenu_None:
                //TODO
                return true;

            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    private void setDayValues(int day){
        Log.d("MyActivity", "Setting Day value = " + day);
        weekday = Weekday.values()[day-1];
        switch (day){
            case 1:
                dayString = "Sunday";
                break;
            case 2:
                dayString = "Monday";
                break;
            case 3:
                dayString = "Tuesday";
                break;
            case 4:
                dayString = "Wednesday";
                break;
            case 5:
                dayString = "Thursday";
                break;
            case 6:
                dayString = "Friday";
                break;
            case 7:
                dayString = "Saturday";
                break;
            default: dayString = "INVALID DAY";
        }
    }

    public void setListData(Week week){
        this.week = week;
        updateListData();
    }

    public void updateListData(){
        Day day = week.getDay(weekday);
        listAdapter = new MealListAdapter(day, this);
        expListView.setAdapter(listAdapter);
    }

    /**
     * sets up menu bar in main Layout
     */
    public void createMenu(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        setDayValues(day);
        dateDisplay = (TextView) findViewById(R.id.header_date);
        dateDisplay.setText(dayString + ", " + calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DATE));

        //assigns onClickListener to preferencesMenuButton
        final ImageButton preferencesButton = (ImageButton) findViewById(R.id.preferencesImageButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.this.openOptionsMenu();
            }
        });

    }

    private class WeekDataFetcher extends AsyncTask<String, Void, Week> {
        private ProgressDialog dialog = new ProgressDialog(MyActivity.this);
        private Exception exception;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Welcome to KitchenSync! Collecting Menu Information..");
            this.dialog.show();
        }

        @Override
        protected Week doInBackground(final String... args) {
            try {
                Log.d("WeekDataFetcher", "Establishing Connection");
                Document doc = Jsoup.connect(args[0]).get();
                if (doc != null) {
                    Log.d("WeekDataFetcher", "Got Doc");
                } else {
                    Log.d("WeekDataFetcher", "No Doc");
                }
                Week week = new Week(doc);
                return week;
            } catch (Exception e) {
                this.exception = e;
                Log.e("WeekDataFetcher", "Error collecting Data");
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Week week) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            // Setting data to list adapter
            if (week !=null){
                setListData(week);
            }
        }
    }
}
