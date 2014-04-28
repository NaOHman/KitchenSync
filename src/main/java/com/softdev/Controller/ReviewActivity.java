package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.softdev.Model.Food;
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
    private ListView reviewsList;
    private List<Review> reviews;
    private ReviewListAdapter reviewAdapter;
    private TextView mealNameTxtView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("Food");
        Log.d("Caught FOOD in Review Page" , food.getName());

        setContentView(R.layout.review_main);
        reviewsList = (ListView) findViewById(R.id.reviewslist);
        reviewAdapter = new ReviewListAdapter(reviews, this);
//TODO        reviewsList.setAdapter(reviewAdapter);

        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
        reviewsList= (ListView) findViewById(R.id.reviewslist);

        //TODO reviewsList.addFooterView(R.layout.review_write);

    }

}
