package com.example.kevin.weatherclothingapp;

import android.app.Activity;

/**
 * Created by Kevin on 10/26/2015.
 */
public class APICreator {

    //TODO etsyAPIRequest

    //TODO amazonAPIRequest

    //TODO wundergroundAPIRequest

/*
        tempTV = (TextView) findViewById(R.id.today_temp);

        String key = getKeyFromRawResource();

        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/conditions/q/MN/Minneapolis.json", key);
        new RequestCurrentMplsTempTask().execute(wuTempUrl);


        radarImage = (ImageView) findViewById(R.id.weather_radar);

        String wuRadarURL = String.format("http://api.wunderground.com/api/%s/radar/q/MN/Minneapolis.gif?width=280&height=280&newmaps=1", key);
        new RequestMinneapolisWeatherMap().execute(wuRadarURL);
    }

    class RequestCurrentMplsTempTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            String responseString = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream responseStream = new BufferedInputStream(connection.getInputStream());

                InputStreamReader streamReader = new InputStreamReader(responseStream);

                StringBuffer buffer = new StringBuffer();

                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }

                responseString = buffer.toString();
                Log.i("WEATHER", "String is" + responseString);
            } catch (Exception e) {

                Log.e(TAG, e.toString());

            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    JSONObject forecast = response.getJSONObject("current_observation");
                    String temp = forecast.getString("temp_f");
                    tempTV.setText("The temperature in Minneapolis is " + temp + "F");
                } catch (JSONException e) {
                    Log.e(TAG, "parsing error, check schema?", e);
                    tempTV.setText("Error fetching temperature for Minneapolis");
                }
            } else {
                tempTV.setText("Error fetching temperature for Minneapolis");
                Log.e(TAG, "Result was null, check doInBackground for errors");
            }
        }
    }

    class RequestMinneapolisWeatherMap extends AsyncTask<String, String, Boolean>{

        Bitmap weatherRadar;

        @Override
        protected Boolean doInBackground(String...urls){
            Boolean completedSuccessfully = false;
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = connection.getInputStream();
                weatherRadar = BitmapFactory.decodeStream(responseStream);
                completedSuccessfully = true;
            }
            catch(Exception e){
                Log.e(TAG, "Error fetching weather map", e);
            }
            return completedSuccessfully;
        }

        @Override
        protected void onPostExecute (Boolean completed){
            if(completed){
                radarImage.setImageBitmap(weatherRadar);
            }
            else{
                Log.e(TAG, "No image to display");
            }
        }
    }

    private String getKeyFromRawResource(){
        InputStream keyStream = getResources().openRawResource(R.raw.key);
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
    }
    */
}
