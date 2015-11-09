package com.example.kevin.weatherclothingapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amazon.device.associates.AssociatesAPI;
import com.amazon.device.associates.LinkService;
import com.amazon.device.associates.NotInitializedException;
import com.amazon.device.associates.OpenSearchPageRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;
import java.util.ArrayList;

public class WeatherClothingActivity extends ListActivity{//} implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

    String latitude;
    String longitude;

    private LocationManager locationManager;
    Location location;


    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected static Location mLastLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    private String newCityName;
    private String newCityZMW;
    private boolean isNewCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);

        chosenStore = AMAZON;
        chosenCategory = "mens";
        chosenItems = "hats";
        a = this;

        //http://stackoverflow.com/questions/32715189/location-manager-remove-updates-permission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = Double.toString(location.getLatitude());
            longitude = Double.toString(location.getLongitude());
            Log.e(TAG, "Latitude: " + latitude);
            }

        APICreator apic = new APICreator(a);
        String APPLICATION_KEY = apic.getKeyFromRawResource("APPLICATION_KEY");
        AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, this));

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
                    // todo the category and search terms will change depending on what the user has chosen to search for. Categories available for the Amazon Mobile Associates API are found here https://developer.amazon.com/public/apis/earn/mobile-associates/docs/available-search-categories

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
            newCityZMW = data.getStringExtra(ChangeLocationActivity.EXTRA_NEW_LOCATION_ZMW);
            newCityName = data.getStringExtra(ChangeLocationActivity.EXTRA_NEW_LOCATION_CITYNAME);
            isNewCity = true;
            if (isNewCity) { // this if statement toggles whether forecast is pulled from user's own location, or the newly chosen location
                // todo Create new forecast based on this new data
            }
        }
    }

/*
    @Override
    public void onConnected(Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.

        buildGoogleApiClient();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e(TAG, mLastLocation.toString());
        if (mLastLocation != null) {

            latitude = Double.toString(mLastLocation.getLatitude());
            longitude = Double.toString(mLastLocation.getLongitude());
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

}

class MAAWebViewClient extends WebViewClient {

    private final String TAG = "MAAWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            LinkService linkService = AssociatesAPI.getLinkService();
            return linkService.overrideLinkInvocation(view, url);
        } catch (NotInitializedException e) {
            Log.v(TAG, "NotInitializedException error");
        }
        return false;
    }*/
}