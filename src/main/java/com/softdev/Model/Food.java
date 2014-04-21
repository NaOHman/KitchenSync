package com.softdev.Model;

import java.util.Set;

/**
 * Created by jeffrey on 2/11/14.
 * a Food represents a dish being served in cafe mac
 * it also contains data about the food such as dietary restrictions
 */
public class Food {
    private String name;
    private String description;
    private Boolean glutenFree;
    private Restriction restriction;
    private Set<Review> reviews;
    private double rating;
    private int ratingCount;
    private long foodID;

    public Food(String name, String description, Restriction restriction, boolean glutenFree, Set<Review> reviews){
        this.name = name;
        this.description = description;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getName(){
        return name;
    }

    public Set<Review> getReviews(){
        return reviews;
    }

    public String getDescription(){
        return description;
    }

    public Restriction getRestriction(){
        return restriction;
    }

    public Boolean getGlutenFree(){
        return glutenFree;
    }
}
