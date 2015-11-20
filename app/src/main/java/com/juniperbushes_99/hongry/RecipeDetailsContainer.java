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

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeDetailsContainer extends FragmentActivity
        implements RecipeDetailsIngredients.OnFragmentInteractionListener,
        RecipeDetailsHeader.OnFragmentInteractionListener,
        RecipeDetailsNutritionalInfo.OnFragmentInteractionListener,
        RecipeDetailsInstructions.OnFragmentInteractionListener,
        RecipeDetailsButtons.OnFragmentInteractionListener
{
    private static final String TAG = "RECIPE_DETAILS_CONT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_container);

        Bundle bundle = getIntent().getExtras();

        ArrayList<String> params = new ArrayList<String>();

        //Extract the data…
        String id = bundle.getString("recipeID");
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
        JSONObject jsonRootObject = null;
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

        JSONArray ingredients = jsonRootObject.optJSONArray("ingredientLines");


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

            //String id, String imageURL, String title, String servingInfo
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsHeader headerFragment = RecipeDetailsHeader.newInstance(id, imageURL, title, servingInfo);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.recipeDetailsHeaderFragmentContainer, headerFragment).commit();
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
    public void onFragmentInteraction(Uri uri) {

    }
}
