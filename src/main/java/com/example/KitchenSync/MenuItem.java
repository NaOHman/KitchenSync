package com.example.KitchenSync;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by jeffrey on 2/11/14.
 * a MenuItem represents a dish being served in cafe mac
 * it also contains data about the food such as dietary restrictions
 */
public class MenuItem {
    private String name;
    private String description;
    private Station location;
    private ArrayList<String> restrictions;
    // ArrayList<Review> reviews;

    /* takes an HTML element representing a food and then
       turns it into a useful java object.
     */
    public MenuItem(Element itemData, Station location){
        this.location = location;
        description = "";
        restrictions = new ArrayList<String>();
        Element foodData = itemData.getElementsByClass("eni-menu-item-name").first();
        name = foodData.text();
        makeDescription(itemData);
        makeRestrictions(foodData);
    }

    /**
     * helper method that adds a description to the menu item
     * @param itemData an HTML element collection representing a food's description
     */
    private void makeDescription(Element itemData){
        Elements foodDescription = itemData.getElementsByClass("eni-menu-description");
        if (foodDescription.size() > 0){
            description = foodDescription.first().text();
        }
    }

    /**
     * A helper method that adds dietary restriction information to the menu item
     * @param foodData a collection of HTML elements that contain dietary restriction info
     */
    private void makeRestrictions(Element foodData){
        Elements restrict = foodData.select(".tipbox");
        for (Element restriction : restrict){
                String restrictionType = restriction.className();
                restrictionType = restrictionType.substring(restrictionType.lastIndexOf(" ")+1);
                restrictions.add(restrictionType);
        }
    }

    /**
     * @return a human readable representation of the menu item
     */
    public String toString(){
        String food = name;
        if (description != ""){
            food = food + "\n\t" + description;
        }
        if (restrictions.size() > 0){
            for (String restriction : restrictions){
                food = food + "\n\t" + restriction;
            }
        }
        return food;
    }

    /**
     * @param restriction a String representing a dietary restriction possible options are
     *                    vegetarian, vegan, made-without-gluten, and seafood-watch
     * @return whether the food matches that dietary restriction
     */
    public boolean hasRestriction(String restriction){
        return restrictions.contains(restriction);
    }

    /**
     * @return the name of the menu item
     */
    public String getName(){
        return name;
    }

    /**
     * @return the descriptors associated with the menu item NOT the dietary restrictions
     */
    public String getDescription(){
        return description;
    }

    /**
     * @return list of restrictions that apply to the food.
     */
    public ArrayList<String> getRestrictions(){
        return restrictions;
    }

}
