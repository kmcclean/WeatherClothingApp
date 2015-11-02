package com.example.kevin.weatherclothingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class WeatherClothingActivity extends ListActivity {

    ListView weatherList;



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

                //creates fragments that show the options for the dates which have been selected by the user.
                FragmentManager fm;
                fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                EtsyFragment ef = new EtsyFragment();
                ft.replace(android.R.id.content, ef);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


    }
}