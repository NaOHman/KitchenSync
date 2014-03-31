package com.example.KitchenSync;
import android.util.Log;
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
    private Boolean glutenFree;
    private Restriction restriction;
    // ArrayList<Review> reviews;

    /* takes an HTML element representing a food and then
       turns it into a useful java object.
     */
    public MenuItem(Element itemData){
        description = "";
        glutenFree = false;
        Element foodData = itemData.getElementsByClass("eni-menu-item-name").first();
        name = foodData.text();
        makeDescription(itemData);
        restriction = makeRestrictions(foodData);
    }
    public MenuItem(String name, String description, ArrayList<String> restrictions){
        glutenFree = false;
        this.name = name;
        this.description = description;
        this.restriction = makeRestrictions(restrictions);
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
    private Restriction makeRestrictions(Element foodData){
        Elements restrict = foodData.select(".tipbox");
        Restriction highestLevel = Restriction.NONE;
        for (Element restriction : restrict){
            String restrictionType = restriction.className();
            restrictionType = restrictionType.substring(restrictionType.lastIndexOf(" ")+1);
            Restriction r = stringToRestriction(restrictionType);
            Log.d("restriction level = ", ""+r);
            if (r.ordinal() < highestLevel.ordinal())
                highestLevel = r;
            Log.d("Highest level = ", "" + highestLevel);
        }
        return highestLevel;
    }
    private Restriction makeRestrictions(ArrayList<String> restrictions){
        Restriction highestLevel = Restriction.NONE;
        for (String r : restrictions){
            Restriction restriction = stringToRestriction(r);
            if (restriction.ordinal() < highestLevel.ordinal())
                highestLevel = restriction;
        }
        return highestLevel;
    }

    private Restriction stringToRestriction(String s){
        Log.d("MenuItem" + name, s);
        if (s.equals("vegan"))
            return Restriction.VEGAN;
        if (s.equals("vegetarian"))
            return Restriction.VEGETARIAN;
        if (s.equals("seafood-watch"))
            return Restriction.PESCETARIAN;
        if (s.equals("made-without-gluten"))
            glutenFree = true;
        return Restriction.NONE;
    }

    /**
     * @return a human readable representation of the menu item
     */
    public String toString(){
        String food = name;
        if (description != ""){
            food = food + "\n\t" + description;
        }
        /**if (restrictions.size() > 0){
            for (String restriction : restrictions){
                food = food + "\n\t" + restriction;
            }
        } */
        return food;
    }

    /**
     * @param r matchgluten a String representing a dietary restriction possible options are
     *                    vegetarian, vegan, made-without-gluten, and seafood-watch
     * @return whether the food matches that dietary restriction
     */
    public boolean matchRestriction(Restriction r, boolean matchGluten){
        return (restriction.ordinal() <= r.ordinal()) && (glutenFree || !matchGluten);
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

}
