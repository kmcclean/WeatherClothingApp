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

public class WeatherClothingActivity extends ListActivity{

    private final String TAG = "WeatherClothingActivity";
    private final String BUNDLE = "com.example.kevin.weatherclothingapp";

    public final int AMAZON = 0;
    public final int ETSY = 1;
    public final String STORE = "store";
    public final String CATEGORY = "category";
    public final String ITEM = "item";

    public final int SETTINGS = 0;
    public final int LOCATION = 1;

    ListView weatherList;
    int chosenStore;
    String chosenCategory;
    String chosenItems;

    String temperatureDescription;
    String weatherDescription;

    Activity a;

    ArrayList<String> temperatureDescriptionList = new ArrayList<>();
    ArrayList<String> iconNameArrayList = new ArrayList<>();

    double latitude;
    double longitude;

    private boolean isNewCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);
        latitude = 44.9;
        longitude = -93.2;
        chosenStore = AMAZON;
        chosenCategory = "mens";
        chosenItems = "hats";
        a = this;


        APICreator apic = new APICreator(a);
        String APPLICATION_KEY = apic.getKeyFromRawResource("APPLICATION_KEY");
        AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, this));

        //this is the data used to set the create the main page for our app.
        JSONObject wunderGroundJSONData = apic.getWunderGroundJSONData(latitude, longitude);
        ArrayList<Bitmap> weatherIconArrayList = apic.getWeatherIconArrayList(wunderGroundJSONData);
        ArrayList<String> forecastDayArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "title");
        ArrayList<String> forecastConditionsArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "fcttext");

        iconNameArrayList = apic.getWunderGroundStringInfo(wunderGroundJSONData, "icon");
        temperatureDescriptionList = apic.getTemperatureDescription(wunderGroundJSONData);

        //sets the list Adapter.
        CustomListAdapter adapter = new CustomListAdapter(this, forecastDayArrayList, weatherIconArrayList, forecastConditionsArrayList, temperatureDescriptionList);
        weatherList = (ListView) findViewById(android.R.id.list);
        weatherList.setAdapter(adapter);
        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (chosenStore == AMAZON) {
                    if (chosenItems.equals("Shoes")) {
                        chosenCategory = chosenCategory + " Shoes";
                    } else {
                        chosenCategory = chosenCategory + " Clothing And Accessories";
                    }

                    APICreator apic = new APICreator(a);

                    temperatureDescription = temperatureDescriptionList.get(position); // this variable has four possibilities based on the temperature forecast from wunderground. <32 freezing, 32-50	cold, 50-75	warm, and >75 hot. Will need to create a @getTemperatureDescription method to convert wunderground into temperatureDescription
                    weatherDescription = apic.getWeatherDescription(iconNameArrayList.get(position)); // this variable has four possibilities based on the weatherIcon from the wunderground forecast: mild, rainy, snowy, and stormy. stormy and rainy are probably the same thing. Will need to create @getweatherDescription method to convert wunderground icon into correct  weatherDescription
                    String amazonString = apic.createAmazonSearchTerm(weatherDescription, temperatureDescription, chosenItems, chosenCategory);
                    Log.e(TAG, amazonString);

                    //This is from https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
                    Toast t = Toast.makeText(WeatherClothingActivity.this, amazonString, Toast.LENGTH_LONG);
                    t.show();
                    OpenSearchPageRequest request = new OpenSearchPageRequest(chosenCategory, amazonString);
                    try {
                        LinkService linkService = AssociatesAPI.getLinkService();
                        linkService.openRetailPage(request);
                    } catch (NotInitializedException e) {
                        Log.v(TAG, "NotInitializedException error");
                    }
                }
                else if (chosenStore == ETSY){
                    Intent i = new Intent(WeatherClothingActivity.this, EtsyActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(CATEGORY, chosenCategory);
                    b.putSerializable(ITEM, chosenItems);
                    i.putExtra(BUNDLE, b);
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
        startActivityForResult(i, SETTINGS);

    }

    public void openChangeLocation(MenuItem menuItem){
        Intent launchChangeLocationActivity = new Intent(WeatherClothingActivity.this, ChangeLocationActivity.class);
        // this means we expect some result to be returned to the activity
        startActivityForResult(launchChangeLocationActivity, LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS && resultCode == RESULT_OK){
            String store = data.getStringExtra(STORE);
            if (store.equals("Etsy")){
                chosenStore = ETSY;
            }
            else if (store.equals("Amazon")){
                chosenStore = AMAZON;
            }

            chosenCategory = data.getStringExtra(CATEGORY);
            chosenItems = data.getStringExtra(ITEM);
        }
        else if (requestCode == LOCATION && resultCode == RESULT_OK) {
            // get data from extra needed to change city
            isNewCity = true;
            if (isNewCity) { // this if statement toggles whether forecast is pulled from user's own location, or the newly chosen location
                // todo Create new forecast based on this new data
            }
        }
    }
}