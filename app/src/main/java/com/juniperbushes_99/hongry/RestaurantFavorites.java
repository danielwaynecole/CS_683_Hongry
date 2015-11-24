package com.juniperbushes_99.hongry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantFavorites extends AppCompatActivity {
    private static final String TAG = "RESTAURANT_FAVORITES";

    ListView restaurantFavoritesListView;
    RestaurantListArrayAdapter restaurantFavoritesListAdapter;
    ArrayAdapter<String> restaurantFavoritesListAdapterString;
    ArrayList<Restaurant> restaurantFavoritesListItems = new ArrayList<Restaurant>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_favorites);
        loadFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFavorites(){
        restaurantFavoritesListView = (ListView) findViewById(R.id.restaurantFavoritesList);
        //ingredientListView = (ListView) getView().findViewById(R.id.RestaurantIngredients);
        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis =  openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            fis.close();
            Log.i(TAG, json.toString());
            JSONObject jO = new JSONObject(json.toString());
            jA = jO.getJSONArray("favorites");
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jA.length() > 0) {
            for (int i = 0; i < jA.length(); i++) {
                try {
                    JSONObject fave = jA.getJSONObject(i);
                    String id = fave.optString("id");
                    String name = fave.optString("name");
                    HashMap<String, String> hmap = new HashMap<String, String>();
                    hmap.put("title", name);
                    hmap.put("id", id);
                    hmap.put("json", fave.toString());

                    Restaurant r = new Restaurant(hmap);
                    restaurantFavoritesListItems.add(r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            restaurantFavoritesListAdapter = new RestaurantListArrayAdapter(this, android.R.layout.list_content, restaurantFavoritesListItems);
            restaurantFavoritesListView.setAdapter(restaurantFavoritesListAdapter);
        } else {
            String infoString = "No Favorites Saved";
            ArrayList<String> noResults = new ArrayList<String>();
            noResults.add(infoString);
            restaurantFavoritesListAdapterString = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noResults);
            restaurantFavoritesListView.setAdapter(restaurantFavoritesListAdapterString);
        }
    }
}
