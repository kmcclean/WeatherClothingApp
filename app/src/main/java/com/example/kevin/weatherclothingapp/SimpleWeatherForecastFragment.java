package com.example.kevin.weatherclothingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin on 10/26/2015.
 */
public class SimpleWeatherForecastFragment extends Fragment {


    ImageView mWeatherIcon;
    TextView mTemperature;
    TextView mDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO this fragment should make a request of the APICreator to the data needed for the weather data.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_weather_forecast_fragment, container, false);
        mWeatherIcon = (ImageView)v.findViewById(R.id.weather_img);
        mTemperature = (TextView)v.findViewById(R.id.temp);
        mDate = (TextView)v.findViewById(R.id.date);
        return v;
    }
}
