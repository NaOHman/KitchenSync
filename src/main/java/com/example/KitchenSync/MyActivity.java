package com.example.KitchenSync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

public class MyActivity extends Activity {
    private StationListAdapter listAdapter;
    private ExpandableListView expListView;
    Week week;
    private Spinner mealSelectorSpinner;
    private Spinner selectDay;
    private String dayString;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //sets up date in header and creates dayString, which can be used in other methods
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        TextView date =  (TextView) findViewById(R.id.header_date);
        dayString = "STRING NOT CURRENTLY SET";
        switch (day){
            case 0: dayString = "Monday";
                break;
            case 1: dayString = "Tuesday";
                break;
            case 2: dayString = "Wednesday";
                break;
            case 3: dayString = "Thursday";
                break;
            case 4: dayString = "Friday";
                break;
            case 5: dayString = "Saturday";
                break;
            case 6: dayString = "Sunday";
                break;
            default:dayString = "INVALID DAY";
                break;
        }
        date.setText("Displaying meals for: ");

        //sets up dateDisplay Spinner and Button
        Button displayDay = (Button) findViewById(R.id.displayMealButton);
        selectDay = (Spinner) findViewById(R.id.daySelectorSpinner);

        //sets selected day to current day for selecting meals TODO: w/o using button
        displayDay.setOnClickListener(new View.OnClickListener() {
            @Override
            //TODO: for some reason does not switch... make work!
            public void onClick(View v) {
                String daySelected = selectDay.getSelectedItem().toString();
                dayString = daySelected;

            }
        });

        /**
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
    public void setListData(Week week){
        Meal meal = week.getDay(Weekday.WEDNESDAY).getLunch();
        listAdapter = new StationListAdapter(this, meal);
        expListView.setAdapter(listAdapter);
    }

    private class WeekDataFetcher extends AsyncTask<String, Void, Week> {
        private ProgressDialog dialog = new ProgressDialog(MyActivity.this);
        private Exception exception;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Collecting Menu Information");
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
        }*/






    }
}
