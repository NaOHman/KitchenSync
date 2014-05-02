package com.softdev.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private double averageRating;
    private ArrayAdapter<Review> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("Food");
        this.reviews = food.getTextReviews();
        this.averageRating = food.getAverageRating();

        setContentView(R.layout.review_main);
        setTitleView(food);

        reviewList = (ListView) findViewById(R.id.reviewslist);

        Review space = new Review("", "", 0);
        reviews.add(space);

        Review test = new Review("David", "bloody diarrhea :(", 1);
        reviews.add(test);
        Review test1 = new Review("Jeffrey", "I would rather be strangled by Paul ", 1);
        reviews.add(test1);
        Review test2 = new Review("Hanna", "best meal all week!!", 5);
        reviews.add(test2);
        Review test3 = new Review("Paul", "I would rather be stranged by Jeffrey!!", 3);
        reviews.add(test3);


        populateListView();


    }

    private void populateListView(){
        adapter = new ReviewListAdapter();
        addFooter();
        reviewList.setAdapter(adapter);
    }

    private void updateListView(){
        adapter = new ReviewListAdapter();
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
            ratingView.setText(currReview.getStringRating());
            dateView.setText(currReview.getDate());
            reviewTextView.setText(currReview.getText());

            return v;
        }
    }

    /*
    * gets text after submit button pressed
    * return Review
    * checks entered atleast rating or review
    */

    public void submitReview(View v){
        String author, text, rating;


        final EditText editName = (EditText) findViewById(R.id.enterNameEditText);
        final EditText editReviewText = (EditText) findViewById(R.id.editReviewText);
        final Spinner ratingSpinner = (Spinner) findViewById(R.id.reviewRatingSpinner);

        author = editName.getText().toString();
        text = editReviewText.getText().toString();
        rating = ratingSpinner.getSelectedItem().toString();

        //verifies user entered at least rating or review
        if(rating.equals("None") && text.equals("")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Review Parameters");
            alertDialogBuilder
                    .setMessage("You must enter at least a rating or review to submit a rating")
                    .setCancelable(false)
                    .setNegativeButton("Yeah, feel bad about it",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }


        if(rating.equals("None"))
           rating = "0";  //TODO
        if(rating.equals("Death"))
            rating = "-10";

        int intRating = Integer.parseInt(rating); //thanks to stackOverFlow Rob Hruska

        Review newReview = new Review(author, text, intRating);
        reviews.add(newReview);
        updateListView();
        //TODO what else todo?

        //clears EditViews
        editName.setText("");
        editReviewText.setText("");
        ratingSpinner.setSelection(0);
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
