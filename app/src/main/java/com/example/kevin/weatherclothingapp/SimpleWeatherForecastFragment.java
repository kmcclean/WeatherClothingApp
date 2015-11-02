package com.example.kevin.weatherclothingapp;

import android.app.ListFragment;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 10/26/2015.
 */
public class SimpleWeatherForecastFragment extends ListFragment {


    ImageView mWeatherIcon;
    TextView mTemperature;
    TextView mDate;
    ArrayList<String> mDays = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO this fragment should make a request of the APICreator to the data needed for the weather data.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_weather_forecast_fragment, container, false);
        mDays.add("This is test 1.");
        mDays.add("This is test 2.");
        mDays.add("This is test 3.");
        mDays.add("This is test 4.");
        mDays.add("This is test 5.");
        mWeatherIcon = (ImageView)v.findViewById(R.id.weather_img);
        mTemperature = (TextView)v.findViewById(R.id.temp);
        mDate = (TextView)v.findViewById(R.id.date);
        ArrayAdapter mDaysAdapter = new ArrayAdapter(inflater.getContext(), android.R.layout.simple_list_item_single_choice, mDays);
        setListAdapter(mDaysAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
