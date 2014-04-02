package com.example.KitchenSync;
import java.util.ArrayList;
/**
 * Created by jeffrey on 2/11/14.
 * a Station represents an actual station in cafe mac and contains menu items
 */
public class Station {
    private final String name;
    private ArrayList<Food> foods;

    /**
     * Creates a station
     * @param name the name of the Station e.g. South
     */
    public Station(String name){
        this.name = name;
        foods = new ArrayList<Food>();
    }

    /**
     * adds a menu item to the station
     * @param food a menu item served at the station
     */
    public void addMenuItem(Food food){
       foods.add(food);
    }

    public void setFoods(ArrayList<Food> foods){
        this.foods = foods;
    }
    /**
     * @return all the menu items served at the station
     */
    public ArrayList<Food> getFoods(){
        return foods;
    }

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
        for (Food food : foods){
            station = station + food.toString() + "\n";
        }
        return station;
    }

    /**
     * @param restriction a dietary restriction either vegan, vegetarian, made-without-gluten, or seafood-watch
     * @return a list of all items at the station that match the restriction e.g. all vegan options
     */
    public ArrayList<Food> getMatches(Restriction restriction, boolean matchGlutenFree){
        ArrayList<Food> items = new ArrayList<Food>();
        for (Food food : foods){
            if (food.matchRestriction(restriction, matchGlutenFree))
                items.add(food);
        }
        return items;
    }

    public ArrayList<String> getMenuItemNames(){
        ArrayList<String> items = new ArrayList<String>();
        for (Food food : foods){
            items.add(food.getName());
        }
        return items;
    }
}
