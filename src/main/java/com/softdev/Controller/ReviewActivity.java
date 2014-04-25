package com.softdev.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import com.softdev.R;

/**
 * Created by dgoldste on 4/22/14.
 */

public class ReviewActivity extends Activity {
    private ExpandableListView reviewsExpList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_main);
//TODO        reviewsExpList = (ExpandableListView) findViewById(R.id.reviewsExpandable);

    }
}
