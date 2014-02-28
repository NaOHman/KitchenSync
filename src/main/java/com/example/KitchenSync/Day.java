package com.example.KitchenSync;
/**
 * Created by jeffrey on 2/11/14.
 * a day represents all the food served in cafe mac during a given
 * day. Each day has two meals, lunch and dinner
 */

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Day {
    private String date="";
    private Meal lunch;
    private Meal dinner;

    /**
     * Creates a new daily menu object containing 2 meals, lunch and dinner
     * @param dayElem an HTML element that contains the data needed to create a daily menu
     */
    public Day(Element dayElem){
        Elements meals = dayElem.select(".row-item > .my-day-menu-table");
        if (dayElem.select("> *").hasClass("menu-date-heading")){
            date = dayElem.getElementsByClass("menu-date-heading").first().text();
            dinner = new Meal(meals.get(1));
            lunch = new Meal(meals.get(0));
        }
        else{
            //this means there's no menu for the day
            lunch = null;
            dinner = null;
        }
    }

    /**
     * @return the dinner meal of a given day
     */
    public Meal getDinner(){
        return dinner;
    }

    /**
     * @return the lunch meal of a given day
     */
    public Meal getLunch(){
        return lunch;
    }

    /**
     * @return the days date in string form e.g. Monday, February 17th, 2014
     */
    public String getDate(){
        return date;
    }

    /**
     * @return a human readable representation of the day's menu
     */
    public String toString(){
        if (lunch != null){
            return date + "\nLUNCH \n" + lunch.toString() + "\nDINNER\n" + dinner.toString();
        } else{
            return "No Menu Published";
        }
    }
}
