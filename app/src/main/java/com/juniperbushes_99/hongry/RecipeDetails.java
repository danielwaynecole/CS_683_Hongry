package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecipeDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetails extends Fragment {

    private static final String TAG = "RECIPE_DETAILS_CONT";
    private static final String ID = "recipeID";

    private JSONObject jsonRootObject;
    private String recipeID;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @return A new instance of fragment RecipeDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetails newInstance(String id) {
        RecipeDetails fragment = new RecipeDetails();
        Bundle args = new Bundle();
        args.putString(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeID = getArguments().getString(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ArrayList<String> params = new ArrayList<>();
        params.add(recipeID);
        String json = null;
        try {
            json = new YummlyDetails().execute(params).get().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject imageObject = null;

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
        assert imageObject != null;
        String imageURL = imageObject.optString("hostedMediumUrl");
        Log.i(TAG, "image url: " + imageURL + "\n");

        JSONArray ingredients = jsonRootObject.optJSONArray("ingredientLines");
        JSONArray nutritionalInfo = jsonRootObject.optJSONArray("nutritionEstimates");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (inf.findViewById(R.id.recipeDetailsBodyFragmentContainer) != null) {

            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsIngredients ingredientsFragment = RecipeDetailsIngredients.newInstance(ingredients.toString());
            Log.i(TAG, ingredientsFragment.toString());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.recipeDetailsBodyFragmentContainer, ingredientsFragment).commit();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (inf.findViewById(R.id.recipeDetailsHeaderFragmentContainer) != null) {

            // set the recipe title
            String title = jsonRootObject.optString("name");
            // set the recipe title
            String servingInfo = jsonRootObject.optString("yield");
            boolean isFave = isInFavorites();

            //String id, String imageURL, String title, String servingInfo
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsHeader headerFragment = RecipeDetailsHeader.newInstance(recipeID, imageURL, title, servingInfo, isFave);

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.recipeDetailsHeaderFragmentContainer, headerFragment).commit();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (inf.findViewById(R.id.recipeDetailsButtonsFragmentContainer) != null) {

            JSONObject jsonSourceObject = null;
            try {
                jsonSourceObject = jsonRootObject.getJSONObject("source");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert jsonSourceObject != null;
            final String sourceURL = jsonSourceObject.optString("sourceRecipeUrl");

            //String id, String imageURL, String title, String servingInfo
            // Create a new Fragment to be placed in the activity layout
            RecipeDetailsButtons buttonsFragment = RecipeDetailsButtons.newInstance(sourceURL, ingredients.toString(), nutritionalInfo.toString());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.recipeDetailsButtonsFragmentContainer, buttonsFragment).commit();
        }
        return inf;
    }

    private boolean isInFavorites() {
        boolean isInFavorites = false;
        // remove from file
        String FILENAME = "recipe_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis = getContext().openFileInput(FILENAME);
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
        } catch (IOException | JSONException e) {
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
