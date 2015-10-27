package com.example.kevin.weatherclothingapp;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Kevin on 10/26/2015.
 */
public class ComplexWeatherShoppingDataFragment extends ListFragment{

    ListView mProductsList;
    Button mChangeSearchButton;

    //TODO create a view that allows the user to see the listing of what products are available.

    //TODO set the fragment that when the radio buttons are pressed, it automatically calls the API to diplay that store.

    //TODO set the viewer so that when a user presses a button to show a product, it gives more details about the product.

    //

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.complex_weather_shopping_fragment, container, false);
        mProductsList = (ListView)v.findViewById(R.id.product_list);
        mChangeSearchButton = (Button)v.findViewById(R.id.change_search_button);

        return v;
    }
}
