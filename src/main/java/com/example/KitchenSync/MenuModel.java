package com.example.KitchenSync;

import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.Calendar;

/**
 * Created by jeffrey on 4/8/14.
 */
public class MenuModel {
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
        return displayDay.toString() + ", " + calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DATE);
    }

    public void setRestriction(String r){
        //Todo catch bad strings here
        filter.setRestriction(Restriction.valueOf(r.toUpperCase()));
        update();
    }

    public void setDisplayDay(String weekday){
        //Todo catch bad strings here
        displayDay = Weekday.valueOf(weekday.toUpperCase());
        update();
    }

    public void setMatchGluten(Boolean matchGluten){
        filter.setMatchGluten(matchGluten);
        update();
    }

    public void setWeek(Week week){
        this.week = week;
        update();
    }

    private void update(){
        if (week != null){
            Day day = filter.applyFilter(week.getDay(displayDay));
            listAdapter = new MealListAdapter(day, context);
            expListView.setAdapter(listAdapter);
            for (int i=0; i < listAdapter.getGroupCount(); i++) {
                expListView.expandGroup(i);
            }
        }
    }
}
