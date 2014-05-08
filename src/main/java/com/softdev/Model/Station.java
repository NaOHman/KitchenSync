package com.softdev.Model;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by jeffrey on 2/11/14.
 * a Station represents an actual station in cafe mac and contains menu items
 */
public class Station extends DisplayItem{
    private String name;
    private List<Food> foods;

    public Station(String name){
        this.name = name;
        foods = new ArrayList<Food>();
    }

    public void setFoods(List<Food> foods){
        this.foods = foods;
    }
    /**
     * @return all the menu items served at the station
     */
    public List<Food> getFoods(){
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
    public List<Food> getMatches(Restriction restriction, boolean matchGlutenFree){
        List<Food> items = new ArrayList<Food>();
        for (Food food : foods)
            if (food.matchRestriction(restriction, matchGlutenFree))
                items.add(food);
        return items;
    }

    public String toString(){
        String station = "----" + name +"\n";
        for (Food food : foods)
            station += "------" + food.getName() + "\n";
        return station;
    }

    public Class getType(){
        return Station.class;
    }
}
