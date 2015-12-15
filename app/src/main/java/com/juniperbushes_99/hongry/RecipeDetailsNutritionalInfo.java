package com.juniperbushes_99.hongry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * to handle interaction events.
 * Use the {@link RecipeDetailsNutritionalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsNutritionalInfo extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "RECIPE_DETAILS_INGR";

    ListView nutritionListView;
    ArrayList<String> nutritionListItems;
    ArrayAdapter<String> nutritionListAdapter;
    private JSONArray nutrtionalInfo;

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
            String niS = b.getString("nutritionList");
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
        nutritionListItems = new ArrayList<>();
        Boolean hasNutritionalInfo = false;
        String infoString = "";
        for(int i=0; i < nutrtionalInfo.length(); i++) {
            infoString = "";
            JSONObject unit;
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
            assert info != null;
            if(!info.isNull("description")){
                hasNutritionalInfo = true;
                infoString += info.optString("description") + ": " + info.optString("value") + " " + unitString;
                nutritionListItems.add(infoString);
            }
        }
        if(!hasNutritionalInfo){
            infoString += "No Nutritional Information Available";
            nutritionListItems.add(infoString);
        }
        nutritionListAdapter = new ArrayAdapter<>(inf.getContext(), android.R.layout.simple_list_item_1, nutritionListItems);
        nutritionListView.setAdapter(nutritionListAdapter);
        return inf;
    }

    public void setNutrtionalInfo(JSONArray nutrtionalInfo) {
        this.nutrtionalInfo = nutrtionalInfo;
    }


}
