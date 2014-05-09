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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.softdev.Model.Food;
import com.softdev.Model.Review;
import com.softdev.Model.Week;
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
 * Displays details about a food and allows users to review and rate the food.
 */

public class ReviewActivity extends Activity {
    private List<Review> reviews = new ArrayList<Review>();
    private TextView mealNameTxtView, averageRatingTextView;
    private ListView reviewList;
    private ArrayAdapter<Review> adapter;
    private Food food;
    private EditText editName, editReviewText;
    private RatingBar reviewRatingBar;
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long foodId = intent.getLongExtra("FoodID", -1L);
        food = Week.getInstance().getFoodById(foodId);
        setContentView(R.layout.review_main);
        mealNameTxtView = (TextView) findViewById(R.id.reviewHeader_foodName);
        mealNameTxtView.setText(food.getName());
        adjustFoodNameSize();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editName = (EditText) findViewById(R.id.enterNameEditText);
        editReviewText = (EditText) findViewById(R.id.editReviewText);
        reviewList = (ListView) findViewById(R.id.reviewslist);
        averageRatingTextView = (TextView) findViewById(R.id.AverageUserRatingTextView);
        reviewRatingBar = (RatingBar) findViewById(R.id.reviewRatingBar);

        updateListView();

    }

    private void updateListView(){
        adapter = new ReviewListAdapter(food.getTextReviews());
        reviewList.setAdapter(adapter);
        Double averageRating = food.getAverageRating();
        if(averageRating == 0){
            averageRatingTextView.setText("Average user rating: Not yet reviewed");
            return;
        }
        String ratings = new DecimalFormat("#.#").format(averageRating);
        averageRatingTextView.setText("Average rating: " + ratings + " / 5");
    }

    /**
     * makes sure the food name doesn't take up too much space on the screen
     */
    private void adjustFoodNameSize(){
        int nameLength = mealNameTxtView.length();
        if(nameLength > 25 && nameLength < 35)
            mealNameTxtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.reviewTitle_TextSizeMedium));
        if(nameLength > 35)
            mealNameTxtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.reviewTitle_TextSizeSmall));
    }

    private class ReviewListAdapter extends ArrayAdapter<Review> {
        List<Review> reviews;
        public ReviewListAdapter(List<Review> reviews){
            super(ReviewActivity.this, R.layout.review_body, reviews);
            this.reviews = reviews;
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
    * checks entered at least rating or review
    */

    public void submitReview(View v){
        String author, text;
        author = editName.getText().toString();
        text = editReviewText.getText().toString();

        int submittedRating = Math.round(reviewRatingBar.getRating());


        //verifies user entered at least rating or review
        if(submittedRating == 0 && text.replaceAll("\\s","").equals("")){
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
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        pushReview(newReview);
    }

    /**
     * attempts to push a review to the server, handles connectivity errors
     * @param review the review to be pushed
     */
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
    public void serverError(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Menu out of sync with the database");
        alertDialogBuilder
                .setMessage("Would you like to refresh or continue?")
                .setCancelable(false)
                .setNegativeButton("Continue",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Refresh",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * a threaded task that attempts to post the review to the server
     */
    private class ReviewPusher extends AsyncTask<Review, Void, ServerResponse> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), " Posting Review ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(ServerResponse response) {
            if (response.success){
                editReviewText.setText("");
                editName.setText("");
                food.getReviews().add(response.review);
                updateListView();
            }
            Toast.makeText(getApplicationContext(),response.error, Toast.LENGTH_SHORT).show();
            if (!response.success && response.error.equals("Food does not exist in database")){
                serverError();
            }
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
                Log.d("----->Food ID", food.getFoodId() +"");
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

    /**
     * basically a 3-tuple
     */
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
