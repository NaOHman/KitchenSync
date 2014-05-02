package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;
import com.softdev.Model.Day;
import com.softdev.Model.Restriction;
import com.softdev.Model.Week;
import com.softdev.Model.Weekday;
import com.softdev.R;

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

    public int getTodayID(){
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch(day){
            case 0:
                return R.id.mealOptionsMenu_Sunday;
            case 1:
                return R.id.mealOptionsMenu_Monday;
            case 2:
                return R.id.mealOptionsMenu_Tuesday;
            case 3:
                return R.id.mealOptionsMenu_Wednesday;
            case 4:
                return R.id.mealOptionsMenu_Thursday;
            case 5:
                return R.id.mealOptionsMenu_Friday;
            default:
                return R.id.mealOptionsMenu_Saturday;
        }
    }


    public boolean getGluten(){
        return filter.getMatchGluten();
    }

    public void setRestriction(String r){
        if(r.equals("Omnivore")) {
            filter.setRestriction(Restriction.NONE);
        } else {
            Restriction restriction = Restriction.valueOf(r.toUpperCase());
            filter.setRestriction(restriction);
        }
        update();
    }
    public boolean isToday(){
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return displayDay == Weekday.values()[day];
    }

    public void resetFilter(){
        filter.setRestriction(Restriction.NONE);
        filter.setMatchGluten(false);
        update();
    }

    public boolean filterChanged(){
        return !(filter.getRestriction() == Restriction.NONE
            && !filter.getMatchGluten());
    }

    public void setToday(){
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        setDisplayDay(Weekday.values()[day].toString());
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
            listAdapter = new MealListAdapter(day, context);
            expListView.setAdapter(listAdapter);
            for (int i=0; i < listAdapter.getGroupCount(); i++) {
                expListView.expandGroup(i);
            }
        }
    }
}
