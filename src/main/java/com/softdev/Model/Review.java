package com.softdev.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jeffrey on 4/19/14.
 * Represents a user's opinion of a food
 */
public class Review implements Serializable {
    private String reviewer;
    private String text;
    private int rating;
    private Date createdAt;

    public Review(String reviewer, String text, int rating) {
        this.text = text;
        this.reviewer = reviewer;
        this.rating = rating;
        Calendar calendar = Calendar.getInstance();
        this.createdAt = calendar.getTime();
    }

    public String getReviewer() {
        return reviewer;
    }

    public int getRating() {
        return rating;
    }

    public String getStringRating() {
        if (rating == 0)
            return "";
        Integer r = rating;
        return r.toString() + " / 5";
    }

    public String getDate() {
        SimpleDateFormat df2 = new SimpleDateFormat("EEE MMM dd, yyyy");
        return df2.format(createdAt);
    }

    public String getText() {
        return text;
    }
}
