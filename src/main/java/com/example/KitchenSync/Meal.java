package com.example.KitchenSync;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jeffrey on 2/11/14.
 * a Meal represents all a meal in cafe mac, it's broken up into different stations
 */

public class Meal {
    private ArrayList <Station> stations;
    //int mealScore

    /**
     * constructor that creates a meal
     * @param mealData HTML Element containing the data needed to make a meal
     */
    public Meal(Element mealData){
        stations = new ArrayList<Station>();
        Elements items = mealData.select("tbody > *");
        makeStations(items);
    }

    public Meal(){
        stations = new ArrayList<Station>();
    }

    public void addStation(Station station){
        this.stations.add(station);
    }

    /**
     * a helper method for the constructor that creates a station objects
     * @param items a collection of HTML elements that contain data relavent to the
     *              creation of a meal
     */
    private void makeStations(Elements items){
        Station currentStation = new Station("");
         for (Element item : items){
            if (item.hasClass("menu-station")) {
                String stationName = item.getElementsByTag("td").first().text();
                currentStation = new Station(stationName);
                stations.add(currentStation);
            } else if (item.hasClass("price-")) {
                Food food = new Food(item);
                currentStation.addMenuItem(food);
            }
        }
    }

    /**
     * @return a list of all the stations in a given meal
     */
    public ArrayList<Station> getStations(){
        return stations;
    }

    /**
     * @param stations list of stations to replace the current list of stations
     */
    public void setStations(ArrayList<Station> stations){
        this.stations = stations;
    }

    /**
     * @return a human readable version of the meal
     */
    public String toString(){
        String meal = "";
        for (Station station : stations){
            meal = meal + station.toString();
        }
        return meal;
    }
    public ArrayList<String> getStationHeaders(){
        ArrayList<String> stationHeaders = new ArrayList<String>();
        for (Station station : stations){
            stationHeaders.add(station.getName());
        }
        return stationHeaders;
    }
}
