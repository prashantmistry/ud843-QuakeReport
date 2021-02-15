package com.example.android.quakereport;

public class WordList {
    private double mMagnitude;
    private String mLocation;
    private String mDate;
    private String mUrl;

    public WordList(double magnitude, String location, String day, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mDate = day;
        mUrl = url;
    }

    public double getmMagnitude(){return mMagnitude;}

    public String getmLocation(){return mLocation;}

    public String getmDate(){return mDate;}

    public String getmUrl(){return mUrl;}
}
