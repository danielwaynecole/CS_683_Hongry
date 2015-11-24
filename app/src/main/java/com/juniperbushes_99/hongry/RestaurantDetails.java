package com.juniperbushes_99.hongry;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RestaurantDetails extends AppCompatActivity {
    private static final String TAG = "RESTAURANT_DETAILS";
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String json = bundle.getString("json");
        JSONObject rObj = null;
        try {
            rObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("title", rObj.optString("name").toString());
        hmap.put("id", rObj.optString("id").toString());
        hmap.put("json", rObj.toString());
        restaurant = new Restaurant(hmap);

        // restaurant name
        TextView nameTV = (TextView) findViewById(R.id.name);
        nameTV.setText(rObj.optString("name"));

        // add onclick to add to favorites button
        Button faveButton = (Button) findViewById(R.id.addToFavorites);
        //Select TextView we want to change the Font
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fontawesome-webfont.ttf");
        //Set the typeface
        faveButton.setTypeface(font);
        if(!isInFavorites()) {
            faveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        addToFavorites();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            faveButton.setText(R.string.already_in_favorites);
            faveButton.setTextColor(Color.parseColor("#B00000"));
        }
    }
    private void addToFavorites() throws JSONException {
        // show toast
        Toast.makeText(this.getApplicationContext(), "Saving to favorites...", Toast.LENGTH_LONG).show();

        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis = this.openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            fis.close();
            JSONObject jO = new JSONObject(json.toString());
            jA = jO.getJSONArray("favorites");
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add new favorite
        JSONObject newFave = new JSONObject(restaurant.getJson());
        jA.put(newFave);

        // recreate json
        JSONObject newJO = new JSONObject();
        newJO.put("favorites", jA);
        Log.i(TAG, JSONObject.quote(newJO.toString()));
        FileOutputStream fos;
        try {
            fos =  this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(newJO.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_details, menu);
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

    private boolean isInFavorites() {
        boolean isInFavorites = false;
        // remove from file
        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis = this.openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            fis.close();
            JSONObject jO = new JSONObject(json.toString());
            jA = jO.getJSONArray("favorites");
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // remove unwanted element
        for (int i = 0; i < jA.length(); i++) {
            try {
                JSONObject r = jA.getJSONObject(i);
                if (r.optString("id").equalsIgnoreCase(restaurant.getId())) {
                    isInFavorites = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return isInFavorites;
    }
}
