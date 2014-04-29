package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.softdev.Model.Food;
import com.softdev.Model.Restriction;
import com.softdev.Model.Review;
import com.softdev.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dgoldste on 4/22/14.
 * with the help of the excellent ListView tutorial https://www.youtube.com/watch?v=WRANgDgM2Zg
 */

public class ReviewActivity extends Activity {
    private List<Review> reviews = new ArrayList<Review>();
    private TextView mealNameTxtView;
    private ListView reviewList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("Food");
        this.reviews = food.getReviews();

        setContentView(R.layout.review_main);
        setTitleView(food);
        reviewList = (ListView) findViewById(R.id.reviewslist);

        //TODO add space review so reviews != null
        Review space = new Review("", "", food, 0);
        reviews.add(space);

        Review test = new Review("David", "bloody diarrhea :(", food, 1);
        reviews.add(test);
        Review test1 = new Review("Jeffrey", "I would rather be strangled by Paul ", food, 1);
        reviews.add(test1);
        Review test2 = new Review("Hanna", "best meal all week!!", food, 5);
        reviews.add(test2);
        Review test3 = new Review("Paul", "I would rather be stranged by Jeffrey!!", food, 3);
        reviews.add(test3);


        populateListView();


    }

    private void populateListView(){
        ArrayAdapter<Review> adapter = new ReviewListAdapter();
        addFooter();
        reviewList.setAdapter(adapter);
    }

    private class ReviewListAdapter extends ArrayAdapter<Review> {
        public ReviewListAdapter(){
            super(ReviewActivity.this, R.layout.review_body, reviews);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;

            //make sure actually working with a view
            if(v == null)
                v = getLayoutInflater().inflate(R.layout.review_body, parent, false);

            //find Review to work with
            Review currReview = reviews.get(position);

            //find views
            TextView authorView = (TextView) v.findViewById(R.id.reviewTitleAuthor);
            TextView ratingView = (TextView) v.findViewById(R.id.reviewTitleRating);
            TextView dateView = (TextView) v.findViewById(R.id.reviewTitleDate);
            TextView reviewTextView = (TextView) v.findViewById(R.id.reviewText);

            //set views
            authorView.setText(currReview.getReviewer());
            ratingView.setText(currReview.getRating());
            dateView.setText(currReview.getDate());
            reviewTextView.setText(currReview.getText());

            return v;
        }
    }

    private void addFooter(){
        View footerView = View.inflate(this, R.layout.review_write, null);
        reviewList.addFooterView(footerView);
    }

    private void setTitleView(Food food){
        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
    }

}
