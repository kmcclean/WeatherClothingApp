package com.example.kevin.weatherclothingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
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

        ArrayList<Bitmap> weatherIconArrayList = apic.getForecastIcon();
        ArrayList<String> forecastDayArrayList = apic.getForecastStringInfo("title");
        ArrayList<String> forecastConditionsArrayList = apic.getForecastStringInfo("fcttext");

        //sets the list Adapter.
        CustomListAdapter adapter = new CustomListAdapter(this, forecastDayArrayList, weatherIconArrayList, forecastConditionsArrayList);
        weatherList = (ListView) findViewById(android.R.id.list);
        weatherList.setAdapter(adapter);
        //TODO set the location fragment and display it.

        //TODO set the change clothing options fragment and display it.

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(WeatherClothingActivity.this, EtsyActivity.class);
                startActivity(i);
            }
        });
    }
}