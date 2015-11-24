package com.juniperbushes_99.hongry;

import android.net.Uri;
import android.support.v4.app.*;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeDetailsContainer extends FragmentActivity
        implements RecipeDetailsIngredients.OnFragmentInteractionListener,
        RecipeDetailsHeader.OnFragmentInteractionListener,
        RecipeDetailsNutritionalInfo.OnFragmentInteractionListener,
        RecipeDetailsButtons.OnFragmentInteractionListener {
    private static final String TAG = "RECIPE_DETAILS_CONT";

    private JSONArray ingredients;
    private JSONArray nutritionalInfo;
    private JSONObject jsonRootObject;
    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_container);

        Bundle bundle = getIntent().getExtras();

        ArrayList<String> params = new ArrayList<String>();

        //Extract the data…
        String id = bundle.getString("recipeID");
        this.recipeID = id;
        // add keyword to params
        params.add(id);
        String json = null;
        try {
            json = new YummlyDetails().execute(params).get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject imageObject = null;
        JSONObject sourceObject = null;

        try {
            jsonRootObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // recipe image
        JSONArray jsonImageArray = jsonRootObject.optJSONArray("images");
        try {
            imageObject = jsonImageArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageURL = imageObject.optString("hostedMediumUrl").toString();
        Log.i(TAG, "image url: " + imageURL + "\n");

        ingredients = jsonRootObject.optJSONArray("ingredientLines");
        nutritionalInfo = jsonRootObject.optJSONArray("nutritionEstimates");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.recipeDetailsBodyFragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsIngredients ingredientsFragment = RecipeDetailsIngredients.newInstance(ingredients);
            Log.i(TAG, ingredientsFragment.toString());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.recipeDetailsBodyFragmentContainer, ingredientsFragment).commit();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.recipeDetailsHeaderFragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // set the recipe title
            String title = jsonRootObject.optString("name");
            // set the recipe title
            String servingInfo = jsonRootObject.optString("yield");
            boolean isFave = isInFavorites();

            //String id, String imageURL, String title, String servingInfo
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsHeader headerFragment = RecipeDetailsHeader.newInstance(id, imageURL, title, servingInfo, isFave);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.recipeDetailsHeaderFragmentContainer, headerFragment).commit();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.recipeDetailsButtonsFragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            JSONObject jsonSourceObject = null;
            try {
                jsonSourceObject = jsonRootObject.getJSONObject("source");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String sourceURL = jsonSourceObject.optString("sourceRecipeUrl").toString();

            //String id, String imageURL, String title, String servingInfo
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsButtons buttonsFragment = RecipeDetailsButtons.newInstance(sourceURL);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.recipeDetailsButtonsFragmentContainer, buttonsFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_details_container, menu);
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

    @Override
    public void onFragmentInteraction(String s) {
        if (s.equalsIgnoreCase("ingredients")) {
            // Create fragment and give it an argument specifying the article it should show
            RecipeDetailsIngredients ingredientsFragment = RecipeDetailsIngredients.newInstance(ingredients);
            Bundle args = new Bundle();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.recipeDetailsBodyFragmentContainer, ingredientsFragment);
            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();
        } else if (s.equalsIgnoreCase("nutinfo")) {
            // Create fragment and give it an argument specifying the article it should show
            RecipeDetailsNutritionalInfo nutritionFragment = RecipeDetailsNutritionalInfo.newInstance(nutritionalInfo);
            Bundle args = new Bundle();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.recipeDetailsBodyFragmentContainer, nutritionFragment);
            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();
        }
    }

    private boolean isInFavorites() {
        boolean isInFavorites = false;
        // remove from file
        String FILENAME = "recipe_favorites";
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
                if (r.optString("id").equalsIgnoreCase(recipeID)) {
                    isInFavorites = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return isInFavorites;
    }
}