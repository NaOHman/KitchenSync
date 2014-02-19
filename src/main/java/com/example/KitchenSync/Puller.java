package com.example.KitchenSync;
/**
 * Created by jeffrey on 2/11/14.
 */

import java.util.ArrayList;

/**
 * this class exists for testing and running different parts of the menu framework
 * It should not be included in our app
 */
public class Puller {
   public static void main(String[] args){
       Week week = new Week();
       System.out.println(week.toString());
       Meal tuesdayLunch = week.getDay(Weekday.TUESDAY).getLunch();
       ArrayList<MenuItem> veganOptions = tuesdayLunch.getMenuItemsWith("vegan");
       for (MenuItem option : veganOptions){
           System.out.println(option.toString());
       }
   }
}
