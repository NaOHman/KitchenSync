package com.softdev.Model;
import java.util.ArrayList;
/**
 * Created by jeffrey on 2/11/14.
 * a Station represents an actual station in cafe mac and contains menu items
 */
public class Station {
    private final String name;
    private ArrayList<Food> foods;

    public Station(String name){
        this.name = name;
        foods = new ArrayList<Food>();
    }

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

    public ArrayList<String> getFoodNames(){
        ArrayList<String> items = new ArrayList<String>();
        for (Food food : foods){
            items.add(food.getName());
        }
        return items;
    }
}
