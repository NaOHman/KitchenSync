package com.softdev.Model;

/**
 * Created by jeffrey on 2/11/14.
 * the week class provides describes the menu for a week in cafe mac
 * it's composed of a list of days. Creating a weeks will automatically
 * parse the menu from cafe mac's website
 */

public class Week {
    private Day[] days = new Day[7];
    private static Week instance = null;

    protected Week(){
    }

    public static Week getInstance() {
        return instance;
    }

    public static void setInstance(Week instance) {
        Week.instance = instance;
    }

    public void setDay(Day day, Weekday dayOfWeek){
        days[dayOfWeek.ordinal()] = day;
    }
    /**
     * @param day a day of the week represented as an int
     * @return the daily menu of that day
     */
    public Day getDay(Weekday day){
        return days[day.ordinal()];
    }

    public Food getFoodById(long id){
        for(Day d : days)
            if(d != null)
                for(Meal m : d.getRealMeals())
                    for(Station s : m.getStations())
                        for(Food f : s.getFoods())
                            if(f.getFoodId() == id)
                                return f;
        return null;
    }

    public String toString(){
        String week = "";
        for (Day day : days)
            if (day != null)
                week += day.toString();
        return week;
    }
}
