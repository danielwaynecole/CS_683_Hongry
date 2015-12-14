package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.content.Context;
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
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsNutritionalInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsNutritionalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsNutritionalInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "RECIPE_DETAILS_INGR";

    ListView nutritionListView;
    ArrayList<String> nutritionListItems;
    ArrayAdapter<String> nutritionListAdapter;
    private JSONArray nutrtionalInfo;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nutritionalInfo String
     * @return A new instance of fragment RecipeDetailsNutritionalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsNutritionalInfo newInstance(String nutritionalInfo) {
        RecipeDetailsNutritionalInfo fragment = new RecipeDetailsNutritionalInfo();
        try {
            JSONArray jNutritionalInfo = new JSONArray(nutritionalInfo);
            fragment.setNutrtionalInfo(jNutritionalInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public RecipeDetailsNutritionalInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            Bundle b = getArguments();
            String json = b.getString("nutritionList");
            String niS = json;
            try {
                JSONArray jNutritionalInfo = new JSONArray(niS);
                setNutrtionalInfo(jNutritionalInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details_ingredients, container, false);
        // build the ingredients listview
        nutritionListView = (ListView) inf.findViewById(R.id.RecipeIngredients);
        //ingredientListView = (ListView) getView().findViewById(R.id.RecipeIngredients);
        nutritionListItems = new ArrayList<String>();
        Boolean hasNutritionalInfo = false;
        String infoString = "";
        for(int i=0; i < nutrtionalInfo.length(); i++) {
            infoString = "";
            JSONObject unit = null;
            String unitString = "";
            JSONObject info = null;
            try {
                info = nutrtionalInfo.getJSONObject(i);
                unit = info.getJSONObject("unit");
                if(Float.parseFloat(info.optString("value")) == 1){
                    unitString = unit.optString("name");
                } else {
                    unitString = unit.optString("plural");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!(info.isNull("description"))){
                hasNutritionalInfo = true;
                infoString += info.optString("description").toString() + ": " + info.optString("value").toString() + " " + unitString;
                nutritionListItems.add(infoString);
            }
        }
        if(!hasNutritionalInfo){
            infoString += "No Nutritional Information Available";
            nutritionListItems.add(infoString);
        }
        nutritionListAdapter = new ArrayAdapter<String>(inf.getContext(), android.R.layout.simple_list_item_1, nutritionListItems);
        nutritionListView.setAdapter(nutritionListAdapter);
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

    public JSONArray getNutrtionalInfo() {
        return nutrtionalInfo;
    }

    public void setNutrtionalInfo(JSONArray nutrtionalInfo) {
        this.nutrtionalInfo = nutrtionalInfo;
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
        public void onFragmentInteraction(String s, String d);
    }

}
