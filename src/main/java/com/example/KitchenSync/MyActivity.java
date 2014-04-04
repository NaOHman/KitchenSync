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
    private TextView dateDisplay,dateDisplayMeals;
    private String dayString;
    private Weekday weekday;
    private String[] dayArray;

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
            //TODO network not connected do something
        }
    }


    /**
     * sets up menu bar in main Layout
     */
    private void createMenu(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        dayArray = getResources().getStringArray(R.array.day_array);
        dateDisplayMeals = (TextView) findViewById(R.id.currentMealDateDisplay);
        dateDisplay = (TextView) findViewById(R.id.header_date);
        dateDisplay.setText(dayArray[day] + ", " + calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DATE));

        //sets current day using Android.calendar
        weekday = Weekday.values()[day];
        setDayValues(dayArray[day]);

        //assigns onClickListener to preferencesMenuButton
        final ImageButton preferencesButton = (ImageButton) findViewById(R.id.preferencesImageButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.this.openOptionsMenu();
            }
        });

    }

    //creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
        return true;
    }



    /**
     * day or filter option chosen in menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if (item.getGroupId() == R.id.menuMealGroup) {
            dayString = item.getTitle().toString();
            setDayValues(dayString);
            if(week != null) updateListData();
            return true;
        }
        if (item.getGroupId() == R.id.menuFilterGroup) {
            //TODO
            return true;
        }
        else {
            super.onOptionsItemSelected(item);
            return true;
        }
    }

    private void setDayValues(String day){
        Log.d("My activity", "setting day to =" + day);

        int i = 0;
        for(String mealDay : dayArray){
            if(day.equals(mealDay)){
                weekday = Weekday.values() [i];
                dateDisplayMeals.setText("Displaying: "+ dayArray[i]);
            }
            i++;
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
