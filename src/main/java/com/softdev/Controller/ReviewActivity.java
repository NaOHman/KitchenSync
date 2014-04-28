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

        Log.d("-----------------------------> current Reviews", reviews.toString());

        reviewList = (ListView) findViewById(R.id.reviewslist);
        if(reviews.size() > 0) {
            populateListView();
        }

    }

    private void populateListView(){
        ArrayAdapter<Review> adapter = new ReviewListAdapter();
        reviewList.setAdapter(adapter);
        reviewList.addFooterView(findViewById(R.layout.review_write));
    }

    private class ReviewListAdapter extends ArrayAdapter<Review> {
        public ReviewListAdapter(){
            super(ReviewActivity.this, R.layout.review_body, reviews);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;

            //make sure actually working with a view
            if(v == null){
                v = getLayoutInflater().inflate(R.layout.review_body, parent, false);

            }

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

    private void setTitleView(Food food){
        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
    }

}
