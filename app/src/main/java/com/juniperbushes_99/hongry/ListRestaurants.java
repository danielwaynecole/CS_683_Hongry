package com.juniperbushes_99.hongry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListRestaurants extends AppCompatActivity {

    private static final String TAG = "ListRestaurants";
    ListView listView;
    ArrayList<Restaurant> listItems;
    ArrayAdapter<Restaurant> adapter;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurants);

        listView = (ListView) findViewById(R.id.restaurantList);
        listItems = new ArrayList<Restaurant>();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String json = bundle.getString("restaurantList");
        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
        }
        jsonArray = jsonRootObject.optJSONArray("businesses");
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("title", jsonObject.optString("name").toString());
            hmap.put("id", jsonObject.optString("id").toString());
            hmap.put("json", jsonObject.toString());
            Restaurant restaurant = new Restaurant(hmap);
            Log.i(TAG, restaurant.getJson());
            listItems.add(restaurant);
        }

        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Restaurant restaurant = (Restaurant) adapter.getItemAtPosition(position);
                showRestaurantDetails(restaurant.getJson());
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_restaurants, menu);
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

    private void showRestaurantDetails(String json){
        Intent i = new Intent(this, RestaurantDetails.class);

        //Create the bundle
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("json", json);

        //Add the bundle to the intent
        i.putExtras(bundle);

        //Fire that second activity
        startActivity(i);
    }
}
