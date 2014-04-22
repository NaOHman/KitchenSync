package com.softdev.Controller;

import com.softdev.Model.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by jeffrey on 3/14/14.
 */
public class Filter {
    private Restriction r;
    private boolean matchGluten;

    public Filter(Restriction r, boolean matchGluten){
        this.r = r;
        this.matchGluten = matchGluten;
    }

    public Week applyFilter(Week week){
        Week filteredWeek = new Week();
        for (Weekday weekday : Weekday.values()){
            filteredWeek.setDay(applyFilter(week.getDay(weekday)), weekday);
        }
        return filteredWeek;
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
        ArrayList<Station> stations = new ArrayList<Station>();
        Meal filteredMeal = new Meal(meal.getMealType());
        for (Station station: meal.getStations()){
            Station newStation = applyFilter(station);
            if (newStation != null)
                filteredMeal.addStation(newStation);
        }
        if (filteredMeal.getStations().size() > 0)
                return filteredMeal;
        return null;
    }

    public Station applyFilter(Station station){
        Set<Food> foods = station.getMatches(r, matchGluten);
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
