package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.widget.ExpandableListView;
import com.softdev.Model.Day;
import com.softdev.Model.Restriction;
import com.softdev.Model.Week;
import com.softdev.Model.Weekday;
import com.softdev.R;

import java.util.Calendar;

/**
 * Created by jeffrey on 4/8/14.
 * This is a facade that simplifies some of the UI programming
 * in the MenuActivity class
 */
public class MenuModel extends Activity{
    private Filter filter;
    private Context context;
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

    /**
     * @return the id of the menu option layout that corresponds
     * to today. This is needed to make sure the right day is
     * checked in the menu
     */
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

    /**
     * @return whether the app is displaying today's menu
     */
    public boolean isToday(){
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return displayDay == Weekday.values()[day];
    }

    public void resetFilter(){
        filter.setRestriction(Restriction.NONE);
        filter.setMatchGluten(false);
        update();
    }

    /**
     * @return whether the filter is in it's default state
     */
    public boolean filterChanged(){
        return !(filter.getRestriction() == Restriction.NONE
            && !filter.getMatchGluten());
    }

    /**
     * make the display show today's menu
     */
    public void setToday(){
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        setDisplayDay(Weekday.values()[day].toString());
    }

    public void setDisplayDay(String weekday){
        try {
            displayDay = Weekday.valueOf(weekday.toUpperCase());
            update();
        } catch (Exception e) { //bad string was passed
            return;
        }
    }
    public void setMatchGluten(){
        filter.setMatchGluten(!filter.getMatchGluten());
        this.update();
    }

    public Restriction getRestriction(){
        return filter.getRestriction();
    }

    /**
     * update the display
     */
    public void update(){
        Week week = Week.getInstance();
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
