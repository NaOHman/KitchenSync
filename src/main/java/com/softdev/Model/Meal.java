package com.softdev.Model;

import java.util.ArrayList;

/**
 * Created by jeffrey on 2/11/14.
 * a Meal represents all a meal in cafe mac, it's broken up into different stations
 */

public class Meal {
    private ArrayList <Station> stations;
    public Meal(){
        stations = new ArrayList<Station>();
    }

    public void addStation(Station station){
        this.stations.add(station);
    }

    /**
     * @return a list of all the stations in a given meal
     */
    public ArrayList<Station> getStations(){
        return stations;
    }

    /**
     * @param stations list of stations to replace the current list of stations
     */
    public void setStations(ArrayList<Station> stations){
        this.stations = stations;
    }

    public ArrayList<String> getStationHeaders(){
        ArrayList<String> stationHeaders = new ArrayList<String>();
        for (Station station : stations){
            stationHeaders.add(station.getName());
        }
        return stationHeaders;
    }
}
