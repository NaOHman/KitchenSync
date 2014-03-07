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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

public class MyActivity extends Activity {
    private MealListAdapter listAdapter;
    private ExpandableListView expListView;
    private Week week = null;

    private TextView dateDisplay;
    private Spinner selectDay;
    private String dayString;
    private Weekday weekday;

    /**
     * poop poop poop Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //access calendar class to get current month, date, year, and day
        Calendar calendar = Calendar.getInstance();
        dateDisplay = (TextView) findViewById(R.id.header_date);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        setDayValues(day);
        dateDisplay.setText("Today is " + dayString + ", " + month + " / " + date);

        //sets up Day selector spinner
        selectDay = (Spinner) findViewById(R.id.daySelectorSpinner);
        selectDay.setSelection(day -1, false);
        selectDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MealSelector Spinner", "Item selected" + position);
                setDayValues(position + 1);
                if (week != null)
                    updateListData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });


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
    private void setDayValues(int day){
        Log.d("MyActivity", "Setting Day value = " + day);
        weekday = Weekday.values()[day-1];
        switch (day){
            case 1: dayString = "Sunday";
            case 2: dayString = "Monday";
            case 3: dayString = "Tuesday";
            case 4: dayString = "Wednesday";
            case 5: dayString = "Thursday";
            case 6: dayString = "Friday";
            case 7: dayString = "Saturday";
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
        }
    }
}
