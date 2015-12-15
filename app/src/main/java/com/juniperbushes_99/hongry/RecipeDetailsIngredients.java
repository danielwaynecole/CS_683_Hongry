package com.juniperbushes_99.hongry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecipeDetailsIngredients#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsIngredients extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "RECIPE_DETAILS_INGR";

    ListView ingredientListView;
    ArrayList<String> ingredientListItems;
    ArrayAdapter<String> ingredientListAdapter;
    private JSONArray ingredients;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredients String
     * @return A new instance of fragment RecipeDetailsIngredients.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsIngredients newInstance(String ingredients) {
        Log.i(TAG, "initial stuff " + ingredients);
        RecipeDetailsIngredients fragment = new RecipeDetailsIngredients();
        try {
            JSONArray jIngredients = new JSONArray(ingredients);
            fragment.setIngredients(jIngredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public RecipeDetailsIngredients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            Bundle b = getArguments();
            String iS = b.getString("ingredientList");
            try {
                JSONArray jIngredients = new JSONArray(iS);
                setIngredients(jIngredients);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "onCreate called" + "\n");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details_ingredients, container, false);
        // build the ingredients listview
        ingredientListView = (ListView) inf.findViewById(R.id.RecipeIngredients);
        //ingredientListView = (ListView) getView().findViewById(R.id.RecipeIngredients);
        ingredientListItems = new ArrayList<>();
        Log.i(TAG, "seriously..." + ingredients.toString() + "\n");
        for(int i=0; i < ingredients.length(); i++) {
            try {
                ingredientListItems.add(ingredients.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "ingredient list: " + ingredientListItems.toString() + "\n");

        ingredientListAdapter = new ArrayAdapter<>(inf.getContext(), android.R.layout.simple_list_item_1, ingredientListItems);
        ingredientListView.setAdapter(ingredientListAdapter);
        return inf;
    }

    public void setIngredients(JSONArray ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString(){
        String s = TAG + "\n";
        for(int i=0; i < ingredients.length(); i++) {
            try {
                s += ingredients.getString(i) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

}
