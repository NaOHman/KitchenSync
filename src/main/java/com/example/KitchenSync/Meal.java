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
                MacMenuItem food = new MacMenuItem(item, currentStation);
                currentStation.addMenuItem(food);
            }
        }
    }

    /**
     * Returns all menu items in served during a meal
     * @return all the menu items at a meal
     */
    public ArrayList<MacMenuItem> getMenuItems(){
        ArrayList<MacMenuItem> items = new ArrayList<MacMenuItem>();
        for (Station station : stations)
            items.addAll(station.getMenuItems());
        return items;
    }

    /**
     * @param restriction a dietary restriction, vegetarian, vegan, made-without-gluten, or seafood-watch
     * @return all the menu items in a meal that have the restriction e.g. all vegan items
     */
    public ArrayList<MacMenuItem> getMenuItemsWith(String restriction){
        ArrayList<MacMenuItem> items = new ArrayList<MacMenuItem>();
        for (Station station : stations)
            items.addAll(station.getMenuItemsWith(restriction));
        return items;
    }

    /**
     * @param restriction a dietary restriction, vegetarian, vegan, made-without-gluten, or seafood-watch
     * @return all the menu items in a meal without that restriction e.g. all items not on seafood-watch
     */
    public ArrayList<MacMenuItem> getMenuItemsWithout(String restriction){
        ArrayList<MacMenuItem> items = new ArrayList<MacMenuItem>();
        for (Station station : stations)
            items.addAll(station.getMenuItemsWithout(restriction));
        return items;
    }

    /**
     * @return a list of all the stations in a given meal
     */
    public ArrayList<Station> getStations(){
        return stations;
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

    public HashMap<String, List<String>> getItemSet(){
        HashMap<String, List<String>> itemSet = new HashMap<String, List<String>>();
        for (Station station : stations){
            itemSet.put(station.getName(), station.getMenuItemNames());
        }
        return itemSet;
    }
}
