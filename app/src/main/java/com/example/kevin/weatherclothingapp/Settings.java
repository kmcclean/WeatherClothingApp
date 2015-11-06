package com.example.kevin.weatherclothingapp;

import android.os.Bundle;
import android.util.Log;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.associates.AssociatesAPI;
import com.amazon.device.associates.LinkService;
import com.amazon.device.associates.NotInitializedException;
import com.amazon.device.associates.OpenSearchPageRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Settings extends Activity{

    // Code researched and adapted from examples found on these pages:
    // http://mobilemerit.com/amazon-product-advertising-api-tutorial/
    // https://developer.amazon.com/public/apis/earn/mobile-associates/docs/setting-up-your-environment
    // https://developer.amazon.com/appsandservices/apis/earn/mobile-associates
    // http://developer.android.com/reference/android/webkit/WebView.html
    // https://developer.amazon.com/public/apis/earn/mobile-associates/javadocs/amazon-mobile-associates-javadoc
    // https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
//
    private static final String TAG = "Project1Test1";
    private TextView mStoreToSearchTextView;
    private TextView mCategoryToSearchTextView;
    private TextView mItemsToSearchTextView;
    String storeToSearch;
    String amazonSearchMenWomenChildren;
    String amazonItemsToSearch;
    WebView amazonResultsWebView;
    //    Activity activity;
    String amazonSearchTerm;
    String amazonCategory;
    String temperatureDescription;
    String weatherDescription;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        String APPLICATION_KEY = getKeyFromRawResource("APPLICATION_KEY");
        AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, this));

        //---STORE TO SEARCH RADIOBUTTONS---
        mStoreToSearchTextView = (TextView) findViewById(R.id.store_to_search_textview);
        RadioGroup radioGroupStoreToSearch = (RadioGroup) findViewById(R.id.store_to_search_radiogroup);
        radioGroupStoreToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSearchAmazon = (RadioButton) findViewById(R.id.store_to_search_amazon);
                RadioButton rbSearchEtsy = (RadioButton) findViewById(R.id.store_to_search_etsy);
                if (rbSearchAmazon.isChecked()) {
                    DisplayToast("You have selected Amazon!");
                    storeToSearch = "Amazon";
                } else if (rbSearchEtsy.isChecked()) {
                    DisplayToast("You have selected Etsy!");
                    storeToSearch = "Etsy";
                }
            }
        });

        //---CATEGORY TO SEARCH RADIOBUTTONS---
        mCategoryToSearchTextView = (TextView) findViewById(R.id.category_to_search_textview);
        RadioGroup radioGroupCategoryToSearch = (RadioGroup) findViewById(R.id.category_to_search_radiogroup);
        radioGroupCategoryToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSearchMens = (RadioButton) findViewById(R.id.category_to_search_mens);
                RadioButton rbSearchWomens = (RadioButton) findViewById(R.id.category_to_search_womens);
                RadioButton rbSearchChildrens = (RadioButton) findViewById(R.id.category_to_search_childrens);
                if (rbSearchMens.isChecked()) {
                    DisplayToast("You have selected Men's Clothing!");
                    amazonSearchMenWomenChildren = "Men's Clothing";
                } else if (rbSearchWomens.isChecked()) {
                    DisplayToast("You have selected Women's Clothing!");
                    amazonSearchMenWomenChildren = "Women's Clothing";
                } else if (rbSearchChildrens.isChecked()) {
                    DisplayToast("You have selected Children & Baby Clothing!");
                    amazonSearchMenWomenChildren = "Children & Baby Clothing";
                }
            }
        });

        //---ITEMS TO SEARCH RADIOBUTTONS---
        mItemsToSearchTextView = (TextView) findViewById(R.id.items_to_search_textview);
        RadioGroup radioGroupItemsToSearch = (RadioGroup) findViewById(R.id.items_to_search_radiogroup);
        radioGroupItemsToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSearchHats = (RadioButton) findViewById(R.id.item_to_search_hats);
                RadioButton rbSearchCoats = (RadioButton) findViewById(R.id.item_to_search_coats);
                RadioButton rbSearchShirts = (RadioButton) findViewById(R.id.item_to_search_shirts);
                RadioButton rbSearchDresses = (RadioButton) findViewById(R.id.item_to_search_dresses);
                RadioButton rbSearchPants = (RadioButton) findViewById(R.id.item_to_search_pants);
                RadioButton rbSearchSuits = (RadioButton) findViewById(R.id.item_to_search_suits);
                RadioButton rbSearchSocks = (RadioButton) findViewById(R.id.item_to_search_socks);
                RadioButton rbSearchShoes = (RadioButton) findViewById(R.id.item_to_search_shoes);

                if (rbSearchHats.isChecked()) {
                    DisplayToast("You have selected Hats!");
                    amazonItemsToSearch = "Hats";
                } else if (rbSearchCoats.isChecked()) {
                    DisplayToast("You have selected Jackets and Coats!");
                    amazonItemsToSearch = "Jackets and Coats";
                } else if (rbSearchShirts.isChecked()) {
                    DisplayToast("You have selected Shirts!");
                    amazonItemsToSearch = "Shirts";
                } else if (rbSearchDresses.isChecked()) {
                    DisplayToast("You have selected Dresses and skirts!");
                    amazonItemsToSearch = "Dresses and skirts";
                } else if (rbSearchPants.isChecked()) {
                    DisplayToast("You have selected Pants!");
                    amazonItemsToSearch = "Pants";
                } else if (rbSearchSuits.isChecked()) {
                    DisplayToast("You have selected Suits, sport coats and blazers!");
                    amazonItemsToSearch = "Suits, sport coats and blazers";
                } else if (rbSearchSocks.isChecked()) {
                    DisplayToast("You have selected Socks and hosiery!");
                    amazonItemsToSearch = "Socks and hosiery";
                } else if (rbSearchShoes.isChecked()) {
                    DisplayToast("You have selected Shoes!");
                    amazonItemsToSearch = "Shoes";
                }
            }
        });


        Button b = (Button) findViewById(R.id.clickbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code runs when button clicked

                if (amazonItemsToSearch.equals("Shoes")) {
                    amazonCategory = "Shoes";
                } else {
                    amazonCategory = "ClothingAndAccessories";
                }
                temperatureDescription = "freezing"; // this variable has four possibilities based on the temperature forecast from wunderground. <32 freezing, 32-50	cold, 50-75	warm, and >75 hot. Will need to create a @getTemperatureDescription method to convert wunderground into temperatureDescription
                weatherDescription = "mild"; // this variable has four possibilities based on the weatherIcon from the wunderground forecast: mild, rainy, snowy, and stormy. stormy and rainy are probably the same thing. Will need to create @getweatherDescription method to convert wunderground icon into correct  weatherDescription
                amazonSearchTerm = createAmazonSearchTerm(weatherDescription, temperatureDescription, amazonItemsToSearch, amazonSearchMenWomenChildren);

                // todo the category and search terms will change depending on what the user has chosen to search for. Categories available for the Amazon Mobile Associates API are found here https://developer.amazon.com/public/apis/earn/mobile-associates/docs/available-search-categories
                //This is from https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
                Toast t = Toast.makeText(Settings.this, amazonSearchTerm, Toast.LENGTH_LONG);
                t.show();
                OpenSearchPageRequest request = new OpenSearchPageRequest(amazonCategory, amazonSearchTerm);
                try {
                    LinkService linkService = AssociatesAPI.getLinkService();
                    linkService.openRetailPage(request);
                } catch (NotInitializedException e) {
                    Log.v(TAG, "NotInitializedException error");
                }
            }
        });
    }

    private void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg,
                Toast.LENGTH_SHORT).show();
    }

    private class MAAWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                LinkService linkService = AssociatesAPI.getLinkService();
                return linkService.overrideLinkInvocation(view, url);
            } catch (NotInitializedException e) {
                Log.v(TAG, "NotInitializedException error");
            }
            return false;
        }
    }

    //determines the search term for Amazon based on weather conditions and category of clothing search.
    private String createAmazonSearchTerm(String weatherDescription, String temperatureDescription, String amazonItemsToSearch, String amazonSearchMenWomenChildren) {
        // todo The search string defined by amazonItemsToSearch will change to depending on a) the choice made in categoryToSearch, b) the choice made in itemToSearch, and c) the two weather-forecast variables, which will affect the phrasing of amazonItemsToSearch, i.e. "parkas" vs. "windbreakers." Currently all the variations of amazonItemsToSearch are the ones for mild, clear weather.
        if (weatherDescription.equals("snowy") ||temperatureDescription.equals("freezing")) {
            // You would probably dress essentially the same way in snowy or very cold weather, so the clothing options pulled from the store can be the same
            if (amazonItemsToSearch.equals("Hats")) {
                amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch.equals("Pants");
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                amazonItemsToSearch = "Shoes";
            }
            amazonSearchTerm = amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
            return amazonSearchTerm;
        } else if (temperatureDescription.equals("cold")) {
            // Cold, warm or hot temperatures will return different clothing choices. Rainy weather will affect some clothing choices (shirts, for instance) but not others (like socks), .
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                } else amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            amazonSearchTerm = amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
            return amazonSearchTerm;
        } else if (temperatureDescription.equals("warm")) {
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                } else amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            amazonSearchTerm = amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
            return amazonSearchTerm;
        } else if (temperatureDescription.equals("hot")) {
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                } else amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            amazonSearchTerm = amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
            return amazonSearchTerm;
        }
        return amazonSearchTerm;

    }


    //gets the key from the raw file.
    private String getKeyFromRawResource(String requestedKey) {
        InputStream keyStream = null;
        if (requestedKey.equals("etsy_key")){
            keyStream = this.getResources().openRawResource(R.raw.etsykey);
        }
        else if (requestedKey.equals("key")) {
            keyStream = this.getResources().openRawResource(R.raw.key);
        }
        else if (requestedKey.equals("APPLICATION_KEY")) {
            keyStream = this.getResources().openRawResource(R.raw.amazon_application_key);
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

}
