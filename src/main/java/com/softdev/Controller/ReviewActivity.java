package com.softdev.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldste on 4/22/14.
 * with the help of the excellent ListView tutorial https://www.youtube.com/watch?v=WRANgDgM2Zg
 */

public class ReviewActivity extends Activity {
    private List<Review> reviews = new ArrayList<Review>();
    private TextView mealNameTxtView, averageRatingTextView;
    private ListView reviewList;
    private ArrayAdapter<Review> adapter;
    private Food food;
    private EditText editName, editReviewText;
    private RatingBar reviewRatingBar;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("Food");
        setContentView(R.layout.review_main);
        setTitleView(food);

        editName = (EditText) findViewById(R.id.enterNameEditText);
        editReviewText = (EditText) findViewById(R.id.editReviewText);
        reviewList = (ListView) findViewById(R.id.reviewslist);
        averageRatingTextView = (TextView) findViewById(R.id.AverageUserRatingTextView);
        reviewRatingBar = (RatingBar) findViewById(R.id.reviewRatingBar);

        updateListView();

    }

    private void updateListView(){
        reviews = food.getTextReviews();
        adapter = new ReviewListAdapter();
        reviewList.setAdapter(adapter);
        Double averageRating = food.getAverageRating();
        if(averageRating == 0){
            averageRatingTextView.setText("Average user rating: Not yet reviewed");
            return;
        }
        String ratings = new DecimalFormat("#.#").format(averageRating);
        averageRatingTextView.setText("Average rating: " + ratings + " / 5");
    }

    private void adjustFoodNameSize(){
        int nameLength = mealNameTxtView.length();
        if(nameLength > 25 && nameLength < 35)
            mealNameTxtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.reviewTitle_TextSizeMedium));
        if(nameLength > 35)
            mealNameTxtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.reviewTitle_TextSizeSmall));
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
        String author, text;
        author = editName.getText().toString();
        text = editReviewText.getText().toString();

        float currRating;
        currRating = reviewRatingBar.getRating();
        int submittedRating = (int) Math.round(currRating);


        //verifies user entered at least rating or review
        if(currRating == 0 && text.replaceAll("\\s","").equals("")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Review Parameters");
            alertDialogBuilder
                    .setMessage("You must enter at least a rating or review to submit")
                    .setCancelable(false)
                    .setNegativeButton("Okay",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }
        if (author.replaceAll("\\s","").equals(""))
            author = "Anonymous";
        Review newReview = new Review(author, text, submittedRating);
        pushReview(newReview);
    }

    public void clearInputFields(){
        editName.setText("");
        editReviewText.setText("");
    }

    private void setTitleView(Food food){
        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
        adjustFoodNameSize();
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

    private class ReviewPusher extends AsyncTask<Review, Void, ServerResponse> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), " Posting Review ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(ServerResponse response) {
            if (response.success){
                clearInputFields();
                food.getReviews().add(response.review);
                updateListView();
            }
            Toast.makeText(getApplicationContext(),response.error, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ServerResponse doInBackground(final Review... args) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(getString(R.string.server_post));
            Review review = args[0];
            // check for success tag
            try {
                JSONObject body = new JSONObject();
                String json = new Gson().toJson(review);
                body.put("id", food.getFoodId());
                body.put("review", json);
                Log.d("JSON",body.toString());
                post.setEntity(new StringEntity(body.toString()));
                HttpResponse response = client.execute(post);
                String responseString = EntityUtils.toString(response.getEntity());
                Log.d("Server Response", responseString);
                JSONObject result = new JSONObject(responseString);
                boolean success = result.getBoolean("success");
                String error;
                if (success) {
                    error = "Review Posted";
                } else {
                    error = result.getString("error");
                    Log.d("Connection error", error);
                }
                return(new ServerResponse(review, success, error));
            } catch (Exception e) {;
                e.printStackTrace();
                return new ServerResponse(null, false, "Error connecting to server");
            }
        }

    }
    private class ServerResponse{
        public Review review;
        public boolean success;
        public String error;

        private ServerResponse(Review review, boolean success, String error) {
            this.review = review;
            this.success = success;
            this.error = error;
        }
    }
}
