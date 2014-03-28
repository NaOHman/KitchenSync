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
import android.widget.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;

public class MyActivity extends Activity {
    private MealListAdapter listAdapter;
    private ExpandableListView expListView;
    private Week week = null;
    private Filter filter;

    private TextView dateDisplay;
    private Spinner selectFilter;
    private Spinner selectDay;
    private String dayString;
    private Weekday weekday;
    private CheckBox glutenBox;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        filter = new Filter(Restriction.NONE, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //access calendar class to get current month, date, year, and day
        Calendar calendar = Calendar.getInstance();
        dateDisplay = (TextView) findViewById(R.id.header_date);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        setDayValues(day);
        dateDisplay.setText(dayString + ", " + month + " / " + date);

        //sets up Day selector spinner
        selectDay = (Spinner) findViewById(R.id.daySelectorSpinner);
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
            }
        });
        selectDay.setSelection(day -1, false);

        selectFilter = (Spinner) findViewById(R.id.spinner_filter);
        selectFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.setRestriction(Restriction.values()[position]);
                if (week != null)
                    updateListData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        selectFilter.setSelection(3);
        //thanks to our bro mkyong for the help here
        glutenBox = (CheckBox) findViewById(R.id.glutenFreeCheckBox);
        glutenBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setMatchGluten( ((CheckBox) v).isChecked());
                updateListData();
            }
        });


        expListView = (ExpandableListView) findViewById(R.id.menu_expandable);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            new WeekDataFetcher().execute(getString(R.string.cafe_url));
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
        Day day = filter.applyFilter(week.getDay(weekday));
        listAdapter = new MealListAdapter(day, this);
        expListView.setAdapter(listAdapter);
        for (int i=0; i < listAdapter.getGroupCount(); i++) {
            expListView.expandGroup(i);
        }
    }

    private class WeekDataFetcher extends AsyncTask<String, Void, Week> {
        private ProgressDialog dialog = new ProgressDialog(MyActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Welcome to KitchenSync! Collecting Menu Information..");
            this.dialog.show();
        }

        @Override
        protected Week doInBackground(final String... args) {
            try {
                Document doc = Jsoup.connect(args[0]).get();
                Week week = new Week(doc);
                return week;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Week week) {
            if (dialog.isShowing())
                dialog.dismiss();
            if (week !=null)
                setListData(week);
        }
    }
}
