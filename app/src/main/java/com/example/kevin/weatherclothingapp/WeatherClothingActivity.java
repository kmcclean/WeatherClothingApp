package com.example.kevin.weatherclothingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeatherClothingActivity extends ListActivity {

    ListView weatherList;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);

        APICreator apic = new APICreator(this);
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        ArrayList<String> dayList = new ArrayList<>();

        //This runs for the even numbers, because that's how WeatherUnderground sets their days (odd numbers are nights).
        for (int i = 0; i <= 6; i = i + 2) {
            Bitmap weatherIcon = apic.getForecastIcon(i);
            String day = apic.getForecastDay(i);
            bitmapArrayList.add(weatherIcon);
            dayList.add(day);
        }


        //sets the list Adapter.
        CustomListAdapter adapter = new CustomListAdapter(this, dayList, bitmapArrayList);
        weatherList = (ListView) findViewById(android.R.id.list);
        weatherList.setAdapter(adapter);
        //TODO set the location fragment and display it.

        //TODO set the change clothing options fragment and display it.

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


    }
}