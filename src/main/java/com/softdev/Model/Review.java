package com.softdev.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jeffrey on 4/19/14.
 */
public class Review {
    private Food food;
    private String reviewer;
    private String text;
    private Integer rating;
    private Date date;
    private Calendar calendar;
    private Integer day;
    private Integer month;
    private Integer year;

    public Review(String reviewer, String text, Food food, int rating){
        this.text = text;
        this.food = food;
        this.reviewer = reviewer;
        this.food = food;
        this.rating = rating;
        calendar = new GregorianCalendar();
        this.day = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getRating(){
        return rating.toString();
    }

    public String getDate(){
        return(day + "-" + month + "-" + year);
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
