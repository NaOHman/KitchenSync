package com.softdev.Model;

import java.util.Date;

/**
 * Created by jeffrey on 4/19/14.
 */
public class Review {
    private Food food;
    private String reviewer;
    private String text;
    private int rating;
    private Date date;

    public Review(String reviewer, String text, Food food, int rating){
        this.food = food;
        this.reviewer = reviewer;
        this.food = food;
        this.rating = rating;
        date = new Date();
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

    public int getRating(){
        return rating;
    }

    public Date getDate(){
        return date;
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
