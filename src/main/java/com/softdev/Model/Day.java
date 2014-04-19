package com.softdev.Model;
/**
 * Created by jeffrey on 2/11/14.
 * a day represents all the food served in cafe mac during a given
 * day. Each day has two meals, lunch and dinner
 */

public class Day {
    private Meal lunch;
    private Meal dinner;

    public Day(){
        lunch = null;
        dinner = null;
    }

    public void setDinner(Meal dinner){
        this.dinner = dinner;
    }

    public void setLunch(Meal lunch){
        this.lunch = lunch;
    }

    public Meal getDinner(){
        return dinner;
    }

    public Meal getLunch(){
        return lunch;
    }
}
