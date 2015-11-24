package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.net.Uri;
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
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsIngredients.OnFragmentInteractionListener} interface
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

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredients
     * @return A new instance of fragment RecipeDetailsIngredients.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsIngredients newInstance(JSONArray ingredients) {
        RecipeDetailsIngredients fragment = new RecipeDetailsIngredients();
        fragment.setIngredients(ingredients);
        return fragment;
    }

    public RecipeDetailsIngredients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called" + "\n");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details_ingredients, container, false);
        // build the ingredients listview
        ingredientListView = (ListView) inf.findViewById(R.id.RecipeIngredients);
        //ingredientListView = (ListView) getView().findViewById(R.id.RecipeIngredients);
        ingredientListItems = new ArrayList<String>();
        String ingredientsString = "";
        for(int i=0; i < ingredients.length(); i++) {
            try {
                ingredientListItems.add(ingredients.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "ingredient list: " + ingredientListItems.toString() + "\n");

        ingredientListAdapter = new ArrayAdapter<String>(inf.getContext(), android.R.layout.simple_list_item_1, ingredientListItems);
        ingredientListView.setAdapter(ingredientListAdapter);
        return inf;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public JSONArray getIngredients() {
        return ingredients;
    }

    public void setIngredients(JSONArray ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
     public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String s);
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
