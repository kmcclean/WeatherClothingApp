package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Kevin on 11/1/2015.
 */

//this is the fragment calleed by WeatherClothingActivity when a date is selected.
public class EtsyFragment extends Fragment{

    ImageView etsyImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.etsy_image_return_fragment, container, false);
        etsyImageView = (ImageView)v.findViewById(R.id.etsyImage);
        Activity a = getActivity();
        APICreator apic = new APICreator(a);
        Bitmap image = apic.getEtsyImage();
        etsyImageView.setImageBitmap(image);
        return v;
    }
}
