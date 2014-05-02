package com.softdev.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jeffrey on 4/19/14.
 */
public class Review implements Serializable {
    private String reviewer;
    private String text;
    private int rating;
    private Date createdAt;

    public Review(String reviewer, String text, int rating){
        this.text = text;
        this.reviewer = reviewer;
        this.rating = rating;
        Calendar calendar = Calendar.getInstance();
        this.createdAt = calendar.getTime();
    }

    public String getReviewer() {
        return reviewer;
    }

    public int getRating(){
        return rating;
    }

    public String getStringRating(){
        Integer r = rating;
        return r.toString();
    }

    public String getDate(){
        SimpleDateFormat df2 = new SimpleDateFormat("EEE MMM dd, yyyy");
        return df2.format(createdAt);
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
