package com.example.kevin.weatherclothingapp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Kevin on 10/26/2015.
 */
public class GarmentTypeFragment extends ListFragment{

    ListView mGarmentOptionList;
    Button mButton;
    EditText mNewGarmentEditText;

    //TODO set the list to display the various garment types. These should be takes from the "SearchOptions" class...
    //TODO ...so that when the list is changed, it can be accessed by other classes.

    //TODO set it so that user can

    //TODO Create a list of checkboxes for each type of garment chosen.

    //TODO Create a list for various persons of clothing (men, women, children).

    //TODO allow the user to choose the default store.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.garment_type_fragment, container, false);
        mGarmentOptionList = (ListView)v.findViewById(R.id.garmentList);
        mButton = (Button)v.findViewById(R.id.newGarmentButton);
        mNewGarmentEditText = (EditText)v.findViewById(R.id.newGarmentEditText);
        return v;
    }
}
