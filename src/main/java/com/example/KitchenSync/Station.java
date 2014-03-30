package com.example.KitchenSync;
import java.util.ArrayList;
/**
 * Created by jeffrey on 2/11/14.
 * a Station represents an actual station in cafe mac and contains menu items
 */
public class Station {
    private final String name;
    private ArrayList<MacMenuItem> menuItems;

    /**
     * Creates a station
     * @param name the name of the Station e.g. South
     */
    public Station(String name){
        this.name = name;
        menuItems = new ArrayList<MacMenuItem>();
    }

    /**
     * adds a menu item to the station
     * @param menuItem a menu item served at the station
     */
    public void addMenuItem(MacMenuItem menuItem){
       menuItems.add(menuItem);
    }

    /**
     * @return all the menu items served at the station
     */
    public ArrayList<MacMenuItem> getMenuItems(){ return menuItems; }

    /**
     * @return returns station's name
     */
    public String getName(){
        return name;
    }
    /**
     * @return a human readable representation of the Station
     */
    public String toString(){
        String station = name + "\n";
        for (MacMenuItem menuItem : menuItems){
            station = station + menuItem.toString() + "\n";
        }
        return station;
    }

    /**
     * @param restriction a dietary restriction either vegan, vegetarian, made-without-gluten, or seafood-watch
     * @return a list of all items at the station that match the restriction e.g. all vegan options
     */
    public ArrayList<MacMenuItem> getMenuItemsWith(String restriction){
        ArrayList<MacMenuItem> items = new ArrayList<MacMenuItem>();
        for (MacMenuItem menuItem : menuItems){
            if (menuItem.hasRestriction(restriction))
                items.add(menuItem);
        }
        return items;
    }

    /**
     * @param restriction a dietary restriction either vegan, vegetarian, made-without-gluten, or seafood-watch
     * @return a list of all items at the station without the restriction e.g. all items not on seafood watch
     */
    public ArrayList<MacMenuItem> getMenuItemsWithout(String restriction){
        ArrayList<MacMenuItem> items = new ArrayList<MacMenuItem>();
        for (MacMenuItem menuItem : menuItems){
            if (!menuItem.hasRestriction(restriction))
                items.add(menuItem);
        }
        return items;
    }
    public ArrayList<String> getMenuItemNames(){
        ArrayList<String> items = new ArrayList<String>();
        for (MacMenuItem menuItem : menuItems){
            items.add(menuItem.getName());
        }
        return items;
    }
}
