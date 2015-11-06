package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kevin on 10/26/2015.
 */
public class APICreator {

    //TODO etsyAPIRequest

    //TODO amazonAPIRequest

    //TODO create methods that can be used to cover repetitive code.

    ArrayList<Bitmap> weatherIconArrayList;
    ArrayList<String> forecastDayArrayList;
    ArrayList<String> etsyImageURLStringArray;

    private final String TAG = "APICreator Class";

    Activity activity;

    APICreator(Activity a){
        this.activity = a;
    }

    //this gets the image from Etsy that has been selected.
    public JSONObject getEtsyJSONObject(){

        String etsyKey = getKeyFromRawResource("etsykey");
        String etsyUrl = String.format("https://openapi.etsy.com/v2/listings/active?api_key=%s&limit=5&category=clothing&fields=title,price,description&includes=MainImage", etsyKey);
        etsyImageURLStringArray = null;
        JSONObject etsyJSO = null;

        try {
            etsyJSO = new RequestEtsyImagesString().execute(etsyUrl).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get String. Check getEtsyJSONObject method.", e);
        }

        return etsyJSO;
    }


    public Bitmap getEtsyImage(String url){
        Bitmap bitmap = null;
        try{
            bitmap = new RequestImages().execute(url).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getEtsyImage method.", e);
        }
        return bitmap;
    }

    //returns a Bitmap of the forecast icon for the selected day.
    public ArrayList<Bitmap> getForecastIcon() {
        weatherIconArrayList = null;
        String key = getKeyFromRawResource("key");
        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/forecast/q/MN/Minneapolis.json", key);
        try{
            weatherIconArrayList = new RequestCurrentMplsWeatherIcon().execute(wuTempUrl).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getForecastIcon method.", e);
        }
        if (weatherIconArrayList == null){
            Log.e(TAG, "weatherIconArrayList variable in getForecastIcon method is null.");
        }
        return weatherIconArrayList;
    }

    //returns the selected day for the Forecast (Monday, Tuesday, etc.).
    public ArrayList<String> getForecastStringInfo(String string) {
        forecastDayArrayList = null;
        String key = getKeyFromRawResource("key");
        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/forecast/q/MN/Minneapolis.json", key);
        Log.i(TAG, "The string is" + string);
        try{
            forecastDayArrayList = new RequestMinneapolisForecastDay().execute(wuTempUrl, string).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get String. Check getForecastDay method.", e);
        }
        if (forecastDayArrayList == null){
            Log.e(TAG, "forecastDayArrayList variable in getForecastDay method is null.");
        }
        return forecastDayArrayList;
    }

    class RequestEtsyImagesString extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urlInfo) {
            String responseString;
            JSONObject response = null;
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
                    response = new JSONObject(responseString);

            } catch (Exception e) {
                Log.e(null, "Error fetching EtsyInfo", e);
            }
            if (response == null){
                Log.e(TAG, "response variable in RequestEtsyImagesString class is null.");
            }
            return response;
        }
    }

    class RequestImages extends AsyncTask<String, String, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... url){

            Bitmap bitmap = getImage(url[0]);

            return bitmap;
        }

    }

    protected String getEtsyString(JSONObject jso, int i, String s){
        String returnString = null;
        try {
            JSONArray jsa1 = jso.getJSONArray("results");
            JSONObject jso1 = jsa1.getJSONObject(i);
            returnString = jso1.getString(s);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching Etsy String", e);
        }
        return returnString;
    }


    protected String getEtsyURLString(JSONObject jso, int i, String s){
        String returnString = null;
        try {
            JSONArray jsa1 = jso.getJSONArray("results");
            JSONObject jso1 = jsa1.getJSONObject(i);
            JSONObject jso2 = jso1.getJSONObject("MainImage");
            returnString = jso2.getString(s);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching Etsy URL String", e);
        }
        return returnString;
    }

    protected Bitmap getImage(String imageURL){
        Bitmap image = null;
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream responseStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(responseStream);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching image.");
        }
        return image;
    }

    //gets the key from the raw file.
    private String getKeyFromRawResource(String requestedKey) {
        InputStream keyStream = null;
        if (requestedKey.equals("etsykey")){
            keyStream = activity.getResources().openRawResource(R.raw.etsykey);
        }
        else if (requestedKey.equals("key")) {
            keyStream = activity.getResources().openRawResource(R.raw.key);
        }
        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
        try{
            String key = keyStreamReader.readLine();
            return key;
        }
        catch (IOException e){
            Log.e(TAG, "Error reading from raw resource file", e);
            return null;
        }
    }


    //This class goes to WeatherUnderground and gets the icon for the specific day's forecast.
    class RequestCurrentMplsWeatherIcon extends AsyncTask<String, String, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String...urlInfo) {
            String responseString;
            Bitmap weatherIcon;
            String forecastIconURL;
            ArrayList<Bitmap> weatherIconsArrayList = new ArrayList<>();
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

                for (int i = 0; i <= 6; i = i + 2) {
                    JSONObject response = new JSONObject(responseString);
                    JSONObject jso1 = response.getJSONObject("forecast");
                    JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                    JSONArray jsa3 = jso2.getJSONArray("forecastday");
                    JSONObject jso4 = jsa3.getJSONObject(i);
                    forecastIconURL = jso4.getString("icon_url");
                    weatherIcon = getImage(forecastIconURL);
                    weatherIconsArrayList.add(weatherIcon);
                }

            } catch (Exception e) {
                Log.e(null, "Error fetching weather map", e);
            }
            return weatherIconsArrayList;
        }

    }

    //This class goes to WeatherUnderground and gets the day of the forecast.
    class RequestMinneapolisForecastDay extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urlInfo) {
            String responseString;
            String day;
            ArrayList<String> dayArrayList = new ArrayList<>();

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


                for (int i = 0; i <= 6; i = i + 2) {
                    JSONObject response = new JSONObject(responseString);
                    JSONObject jso1 = response.getJSONObject("forecast");
                    JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                    JSONArray jsa3 = jso2.getJSONArray("forecastday");
                    JSONObject jso4 = jsa3.getJSONObject(i);
                    day = jso4.getString(urlInfo[1]);
                    dayArrayList.add(day);
                }
            } catch (Exception e) {
                Log.e(null, "Error fetching weather info.", e);
            }
            return dayArrayList;
        }
    }
}