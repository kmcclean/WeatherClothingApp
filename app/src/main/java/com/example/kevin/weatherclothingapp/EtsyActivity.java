package com.example.kevin.weatherclothingapp;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONObject;
import java.util.ArrayList;

//this is the fragment calleed by WeatherClothingActivity when a date is selected.
public class EtsyActivity extends ListActivity{

    ListView lv;

    JSONObject jso;
    ArrayList<String> mEtsyProductNameArray = new ArrayList<>();
    ArrayList<String> mEtsyProductDescriptionArray = new ArrayList<>();
    ArrayList<String> mEtsyProductCostArray = new ArrayList<>();
    ArrayList<Bitmap> mEtsyProductImageArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_etsy);

        APICreator apic = new APICreator(this);
        jso = apic.getEtsyJSONObject();
        for (int i = 0; i <=4; i++){
            mEtsyProductNameArray.add(apic.getEtsyString(jso, i, "title"));
            mEtsyProductDescriptionArray.add(apic.getEtsyString(jso, i, "description"));
            mEtsyProductCostArray.add(apic.getEtsyString(jso, i, "price"));

            String url = apic.getEtsyURLString(jso, i, "url_75x75");
            mEtsyProductImageArray.add(apic.getEtsyImage(url));
        }

        if (mEtsyProductNameArray == null){
            Log.e("EtsyActivity.class", "imageArrayURLList in EtsyActivity class is null.");
        }

        CustomEtsyListAdapter cela = new CustomEtsyListAdapter(this, mEtsyProductNameArray, mEtsyProductCostArray, mEtsyProductDescriptionArray, mEtsyProductImageArray);
        lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(cela);

    }
}
