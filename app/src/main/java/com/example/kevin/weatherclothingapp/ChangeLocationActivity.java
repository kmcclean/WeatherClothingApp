package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedInputStream;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.HashMap;;

import org.json.JSONObject;

/**
 * Created by Kevin on 11/8/2015.
 */
public class ChangeLocationActivity extends Activity  {

    // This section of the app uses Wunderground's Autocomplete API http://www.wunderground.com/weather/api/d/docs?d=autocomplete-api&MR=1 to let the user choose a specific city from an autogenerated list based on typing into an EditText. If you type in "San Francisco" you'll get a dozen results from all over the world, so this lets you choose the one you want.
    private static final String TAG = "ChangeLocationActivity";
    public static final String EXTRA_NEW_LOCATION_ZMW = "com/example/kevin/weatherclothingappchangelocationactivity.newLocationZMW";
    public static final String EXTRA_NEW_LOCATION_CITYNAME = "com/example/kevin/weatherclothingappchangelocationactivity.newLocationCityName";
    private TextView mEnterNewLocationTextView;
    private Button mGetCityListButton;
    private Button mChooseCityButton;
    private HashMap<String,String> cityList = new HashMap<String,String>();
    private String newCityName;
    private String newCityZMW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        // survey question
        mEnterNewLocationTextView = (TextView) findViewById(R.id.enter_new_location_text);

        // This button accepts the user's entry of a new city to search for, and queries wunderground's Autocomplete API to get a list of possible choices. This creates a display of an ArrayList of the City names pulled from that json report.
        Button mGetCityListButton = (Button) findViewById(R.id.clickbutton_new_city_list);
        mGetCityListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enterNewCity = (EditText) findViewById(R.id.string_new_city);
                String newCity = enterNewCity.getText().toString();
                //  query Weather Underground Autocomplete API and get list of potential cities as json data, then pull city names and WU's "ZMW" ID for each from that list and put them into a new HashMap
                String newCityTempUrl = String.format("http://autocomplete.wunderground.com/aq?query=%s", newCity);
                try {
                    cityList = new RequestNewCityList().execute(newCityTempUrl).get();
                } catch (Exception e) {
                    Log.e(TAG, "Unable to get String. Check getCityList method.", e);
                }
                if (cityList == null) {
                    Log.e(TAG, "cityList variable in getCityList method is null.");
                }
            }
        });

        // create a Spinner from the list in the HashMap so that the user can choose one city. That spinner is tied to Button mChooseCityButton, below. Code modified from http://stackoverflow.com/questions/28120588/populating-spinner-using-a-hashmap and from Wei-Meng Lee textbook p. 199, and http://stackoverflow.com/questions/9061689/how-to-sort-hashmap-as-added-in-android-with-arrayadapter
        Spinner newCitySpinner = (Spinner) findViewById(R.id.spinner_new_city_list);

        ArrayList<String> cityArray = new ArrayList<String>();
        for (String key : cityList.keySet()) {
            cityArray.add(key.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, cityArray);
        newCitySpinner.setAdapter(adapter);
        newCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                newCityName = arg0.getItemAtPosition(index).toString();
                newCityZMW = cityList.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // This button accepts the user's choice of city from newCitySpinner. When button is clicked, it will return Weather Underground's ZMW variable and the name of the city to main activity, which will then pull a new weather forecast for that location.
        Button mChooseCityButton = (Button) findViewById(R.id.clickbutton_new_city_chooser);
        mChooseCityButton.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     // create Intent that will relaunch the 'calling' activity
                                                     Intent resultIntent = new Intent();
                                                     // put any extras that should be sent back to this activity - in this case the trio of new texts. See Wei-Ming 67
                                                     resultIntent.putExtra(EXTRA_NEW_LOCATION_ZMW, newCityZMW);
                                                     resultIntent.putExtra(EXTRA_NEW_LOCATION_CITYNAME, newCityName);
                                                     // set the result - can be RESULT_OK or RESULT_CANCELED.
                                                     setResult(RESULT_OK, resultIntent);
                                                     // close this activity. Android system will then relaunch calling activity.
                                                     finish();
                                                 }
                                             }
        );
    }


    //This class goes to WeatherUnderground and gets a list of cities from its Autocomplete API.
    class RequestNewCityList extends AsyncTask<String, String, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... urlInfo) {
            String responseString;
            String day = null;

            try {
                URL url = new URL(urlInfo[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();
                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }
                responseString = buffer.toString();
                JSONObject response = new JSONObject(responseString);
                JSONArray jsonResults = response.getJSONArray("RESULTS");
                // modified from Stack Overflow http://stackoverflow.com/questions/3408985/json-array-iteration-in-android-java
                String jsonZMW;
                String jsonCityName;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject row = jsonResults.getJSONObject(i);
                    jsonZMW = row.getString("zmw");
                    jsonCityName = row.getString("name");
                    cityList.put(jsonCityName,jsonZMW);
                }
            } catch (Exception e) {
                Log.e(null, "Error fetching city list", e);
            }
            return cityList;
        }
    }
}