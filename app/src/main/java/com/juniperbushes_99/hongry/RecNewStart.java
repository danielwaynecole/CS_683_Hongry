package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecNewStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecNewStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecNewStart extends Fragment {

    View view;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecNewStart.
     */
    public static RecNewStart newInstance() {
        return new RecNewStart();
    }

    public RecNewStart() {
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
        View inf = inflater.inflate(R.layout.fragment_rec_new_start, container, false);
        Spinner spinner = (Spinner) inf.findViewById(R.id.cuisineSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(inf.getContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        for(int i = 0; i < Constants.yummlyCuisines.length; i++){
            String cuisine = Constants.yummlyCuisines[i];
            spinnerAdapter.add(cuisine);
        }
        spinnerAdapter.notifyDataSetChanged();
        final Button eatOutButton = (Button) inf.findViewById(R.id.searchRecipeGo);
        eatOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchRecipesGo();
            }
        });
        view = inf;
        return inf;
    }

    @SuppressWarnings("unchecked")
    public void searchRecipesGo(){
        EditText keyword = (EditText) view.findViewById(R.id.recipeKeyword);

        ArrayList<String> params = new ArrayList<>();

        // add keyword to params
        String k = keyword.getText().toString();
        params.add(k);

        // add search radius
        Spinner cuisinePicker = (Spinner) view.findViewById(R.id.cuisineSpinner);
        String c = cuisinePicker.getSelectedItem().toString();
        params.add(String.valueOf(c));
        try {
            String json = new YummlySearch().execute(params).get().toString();
            mListener.onFragmentInteraction("recipeList", json);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
