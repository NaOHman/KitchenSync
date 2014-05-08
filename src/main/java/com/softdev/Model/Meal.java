package com.softdev.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffrey on 2/11/14.
 * a Meal represents all a meal in cafe mac, it's broken up into different stations
 */

public class Meal {
    private List<Station> stations;
    private MealType mealType;

    public Meal(MealType mealType){
        stations = new ArrayList<Station>();
        this.mealType = mealType;
    }

    public void addStation(Station station){
        this.stations.add(station);
    }

    public List<Station> getStations(){
        return stations;
    }

    public MealType getMealType() {
        return mealType;
    }

    public List<DisplayItem> getDisplayItems(){
        List<DisplayItem> displayItems = new ArrayList<DisplayItem>();
        for (Station station : stations){
            displayItems.add(station);
            for (Food food : station.getFoods())
                displayItems.add(food);
        }
        return displayItems;
    }

    public String toString(){
        switch (mealType){
            case BREAKFAST:
                return "Breakfast";
            case LUNCH:
                return "Lunch";
            case DINNER:
                return "Dinner";
        }
        return "No Meal Published";
    }

    public String toDebugString(){
        String meal = "--"+mealType.toString() + "\n";
        for(Station station : stations)
            meal += station.toString();
        return meal;
    }
}
