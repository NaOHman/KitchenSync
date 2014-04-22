package com.softdev.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jeffrey on 2/11/14.
 * a Meal represents all a meal in cafe mac, it's broken up into different stations
 */

public class Meal {
    private Set<Station> stations;
    private MealType mealType;
    private List<Station> stationList;

    public Meal(MealType mealType){
        stations = new HashSet<Station>();
        this.mealType = mealType;
    }

    public void addStation(Station station){
        this.stations.add(station);
        this.stationList = new ArrayList<Station>(stations);
    }

    /**
     * @return a list of all the stations in a given meal
     */
    public List<Station> getStations(){
        if (stationList == null)
            stationList = new ArrayList<Station>(stations);
        return stationList;
    }

    /**
     * @param stations list of stations to replace the current list of stations
     */
    public void setStations(Set<Station> stations){
        this.stations = stations;
        this.stationList = new ArrayList<Station>(stations);
    }

    public ArrayList<String> getStationHeaders(){
        if (stationList == null)
            stationList = new ArrayList<Station>(stations);
        ArrayList<String> stationHeaders = new ArrayList<String>();
        for (Station station : stationList){
            stationHeaders.add(station.getName());
        }
        return stationHeaders;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
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
}
