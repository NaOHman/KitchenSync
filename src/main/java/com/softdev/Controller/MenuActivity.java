package com.softdev.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;
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
import java.net.URI;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MenuActivity extends Activity {
    private ExpandableListView expListView;
    private TextView dateDisplayMeals;
    private ImageView[] displayFilters = new ImageView[2];
    private MenuModel model;
    public final static String MEAL_NAME= "Controller.MenuActivity.MEAL_NAME";
    private final static Set<String> stations = new HashSet<String>(Arrays.asList("Breakfast Special", "Pasta", "Pizza", "Soup of the Day", "South", "East", "Grill"));

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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Not Connected to the Internet");
            alertDialogBuilder
                    .setMessage("Please connect to the internet so we can fetch today's menu")
                    .setCancelable(false)
                    .setNegativeButton("Okay",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

/*    public void startReviewActivity(View v){
        Intent intent = new Intent(MenuActivity.this, ReviewActivity.class);
        String mealName = (v.findViewById(R.id.lblListItem).getTag()).toString();

        //verifies itemLayout is not a station
        if(stations.contains(mealName)) return;


        intent.putExtra(MEAL_NAME, mealName);
        startActivity(intent);
    }*/


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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popupmenu, menu);
        menu.findItem(R.id.mealOptionsMenu_None).setChecked(true);
        menu.findItem(model.getTodayID()).setChecked(true);
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
        if (item.getGroupId() == R.id.mealOptionsMenu_None)
            model.clearRestrictions();
        updateFilterImgs();
        super.onOptionsItemSelected(item);
        return true;
    }

    private void updateFilterImgs(){
        displayFilters[1].setImageResource(R.drawable.background);
        displayFilters[0].setImageResource(R.drawable.background);
        Restriction r = model.getRestriction();
        if(r == Restriction.VEGAN)
            displayFilters[0].setImageResource(R.drawable.vegan);
        if(r == Restriction.VEGETARIAN)
            displayFilters[0].setImageResource(R.drawable.vegetarian);
        if(r == Restriction.PESCETARIAN)
            displayFilters[0].setImageResource(R.drawable.pescetarianicon);
        if(model.getGluten()) {
            if (r == Restriction.NONE)
                displayFilters[0].setImageResource(R.drawable.glutenfree);
            else
                displayFilters[1].setImageResource(R.drawable.glutenfree);
        }
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
                // check if success is true/false

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
            if (week != null){
                Log.d("WeekFetcher", "Sucessfully caught week");
                model.setWeek(week);
            }
        }

    }
}