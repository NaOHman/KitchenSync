package com.softdev.Model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jeffrey on 2/11/14.
 * a Station represents an actual station in cafe mac and contains menu items
 */
public class Station {
    private String name;
    private Set<Food> foods;
    private List<Food> listFoods;

    public Station(String name){
        this.name = name;
        foods = new HashSet<Food>();
    }

    public void addMenuItem(Food food){
       foods.add(food);
    }

    public void setFoods(Set<Food> foods){
        this.foods = foods;
        this.listFoods = new ArrayList<Food>(foods);
    }
    /**
     * @return all the menu items served at the station
     */
    public List<Food> getFoods(){
        if (listFoods == null)
            listFoods = new ArrayList<Food>(foods);
        return listFoods;
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
    public Set<Food> getMatches(Restriction restriction, boolean matchGlutenFree){
        Set<Food> items = new HashSet<Food>();
        for (Food food : foods)
            if (food.matchRestriction(restriction, matchGlutenFree))
                items.add(food);
        return items;
    }

    public ArrayList<String> getFoodNames(){
        ArrayList<String> items = new ArrayList<String>();
        for (Food food : listFoods){
            items.add(food.getName());
        }
        return items;
    }
}
