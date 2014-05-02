package com.softdev.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.softdev.Model.Food;
import com.softdev.Model.Review;
import com.softdev.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.widget.Button;
import android.widget.EditText;;

/**
 * Created by dgoldste on 4/22/14.
 * with the help of the excellent ListView tutorial https://www.youtube.com/watch?v=WRANgDgM2Zg
 */

public class ReviewActivity extends Activity {
    private List<Review> reviews = new ArrayList<Review>();
    private TextView mealNameTxtView;
    private ListView reviewList;
    private EditText editName, editReviewText;
    private Spinner ratingSpinner;
    private double averageRating;
    private Food food;
    private Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("Food");
        this.reviews = food.getTextReviews();
        this.averageRating = food.getAverageRating();

        setContentView(R.layout.review_main);
        setTitleView(food);
        reviewList = (ListView) findViewById(R.id.reviewslist);

        //TODO add space review so reviews != null
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
            ratingView.setText(currReview.getStringRating());
            dateView.setText(currReview.getDate());
            reviewTextView.setText(currReview.getText());

            return v;
        }
    }

    /*
    * gets text after submit button pressed and returns review
    * @
    */
    private void submitReview(View v){
        Log.d("------------------------>", "got here");


    }

    private void addFooter(){
        View footerView = View.inflate(this, R.layout.review_write, null);
        footerView.findViewById(R.id.submitReviewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushReview(new Review("Jeffrey","Testing 1...2...3",4));
            }
        });
        reviewList.addFooterView(footerView);
    }

    private void setTitleView(Food food){
        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
    }

    public void pushReview(Review review){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new ReviewPusher().execute(review);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Not Connected to the Internet");
            alertDialogBuilder
                    .setMessage("Please connect to the internet so we can fetch today's menu")
                    .setCancelable(false)
                    .setNegativeButton("Okay",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private class ReviewPusher extends AsyncTask<Review, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), " Posting Review ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(final Review... args) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(getString(R.string.server_post));
            Review review = args[0];
            // check for success tag
            try {
                JSONArray body = new JSONArray();
                String id = new JSONObject().put("id", food.getFoodId()).toString();
                String json = new Gson().toJson(review);
                body.put(id);
                body.put(json);
                post.setEntity(new StringEntity(body.toString()));
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
