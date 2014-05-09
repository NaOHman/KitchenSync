package com.softdev.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.*;
import com.softdev.Model.Restriction;
import com.softdev.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import com.google.gson.Gson;
import com.softdev.Model.Week;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.io.InputStream;

/**
 * the Activity that displays the menu for a given day
 */
public class MenuActivity extends Activity {
    private long backPressedTime = 0;    // used by onBackPressed()
    private ExpandableListView expListView;
    private TextView dateDisplayMeals;
    private ImageView[] displayFilters = new ImageView[2];
    private MenuModel model;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        expListView = (ExpandableListView) findViewById(R.id.menu_expandable);
        model = new MenuModel(expListView, this);
        createDaydisplay();
        pullWeek();
    }

    public void pullWeek(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new WeekDataFetcher().execute(getString(R.string.server_url));
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Not Connected to the Internet");
            alertDialogBuilder
                    .setMessage("Please connect to the internet so we can fetch today's menu")
                    .setCancelable(false)
                    .setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            finish();
                        }
                    })
                    .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            pullWeek();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    /**
     * sets up menu bar in main Layout
     */
    private void createDaydisplay(){
        dateDisplayMeals = (TextView) findViewById(R.id.currentMealDateDisplay);
        dateDisplayMeals.setText(model.getDisplayText());

        displayFilters[0]  = (ImageView) findViewById(R.id.filterImageView0);
        displayFilters[1] = (ImageView) findViewById(R.id.filterImageView1);
    }

    //creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
        menu.findItem(R.id.mealOptionsMenu_None).setChecked(true);
        menu.findItem(model.getTodayID()).setChecked(true);
        return true;
    }

    public void optionsButtonClick(View v){
        openOptionsMenu();
    }
    //day or filter option chosen in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menuMealGroup) {
            model.setDisplayDay(item.getTitle().toString());
            dateDisplayMeals.setText(model.getDisplayText());
            item.setChecked(true);
        }
        if (item.getGroupId() == R.id.DietaryRestrictions) {
            item.setChecked(true);
            model.setRestriction(item.getTitle().toString());
        }
        if (item.getGroupId() == R.id.GlutenFreeBoolean) {
            model.setMatchGluten();
            item.setChecked(model.getGluten());
        }
        if (item.getItemId() == R.id.mealOptionsMenu_refresh)
        {
            pullWeek();
        }
        if (item.getItemId() == R.id.mealOptionsMenu_help){
            openHelpActivity();
        }
        updateFilterImgs();
        super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * the back button first resets the filter
     * then it resets the day
     * finally, if it is hit twice it exits the app
     */
    @Override
    public void onBackPressed() {
        if(model.filterChanged()){
            model.resetFilter();
            menu.findItem(R.id.mealOptionsMenu_None).setChecked(true);
            menu.findItem(R.id.mealOptionsMenu_Glutenfree).setChecked(false);
            updateFilterImgs();
            return;
        }
        if(!model.isToday()){
            model.setToday();
            menu.findItem(model.getTodayID()).setChecked(true);
            dateDisplayMeals.setText(model.getDisplayText());
            return;
        }
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
        } else {    // this guy is serious
            super.onBackPressed();       // bye
        }
    }

    private void openHelpActivity(){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    /**
     * sets the images in the header that correspond to the filter
     */
    private void updateFilterImgs(){
        displayFilters[1].setImageResource(R.drawable.transparent);
        displayFilters[0].setImageResource(R.drawable.transparent);
        Restriction r = model.getRestriction();
        if(r == Restriction.VEGAN)
            displayFilters[0].setImageResource(R.drawable.vegandark);
        if(r == Restriction.VEGETARIAN)
            displayFilters[0].setImageResource(R.drawable.vegetariandark);
        if(r == Restriction.PESCETARIAN)
            displayFilters[0].setImageResource(R.drawable.pescetariandark);
        if(model.getGluten()) {
            if (r == Restriction.NONE)
                displayFilters[0].setImageResource(R.drawable.glutenfreedark);
            else
                displayFilters[1].setImageResource(R.drawable.glutenfreedark);
        }
    }

    /**
     * throws up a dialog telling the user what went wrong in case of
     * a server error
     */
    private void serverError(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Server error");
        alertDialogBuilder
                .setMessage("There was a problem getting the menu")
                .setCancelable(false)
                .setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        finish();
                    }
                })
                .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        pullWeek();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Starts new thread that tries to grab a week from the server
     */
    private class WeekDataFetcher extends AsyncTask<String, Void, Week> {
        private ProgressDialog dialog = new ProgressDialog(MenuActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Welcome to KitchenSync! Collecting Menu Information..");
            this.dialog.setCancelable(false);
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
                // check if success is true/false
                try {
                    JSONObject reply = new JSONObject(json);
                    if (reply.getBoolean("success")){
                        String weekJson = reply.getJSONObject("week").toString();
                        return new Gson().fromJson(weekJson, Week.class);
                    } else {
                        Log.e("Week Fetcher", "FAILURE CRASH AND BURN");
                        return null;
                    }
                } catch (JSONException e){
                    Log.e("WeekCatcher", "Malformed Data");
                    return null;
                }
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
            if (week != null){
                Log.d("WeekFetcher", "Successfully caught week");
                Week.setInstance(week);
                model.update();
            }else{
                serverError();
            }
        }

    }
}