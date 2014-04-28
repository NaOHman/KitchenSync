package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;
import com.softdev.Model.Day;
import com.softdev.Model.Restriction;
import com.softdev.Model.Week;
import com.softdev.Model.Weekday;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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

    public MenuModel(ExpandableListView expListView, Context context){
        this.context = context;
        this.expListView = expListView;
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        displayDay = Weekday.values()[day];
        filter = new Filter(Restriction.NONE, false);
    }

    public String getDisplayText(){
        return "Displaying: " + displayDay.toString();
    }

    public String getTodayText(){
        return displayDay.toString() + ", " + (calendar.get(Calendar.MONTH) + 1) + " / " + calendar.get(Calendar.DATE);
    }


    public boolean getGluten(){
        return filter.getMatchGluten();
    }

    public void setRestriction(String r){
        Restriction restriction = Restriction.valueOf(r.toUpperCase());
        if (filter.getRestriction() == restriction)
            filter.setRestriction(Restriction.NONE);
        else
            filter.setRestriction(restriction);
        update();
    }

    public void clearRestrictions(){
        filter.setRestriction(Restriction.NONE);
        filter.setMatchGluten(false);
        update();
    }

    public void setDisplayDay(String weekday){
        //Todo catch bad strings here
        displayDay = Weekday.valueOf(weekday.toUpperCase());
        update();
    }
    public void setMatchGluten(){
        filter.setMatchGluten(!filter.getMatchGluten());
        this.update();
    }

    public void setWeek(Week week){
        this.week = week;
        update();
    }

    public Restriction getRestriction(){
        return filter.getRestriction();
    }

    private void update(){
        if (week != null){
            Day day = filter.applyFilter(week.getDay(displayDay));
            Log.e("MenuModel", "Day is null?" + (day==null));
            listAdapter = new MealListAdapter(day, context);
            expListView.setAdapter(listAdapter);
            for (int i=0; i < listAdapter.getGroupCount(); i++) {
                expListView.expandGroup(i);
            }
        }
    }
}
