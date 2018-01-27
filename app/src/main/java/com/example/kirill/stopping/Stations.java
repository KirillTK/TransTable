package com.example.kirill.stopping;


public class Stations {
    private String stations;
    private int position;
    private boolean isfavourite;
    private String busnamber;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Stations(String stations,int position,String busnamber){
        this.stations = stations;
        this.isfavourite = false;
        this.busnamber = busnamber;
        this.position = position;
    }

    public String getBusnamber() {
        return busnamber;
    }

    public void setBusnamber(String busnamber) {
        this.busnamber = busnamber;
    }

    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public boolean isfavourite() {
        return isfavourite;
    }

    public void setIsfavourite(boolean isfavourite) {
        this.isfavourite = isfavourite;
    }
}
