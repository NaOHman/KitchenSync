package com.example.KitchenSync;
/**
 * Created by jeffrey on 2/11/14.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * this class exists for testing and running different parts of the menu framework
 * It should not be included in our app
 */
public class Puller {
   public static void main(String[] args){
       try {
            Document doc = Jsoup.connect("http://macalester.cafebonappetit.com/hungry/cafe-mac/").get();
            Week week = new Week(doc);
            System.out.println(week.toString());
            Meal tuesdayLunch = week.getDay(Weekday.TUESDAY).getLunch();
            ArrayList<MenuItem> veganOptions = tuesdayLunch.getMenuItemsWith("vegan");
            for (MenuItem option : veganOptions){
                System.out.println(option.toString());
       }
       } catch (IOException e){
           return;
       }
   }
}
