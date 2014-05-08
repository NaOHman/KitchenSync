package com.softdev.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffrey on 2/11/14.
 * a day represents all the food served in cafe mac during a given
 * day. Each day has two meals, lunch and dinner
 */

public class Day {
    private Meal[] meals = new Meal[3];

    public Day(){
    }

    public void setMeal(Meal meal, MealType type){
        meals[type.ordinal()] = meal;
    }
    public Meal getMeal(MealType type){
        return meals[type.ordinal()];
    }
    public List<Meal> getRealMeals(){
        List<Meal> mealList = new ArrayList<Meal>();
        for(Meal meal : meals)
            if (meal != null)
                mealList.add(meal);
        Log.d("---------->MEAL", "got " + mealList.size() + " Meals");
        return mealList;
    }

    public String toString(){
        String day = "day\n";
        for(Meal meal : meals)
            if (meal != null)
                day += meal.toDebugString();
        return day;
    }
}
