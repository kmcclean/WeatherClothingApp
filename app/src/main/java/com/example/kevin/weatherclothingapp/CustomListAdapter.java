package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 11/1/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private ArrayList<Bitmap> forecastIconBMArrayList;
    private ArrayList<String> forecastDayStringList;

    //sets the size of the Custom Adapter, along with the Strings and Bitmap Arrays which are needed for it.
    public CustomListAdapter(Activity context, ArrayList<String> dayList, ArrayList<Bitmap> forecastIcons) {
        super(context, R.layout.weather_list, dayList);
        // TODO Auto-generated constructor stub
        this.forecastIconBMArrayList = forecastIcons;
        this.forecastDayStringList = dayList;
        this.context=context;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weather_list, null, true);

        //The UI view cannot read ArrayLists, so the ALs have to be converted into Arrays.
        Bitmap[] forecastIconBMArray = forecastIconBMArrayList.toArray(new Bitmap[forecastIconBMArrayList.size()]);
        String[] forecastDayStringArray = forecastDayStringList.toArray(new String[forecastDayStringList.size()]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.weatherIconImageView);
        imageView.setImageBitmap(forecastIconBMArray[position]);

        TextView textView = (TextView) rowView.findViewById(R.id.weatherDayTextView);
        textView.setText(forecastDayStringArray[position]);

        return rowView;
    }

    /*@Override
    public int getCount(){
        return forecastIconBMArrayList.size();
    }*/
}
