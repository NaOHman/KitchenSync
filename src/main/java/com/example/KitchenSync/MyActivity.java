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
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.io.InputStream;

public class MyActivity extends Activity {
    private ExpandableListView expListView;
    private TextView dateDisplay,dateDisplayMeals;
    private MenuModel model;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        expListView = (ExpandableListView) findViewById(R.id.menu_expandable);
        model = new MenuModel(expListView, this);
        createDaydisplay();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new WeekDataFetcher().execute(getString(R.string.server_url));
        } else {
            //TODO network not connected do something
        }
    }
    /**
     * sets up menu bar in main Layout
     */
    private void createDaydisplay(){
        dateDisplayMeals = (TextView) findViewById(R.id.currentMealDateDisplay);
        dateDisplayMeals.setText(model.getDisplayText());
        dateDisplay = (TextView) findViewById(R.id.header_date);
        dateDisplay.setText(model.getTodayText());
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
        if (item.getGroupId() == R.id.menuMealGroup) {
            model.setDisplayDay(item.getTitle().toString());
            dateDisplayMeals.setText(model.getDisplayText());
            return true;
        }
        if (item.getGroupId() == R.id.menuFilterGroup) {
            model.setRestriction(item.getTitle().toString());
            return true;
        }
        super.onOptionsItemSelected(item);
        return true;
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
                Log.d("WeekDataFetcher", "Establishing Connection");
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                URI server = new URI(args[0]);
                request.setURI(server);
                HttpResponse response = httpClient.execute(request);
                InputStream inputStream = response.getEntity().getContent();
                String json = IOUtils.toString(inputStream);
                Week week = (new Gson()).fromJson(json, Week.class);
                return week;
            } catch (Exception e) {
                Log.e("WeekDataFetcher", "Error collecting Data");
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(final Week week) {
            if (dialog.isShowing())
                dialog.dismiss();
            if (week !=null){
                Log.d("WeekFetcher", "Sucessfully caught week");
                model.setWeek(week);
            }
        }
    }
}