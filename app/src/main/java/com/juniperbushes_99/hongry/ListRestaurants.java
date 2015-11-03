package com.juniperbushes_99.hongry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ListRestaurants extends AppCompatActivity {

    private static final String TAG = "ListRestaurants";
    ListView listView;
    ArrayList<Recipe> listItems;
    ArrayAdapter<Recipe> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurants);

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
        JSONArray jsonArray = jsonRootObject.optJSONArray("businesses");
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

            Recipe r = new Recipe(hmap);
            listItems.add(r);
        }

        listView = (ListView) findViewById(R.id.restaurantList);
        listItems = new ArrayList<Recipe>();
        adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);


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
}
