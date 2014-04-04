package com.example.KitchenSync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.io.InputStream;

public class MyActivity extends Activity {
    private MealListAdapter listAdapter;
    private ExpandableListView expListView;
    private Week week = null;
    private Filter filter;

    private TextView dateDisplay;
    private Spinner selectDay;
    private String dayString;
    private Weekday weekday;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        filter = new Filter(Restriction.NONE, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createMenu();
        expListView = (ExpandableListView) findViewById(R.id.menu_expandable);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        dateDisplay = (TextView) findViewById(R.id.header_date);
        dateDisplay.setText(getString(R.string.Saturday) + ", " + calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DATE));
        setDayValues(getString(R.string.Saturday));
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
        if(day == getString(R.string.Sunday)) weekday = Weekday.SUNDAY;
        if(day == getString(R.string.Monday)) weekday = Weekday.MONDAY;
        if(day == getString(R.string.Tuesday)) weekday = Weekday.TUESDAY;
        if(day == getString(R.string.Wednesday)) weekday = Weekday.WEDNESDAY;
        if(day == getString(R.string.Thursday)) weekday = Weekday.THURSDAY;
        if(day == getString(R.string.Friday)) weekday = Weekday.FRIDAY;
        if(day == getString(R.string.Saturday)) weekday = Weekday.SATURDAY;
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
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                URI server = new URI(args[0]);
                request.setURI(server);
                HttpResponse response = httpClient.execute(request);
                InputStream inputStream = response.getEntity().getContent();
                String json = convertIStoText(inputStream);
                Gson gson = new Gson();
                Week week = gson.fromJson(json, Week.class);
                int i = 1+2;
                return week;
            } catch (Exception e) {
                return null;
            }
        }
        //changing shit
        @Override
        protected void onPostExecute(final Week week) {
            if (dialog.isShowing())
                dialog.dismiss();
            if (week !=null)
                setListData(week);
        }
    }
    public String convertIStoText(InputStream iS){
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(iS));
        String line;
        String result = "";
        try {
            while((line = bufferedReader.readLine()) != null)
                result += line;
            iS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
