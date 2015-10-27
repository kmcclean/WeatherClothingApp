package com.example.kevin.weatherclothingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherClothingActivity extends AppCompatActivity {

    private static final String TAG = "Weather Application";
    TextView tempTV;
    ImageView radarImage;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);

        //TODO set the location fragment and display it.

        //TODO set the change clothing options fragment and display it.

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        SimpleWeatherForecastFragment swff = new SimpleWeatherForecastFragment();
        //TODO swff should have a number pushed to it, and that should tell the program which day of the week...
        //TODO ...that fragment should present. It should be a loop that allows it to create the appropriate number of fragments.

    }
}
