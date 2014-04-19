package com.softdev.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.softdev.Model.Food;
import com.softdev.Model.Meal;
import com.softdev.Model.Station;
import com.softdev.R;





/**
 * Created by jeffrey on 2/25/14.
 */
public class StationListAdapter extends BaseExpandableListAdapter {
    private Meal meal;
    private Context context;


    public StationListAdapter(Context context, Meal meal){
        this.meal = meal;
        this.context = context;
    }
    @Override
    public int getGroupCount() {
        return meal.getStations().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return meal.getStations().get(groupPosition).getFoodNames().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return meal.getStationHeaders().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Station station = meal.getStations().get(groupPosition);
        return station.getFoodNames().get(childPosition);
    }
    
    private Food getChildObject(int groupPosition, int childPosition){
        Station station = meal.getStations().get(groupPosition);
        return station.getFoods().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblStationHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null){

         /**    TODO: ImageViews returns nullPointed b/c convertView meant for lists
            ImageView restrictionImgView = (ImageView) convertView.findViewById(R.id.mealimageviewrestriction);
            ImageView glutenImgView = (ImageView) convertView.findViewById(R.id.mealimageviewgluten);

            Food food = getChildObject(groupPosition, childPosition);
            String restriction = food.getRestriction().toString();
            if(restriction == "VEGAN") restrictionImgView.setImageResource(R.drawable.veganicon);
            if(restriction == "VEGETARIAN") restrictionImgView.setImageResource(R.drawable.vegetarianicon);
            if(restriction == "PESCETARIAN") restrictionImgView.setImageResource(R.drawable.pescetarianicon);
            if(food.getMadeWithoutGluten()) glutenImgView.setImageResource(R.drawable.noglutenicon);
          */
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
