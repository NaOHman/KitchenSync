package com.softdev.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.softdev.Model.*;
import com.softdev.R;

import java.util.List;

public class MealListAdapter extends BaseExpandableListAdapter {

    Context context;
    Day day;
    List<Meal> meals;
    int tagID = 0;

    public MealListAdapter(Day day, Context context){
        this.context = context;
        this.day = day;
        Log.e("MealListAdapter", "Day == null?" + (day == null));
        meals = day.getRealMeals();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return meals.get(groupPosition).getDisplayItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final DisplayItem child = (DisplayItem) getChild(groupPosition,childPosition);
        String name = child.getName();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        ImageView img1 = (ImageView) convertView.findViewById(R.id.list_item_glutenImgview);
        ImageView img2 = (ImageView) convertView.findViewById(R.id.list_item_restrictionImgView);
        img1.setImageResource(R.drawable.backgroundlight);
        img2.setImageResource(R.drawable.backgroundlight);
        ImageView restrictImg = img1;
        if (child.getType() == Station.class) {
            //Todo Programatically make the stations look distinct
            txtListChild.setTypeface(null,Typeface.BOLD);
            txtListChild.setAllCaps(true);
            txtListChild.setPadding(0,0,0,0);
        } else {
            txtListChild.setTypeface(null, Typeface.NORMAL);
            txtListChild.setAllCaps(false);
            txtListChild.setPadding(5,0,0,0);
            Restriction r = ((Food) child).getRestriction();
            if (((Food) child).getGlutenFree()){
                img1.setImageResource(R.drawable.glutenfree);
                restrictImg = img2;
            }
            if(r == Restriction.VEGAN)
                restrictImg.setImageResource(R.drawable.vegan);
            if(r == Restriction.VEGETARIAN)
                restrictImg.setImageResource(R.drawable.vegetarian);
            if(r == Restriction.PESCETARIAN)
                restrictImg.setImageResource(R.drawable.pescetarianicon);
            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReviewActivity.class);
                    Food food = (Food) child;
                    intent.putExtra("Food", food);
                    context.startActivity(intent);
                }
            });
        }
        txtListChild.setText(name);
        txtListChild.setTag(name);
        tagID++;
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (meals.size() == 0)
            return 0;
        return meals.get(groupPosition).getDisplayItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(meals.size() == 0)
            return "No Meals Published";
        return meals.get(groupPosition).toString();
    }

    @Override
    public int getGroupCount() {
        if (meals.size() == 0)
            return 1;
        return meals.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_heading, null);
        }
        TextView mealHeader = (TextView) convertView.findViewById(R.id.lblMealHeader);
        mealHeader.setTypeface(null, Typeface.BOLD);
        mealHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
