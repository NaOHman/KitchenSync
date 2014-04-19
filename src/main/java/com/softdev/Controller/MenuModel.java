package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import com.softdev.Model.Day;
import com.softdev.Model.Restriction;
import com.softdev.Model.Week;
import com.softdev.Model.Weekday;
import com.softdev.R;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jeffrey on 4/8/14.
 */
public class MenuModel extends Activity{
    private Filter filter;
    private Context context;
    private Week week;
    private Weekday displayDay;
    private MealListAdapter listAdapter;
    private ExpandableListView expListView;
    private static Calendar calendar = Calendar.getInstance();
    private ImageView[] displayFilters = new ImageView[4];
    private List<String> currentRestrictions;
    private int[] filterIcons = {R.drawable.veganicon, R.drawable.vegetarianicon, R.drawable.pescetarianicon, R.drawable.noglutenicon};
    private int currentImageView = 0;
    private boolean gluten = false;

    public MenuModel(ExpandableListView expListView, Context context){
        this.context = context;
        this.expListView = expListView;
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        displayDay = Weekday.values()[day];
        filter = new Filter(Restriction.NONE, false);

        /**
        displayFilters[0]  = (ImageView) findViewById(R.id.filterImageView0);
        displayFilters[1] = (ImageView) findViewById(R.id.filterImageView1);
        displayFilters[2] = (ImageView) findViewById(R.id.filterImageView2);
        displayFilters[3] = (ImageView) findViewById(R.id.filterImageView3);
        */
    }

    public String getDisplayText(){
        return "Displaying: " + displayDay.toString();
    }

    public String getTodayText(){
        return displayDay.toString() + ", " + calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DATE);
    }

    public void setRestriction(String r){
        currentRestrictions.add(r);
        filter.setRestriction(Restriction.valueOf(r.toUpperCase()));
        update();
    }

    private void updateFilterImgs(){
        currentRestrictions.clear();
        int i = 0;
        for(String r : currentRestrictions){
            if(r == getString(R.string.vegan)){
                displayFilters[i].setImageResource(filterIcons[0]);
                currentImageView++;
            }
            if(r == getString(R.string.vegetarian)){
                displayFilters[i].setImageResource(filterIcons[1]);
                currentImageView++;
            }
            if(r == getString(R.string.pescetarian)){
                displayFilters[i].setImageResource(filterIcons[2]);
                currentImageView++;
            }
            i++;
        }
        if(gluten){
            displayFilters[i].setImageResource(filterIcons[3]);
            currentImageView++;
        }
    }

    public void setDisplayDay(String weekday){
        //Todo catch bad strings here
        displayDay = Weekday.valueOf(weekday.toUpperCase());
        update();
    }


    public void setMatchGluten(){
        if (gluten){
            filter.setMatchGluten(false);
            gluten = false;
        }
        else{
            filter.setMatchGluten(true);
            gluten = true;
        }
        update();
    }

    public void setWeek(Week week){
        this.week = week;
        update();
    }


    private void update(){
        if (week != null){
    //      updateFilterImgs();
            Day day = filter.applyFilter(week.getDay(displayDay));
            listAdapter = new MealListAdapter(day, context);
            expListView.setAdapter(listAdapter);
            for (int i=0; i < listAdapter.getGroupCount(); i++) {
                expListView.expandGroup(i);
            }
        }
    }
}
