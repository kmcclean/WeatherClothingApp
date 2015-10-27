package com.example.kevin.weatherclothingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Kevin on 10/26/2015.
 */
public class LocationFragment extends Fragment{

    TextView mCurrentCityTextView;
    Button mChangeCityButton;
    EditText mUserCityEditText;

    //TODO add the ability to change cities. This should use the GeoCacher to find the new location...
    //TODO ...and close this fragment, returning to the first one and setting the new city with its new forecasts.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_location_fragment, container, false);
        mCurrentCityTextView = (TextView)v.findViewById(R.id.currentCityTextView);
        mChangeCityButton = (Button)v.findViewById(R.id.changeCityButton);
        mUserCityEditText = (EditText)v.findViewById(R.id.newCityEditText);
        return v;
    }
}
