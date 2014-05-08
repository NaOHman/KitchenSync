package com.softdev.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffrey on 2/11/14.
 * a Food represents a dish being served in cafe mac
 * it also contains data about the food such as dietary restrictions
 */
public class Food extends DisplayItem implements Serializable{
    private String name;
    private Boolean glutenFree;
    private Restriction restriction;
    private List<Review> reviews;
    private long foodId;

    public Food(String name, Restriction restriction, boolean glutenFree, List<Review> reviews){
        this.name = name;
        this.restriction = restriction;
        this.glutenFree = glutenFree;
        this.reviews = reviews;
    }
    /**
     * @param r matchGluten a String representing a dietary restriction possible options are
     *                    vegetarian, vegan, made-without-gluten, and seafood-watch
     * @return whether the food matches that dietary restriction
     */
    public boolean matchRestriction(Restriction r, boolean matchGluten){
        return (restriction.ordinal() <= r.ordinal()) && (glutenFree || !matchGluten);
    }

    public Double getAverageRating(){
        int rateCount = 0;
        double rating = 0;
        for(Review review: reviews){
            if (review.getRating() != 0){
                rating += review.getRating();
                rateCount++;
            }
        }
        if (rateCount == 0)
            return 0.0;
        return rating/rateCount;
    }

    public List<Review> getTextReviews(){
        List<Review> textReviews = new ArrayList<Review>();
        for(Review review : reviews){
            if(!review.getText().replaceAll("\\s", "").equals("")) {
                textReviews.add(review);
                Log.d("Found Review", review.getText());
            }
        }
        return textReviews;
    }

    public String getName(){
        return name;
    }

    public Class getType(){
        return Food.class;
    }

    public List<Review> getReviews(){
        return reviews;
    }

    public Restriction getRestriction(){
        return restriction;
    }

    public Boolean getGlutenFree(){
        return glutenFree;
    }

    public long getFoodId() {
        return foodId;
    }
}
