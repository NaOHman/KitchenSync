package com.softdev.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.softdev.Model.Day;
import com.softdev.Model.Meal;
import com.softdev.R;

import java.util.List;

public class MealListAdapter extends BaseExpandableListAdapter {

    Context context;
    Day day;
    List<Meal> meals;

    public MealListAdapter(Day day, Context context){
        this.context = context;
        this.day = day;
        meals = day.getRealMeals();
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        return arg1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        CustExpListview secondLevelexplv = new CustExpListview(context);
        StationListAdapter adapter = new StationListAdapter(context, getMeal(groupPosition));
        secondLevelexplv.setAdapter(adapter);
        secondLevelexplv.setGroupIndicator(null);
        for (int i=0; i < adapter.getGroupCount(); i++){
            secondLevelexplv.expandGroup(i);
        }
        return secondLevelexplv;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (meals.size() == 0)
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(meals.size() == 0)
            return "No Meals Published";
        return meals.get(groupPosition).toString();
    }

    public Meal getMeal(int groupPosition){
        return meals.get(groupPosition);
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
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}