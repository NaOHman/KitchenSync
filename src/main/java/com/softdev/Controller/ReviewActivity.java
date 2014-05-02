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
    private double averageRating;
    private ArrayAdapter<Review> adapter;
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
                    .setMessage("You must enter at least a rating or review")
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

        //puts 'none' and 'death' into numbers TODO
        if(rating.equals("None"))
           rating = "3";
        if(rating.equals("Death"))
            rating = "-10";

        //puts author to anonymous if none entered
        if(author.equals(""))
            author = "Anonymous Reviewer";

        int intRating;
        try{
            //string => int
            intRating = Integer.parseInt(rating); //thanks to stackOverFlow Rob Hruska
        }
        catch (IllegalStateException e){
            intRating = 0;
            Log.d("--------------------->","ill state exception caught");
        }


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
                JSONObject body = new JSONObject();
                String json = new Gson().toJson(review);
                body.put("id", food.getFoodId());
                body.put("review", json);
                Log.d("JSON",body.toString());
                post.setEntity(new StringEntity(body.toString()));
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                Log.d("Response",result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
