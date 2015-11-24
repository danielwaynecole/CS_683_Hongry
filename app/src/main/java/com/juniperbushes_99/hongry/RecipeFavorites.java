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

public class RecipeFavorites extends AppCompatActivity {
    private static final String TAG = "RECIPE_FAVORITES";

    ListView recipeFavoritesListView;
    RecipeListArrayAdapter recipeFavoritesListAdapter;
    ArrayAdapter<String> recipeFavoritesListAdapterString;
    ArrayList<Recipe> recipeFavoritesListItems = new ArrayList<Recipe>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favorites);
        loadFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_favorites, menu);
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
        recipeFavoritesListView = (ListView) findViewById(R.id.recipeFavoritesList);
        //ingredientListView = (ListView) getView().findViewById(R.id.RecipeIngredients);
        String FILENAME = "recipe_favorites";
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
                    String title = fave.optString("title");
                    String infoString = title;
                    HashMap<String, String> hmap = new HashMap<String, String>();
                    hmap.put("title", title);
                    hmap.put("id", id);

                    Recipe r = new Recipe(hmap);
                    recipeFavoritesListItems.add(r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            recipeFavoritesListAdapter = new RecipeListArrayAdapter(this, android.R.layout.list_content, recipeFavoritesListItems);
            recipeFavoritesListView.setAdapter(recipeFavoritesListAdapter);
        } else {
            String infoString = "No Favorites Saved";
            ArrayList<String> noResults = new ArrayList<String>();
            noResults.add(infoString);
            recipeFavoritesListAdapterString = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noResults);
            recipeFavoritesListView.setAdapter(recipeFavoritesListAdapterString);
        }
    }
}
