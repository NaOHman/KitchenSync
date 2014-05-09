package com.softdev.Controller;

import com.softdev.Model.*;

import java.util.List;

/**
 * Created by jeffrey on 3/14/14.
 * Filters contain a set of dietary restrictions and
 * can be applied to any level of the model to return a version
 * with onl the food that meets those restrictions
 */
public class Filter {
    private Restriction r;
    private boolean matchGluten;

    public Filter(Restriction r, boolean matchGluten){
        this.r = r;
        this.matchGluten = matchGluten;
    }

    public Day applyFilter(Day day){
        if (day == null)
                return null;
        Day filteredDay = new Day();
        for(int i=0; i<3; i++)
            filteredDay.setMeal(
                    applyFilter(day.getMeal(MealType.values()[i]))
                    ,MealType.values()[i]);
        return filteredDay;
    }

    public Meal applyFilter(Meal meal){
        if (meal == null)
            return null;
        Meal filteredMeal = new Meal(meal.getMealType());
        for (Station station: meal.getStations()){
            Station newStation = applyFilter(station);
            if (newStation != null)
                filteredMeal.addStation(newStation);
        }
        return filteredMeal;
    }

    public Station applyFilter(Station station){
        List<Food> foods = station.getMatches(r, matchGluten);
        if (foods.size() > 0){
            Station newStation = new Station(station.getName());
            newStation.setFoods(foods);
            return newStation;
        }
        return null;
    }
    public void setRestriction(Restriction restriction){
        r = restriction;
    }
    public void setMatchGluten(boolean match){
        matchGluten = match;
    }
    public boolean getMatchGluten(){
        return matchGluten;
    }
    public Restriction getRestriction(){
        return r;
    }
}
