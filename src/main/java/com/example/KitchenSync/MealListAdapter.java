package com.example.KitchenSync;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MealListAdapter extends BaseExpandableListAdapter {

    Context context;
    Day day;

    public MealListAdapter(Day day, Context context){
        this.context = context;
        this.day = day;
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
        CustExpListview SecondLevelexplv = new CustExpListview(context);
        SecondLevelexplv.setAdapter(new StationListAdapter(context, getMeal(groupPosition)));
        SecondLevelexplv.setGroupIndicator(null);
        return SecondLevelexplv;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (day.getLunch() == null)
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (day.getLunch() == null)
            return "No Meal Published";
        if (groupPosition == 0){
            return "Lunch";
        } else if (groupPosition == 1){
            return "Dinner";
        }
        return "";
    }

    public Meal getMeal(int groupPosition){
        if (groupPosition == 0){
            return day.getLunch();
        } else if (groupPosition == 1){
            return day.getDinner();
        }
        return null;
    }

    @Override
    public int getGroupCount() {
        if (day.getLunch() != null)
            return 2;
        return 1;
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