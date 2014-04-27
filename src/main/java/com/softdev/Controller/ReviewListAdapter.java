package com.softdev.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.softdev.Model.Review;
import com.softdev.R;

import java.util.List;
import java.util.Set;

/**
 * Created by dgoldste on 4/25/14.
 * adapted from https://github.com/survivingwithandroid/Surviving-with-android/blob/master/SimpleList/src/com/survivingwithandroid/listview/SimpleList/PlanetAdapter.java
 */
public class ReviewListAdapter extends ArrayAdapter<Review>{
    private List<Review> reviewList;
    private Context context;

    public ReviewListAdapter(List<Review> reviewList, Context context){
        super(context, R.layout.review_body, reviewList);
        this.reviewList = reviewList;
        this.context = context;
    }

    /*
    public int getCount(){
        return reviewList.size();
    }
    */

    public Review getItem(int position){
        return reviewList.get(position);
    }

    public long getItemId(int position){
        return reviewList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        ReviewHolder holder = new ReviewHolder();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.review_body, null);

            TextView authorView = (TextView) v.findViewById(R.id.reviewTitleAuthor);
            TextView ratingView = (TextView) v.findViewById(R.id.reviewTitleRating);
            TextView dateView = (TextView) v.findViewById(R.id.reviewTitleDate);
            TextView textView = (TextView) v.findViewById(R.id.reviewText);

            holder.reviewAuthor = authorView;
            holder.reviewRating = ratingView;
            holder.reviewDate = dateView;
            holder.reviewText = textView;

            v.setTag(holder);
        }

        else holder = (ReviewHolder) v.getTag();


        Review review = reviewList.get(position);
        holder.reviewAuthor.setText(review.getText());
        holder.reviewRating.setText("" + review.getRating());
        holder.reviewDate.setText("" + review.getDate());
        holder.reviewText.setText(review.getText());

        return v;
    }

    private static class ReviewHolder {
        public TextView reviewAuthor;
        public TextView reviewRating;
        public TextView reviewDate;
        public TextView reviewText;
    }

}
