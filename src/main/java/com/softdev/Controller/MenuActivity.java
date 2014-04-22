package com.softdev.Controller;

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
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.softdev.Model.Week;
import com.softdev.Model.Weekday;
import com.softdev.R;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;

public class MenuActivity extends Activity {
    private ExpandableListView expListView;
    private TextView dateDisplay,dateDisplayMeals;
    private ImageView[] displayFilters = new ImageView[4];
    private int currentImageView = 0;
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
        displayFilters[0]  = (ImageView) findViewById(R.id.filterImageView0);
        displayFilters[1] = (ImageView) findViewById(R.id.filterImageView1);
        displayFilters[2] = (ImageView) findViewById(R.id.filterImageView2);
        displayFilters[3] = (ImageView) findViewById(R.id.filterImageView3);

    }

    //creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
        return true;
    }

    public void optionsButtonClick(View v){
        openOptionsMenu();
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
        //TODO background incon color / resolution
        //TODO: check Restriction.getValues() so that dont duplicate images
        if (item.getGroupId() == R.id.menuFilterGroup) {
            model.setRestriction(item.getTitle().toString());
            if(item.getTitle() == getString(R.string.vegan)) displayFilters[currentImageView].setImageResource(R.drawable.veganicon);
            if(item.getTitle() == getString(R.string.vegetarian)) displayFilters[currentImageView].setImageResource(R.drawable.vegetarianicon);
            if(item.getTitle() == getString(R.string.pescetarian)) displayFilters[currentImageView].setImageResource(R.drawable.pescetarianicon);
            currentImageView++;
            return true;
        }
        if (item.getGroupId() == R.id.GlutenFreeBoolean){
            model.setMatchGluten();
            displayFilters[currentImageView].setImageResource(R.drawable.noglutenicon);
            currentImageView++;
        }
        if (item.getGroupId() == R.id.mealOptionsMenu_None){
            for(int i=0;i<4;i++){
                displayFilters[i].setImageResource(R.drawable.transparent);
            }
            //TODO: model.setRestriction(item.getTitle().toString());
            currentImageView = 0;

        }

        super.onOptionsItemSelected(item);
        return true;
    }

    private class WeekDataFetcher extends AsyncTask<String, Void, Week> {
        private ProgressDialog dialog = new ProgressDialog(MenuActivity.this);
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
                week.getDay(Weekday.MONDAY).getRealMeals();
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