package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<WordList> {
    //Creating Constructor for the class
    public WordAdapter (Activity context, ArrayList<WordList> earthquakeList){
        super(context,0,earthquakeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if exisiting view is being reused else inflate the view
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }

        WordList currentWord = getItem(position);

        //Setting Magnitude text
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(currentWord.getmMagnitude());
        TextView magnitudeText = listItemView.findViewById(R.id.magnitude_txt);
        magnitudeText.setText(output);

        //Setting background color for magnitude depending upon the severity
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeText.getBackground();
        int magnitudeColor = getMagnitudeColor(currentWord.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        //splitting location in 2 parts
        String[] locationArray = locSplitter(currentWord.getmLocation());

        //Setting Direction text
        TextView directionText = listItemView.findViewById(R.id.direction_txt);
        directionText.setText(locationArray[0]);

        //Setting Location text
        TextView locationText = listItemView.findViewById(R.id.location_txt);
        locationText.setText(locationArray[1]);

        //Setting day and date
        TextView dayText = listItemView.findViewById(R.id.day_txt);
        dayText.setText(currentWord.getmDate());

        return listItemView;
    }

    private String[] locSplitter(String location){
        String offset,primary;
        if(location.contains("of")){
            int splitindex = location.indexOf("of") + 2;
            offset = location.substring(0,splitindex);
            primary = location.substring(splitindex + 1);
        } else{
            offset = "Near the";
            primary = location;
        }
        return new String[]{offset, primary};
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorRes = 0;
        switch ((int) magnitude){
            case 0:
            case 1:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude1);
                break;
            case 2:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude2);
                break;
            case 3:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude3);
                break;
            case 4:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude4);
                break;
            case 5:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude5);
                break;
            case 6:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude6);
                break;
            case 7:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude7);
                break;
            case 8:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude8);
                break;
            case 9:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude9);
                break;
            case 10:
                magnitudeColorRes = ContextCompat.getColor(getContext(),R.color.magnitude10plus);
                break;

        }
        return magnitudeColorRes;
    }

}
