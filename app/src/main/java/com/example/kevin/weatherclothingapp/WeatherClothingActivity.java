package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amazon.device.associates.AssociatesAPI;
import com.amazon.device.associates.LinkService;
import com.amazon.device.associates.NotInitializedException;
import com.amazon.device.associates.OpenSearchPageRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherClothingActivity extends ListActivity {

    private final String TAG = "WeatherClothingActivity";

    private final int AMAZON = 0;
    private final int ETSY = 1;

    public final String STORE = "store";
    public final String CATEGORY = "Category";
    public final String ITEM = "items";

    ListView weatherList;
    int chosenStore;
    String chosenCategory;
    String chosenItems;

    String amazonSearchTerm;
    String amazonCategory;
    String temperatureDescription;
    String weatherDescription;

    Activity a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);
        chosenStore = ETSY;
        chosenCategory = "mens";
        chosenItems = "hats";
        a = this;

        APICreator apic = new APICreator(a);

        JSONObject wunderGroundJSONData = apic.getWunderGroundJSONData();
        ArrayList<Bitmap> weatherIconArrayList = apic.getWeatherIconArrayList(wunderGroundJSONData);
        ArrayList<String> forecastDayArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "title");
        ArrayList<String> forecastConditionsArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "fcttext");
        final ArrayList<String> iconNameArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "icon");

        //sets the list Adapter.
        CustomListAdapter adapter = new CustomListAdapter(this, forecastDayArrayList, weatherIconArrayList, forecastConditionsArrayList);
        weatherList = (ListView) findViewById(android.R.id.list);
        weatherList.setAdapter(adapter);
        //TODO set the location fragment and display it.

        //TODO set the change clothing options fragment and display it.

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (chosenStore == AMAZON) {
                    if (chosenItems.equals("Shoes")) {
                        amazonCategory = "Shoes";
                    } else {
                        amazonCategory = "ClothingAndAccessories";
                    }

                    APICreator apic = new APICreator(a);

                    temperatureDescription = "freezing"; // this variable has four possibilities based on the temperature forecast from wunderground. <32 freezing, 32-50	cold, 50-75	warm, and >75 hot. Will need to create a @getTemperatureDescription method to convert wunderground into temperatureDescription
                    weatherDescription = apic.getWeatherDescription(iconNameArrayList.get(position)); // this variable has four possibilities based on the weatherIcon from the wunderground forecast: mild, rainy, snowy, and stormy. stormy and rainy are probably the same thing. Will need to create @getweatherDescription method to convert wunderground icon into correct  weatherDescription
                    String amazonString = apic.createAmazonSearchTerm(weatherDescription, temperatureDescription, chosenItems, chosenCategory);

                    // todo the category and search terms will change depending on what the user has chosen to search for. Categories available for the Amazon Mobile Associates API are found here https://developer.amazon.com/public/apis/earn/mobile-associates/docs/available-search-categories
                    //This is from https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
                    Toast t = Toast.makeText(WeatherClothingActivity.this, amazonString, Toast.LENGTH_LONG);
                    t.show();
                    OpenSearchPageRequest request = new OpenSearchPageRequest(amazonCategory, amazonString);
                    try {
                        LinkService linkService = AssociatesAPI.getLinkService();
                        linkService.openRetailPage(request);
                    } catch (NotInitializedException e) {
                        Log.v(TAG, "NotInitializedException error");
                    }
                }
                else if (chosenStore == ETSY){
                    Intent i = new Intent(WeatherClothingActivity.this, EtsyActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public void openSettings(MenuItem menuItem){
        Intent i = new Intent(WeatherClothingActivity.this, SettingsActivity.class);
        Bundle b = new Bundle();


        startActivityForResult(i, 0);
    }
}