package com.softdev.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.softdev.Model.Review;
import com.softdev.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dgoldste on 4/22/14.
 */

public class ReviewActivity extends Activity {
    private ListView reviewsList;
    private List<Review> reviews = new ArrayList<Review>();
    private SimpleAdapter reviewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_main);
        reviewsList= (ListView) findViewById(R.id.reviewslist);










        //TODO reviewsList.addFooterView(R.layout.review_write);

    }

    private void populateReviews(){
        //TODO
    }
}
