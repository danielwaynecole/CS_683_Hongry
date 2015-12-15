package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsButtons.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsButtons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsButtons extends Fragment {

    private String sourceURL;
    private String ingredientsJSON;
    private String nutritionalJSON;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sourceURL Parameter 1.
     * @return A new instance of fragment RecipeDetailsButtons.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsButtons newInstance(String sourceURL, String ingredientsJSON, String nutritionalJSON) {
        RecipeDetailsButtons fragment = new RecipeDetailsButtons();
        fragment.ingredientsJSON = ingredientsJSON;
        fragment.nutritionalJSON = nutritionalJSON;
        fragment.sourceURL = sourceURL;
        return fragment;
    }

    public RecipeDetailsButtons() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details_buttons, container, false);

        // details button click interface with activity
        final Button recipeDetailsDetailsButton = (Button) inf.findViewById(R.id.RecipeDetailsDetailsButton);
        recipeDetailsDetailsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(sourceURL));
                startActivity(i);
            }
        });

        // ingredients button click interface with activity
        final Button recipeDetailsIngredientsButton = (Button) inf.findViewById(R.id.RecipeDetailsIngredientsButton);
        recipeDetailsIngredientsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onFragmentInteraction("ingredients", ingredientsJSON);
            }
        });

        // nutritional info button click interface with activity
        final Button recipeDetailsNutInfoButton = (Button) inf.findViewById(R.id.RecipeDetailsNutInfoButton);
        recipeDetailsNutInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onFragmentInteraction("nutinfo", nutritionalJSON);
            }
        });

        return inf;
    }

    @SuppressWarnings("deprecation")
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
        void onFragmentInteraction(String s, String d);
    }

}
