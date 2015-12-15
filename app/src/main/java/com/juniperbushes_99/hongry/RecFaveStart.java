package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecFaveStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecFaveStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecFaveStart extends Fragment {
    private static final String TAG = "RECIPE_FAVORITES";

    ListView recipeFavoritesListView;
    RecipeListArrayAdapter recipeFavoritesListAdapter;
    ArrayList<Recipe> recipeFavoritesListItems = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecFaveStart.
     */
    public static RecFaveStart newInstance() {
        return new RecFaveStart();
    }

    public RecFaveStart() {
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
        View inf =  inflater.inflate(R.layout.fragment_rec_fave_start, container, false);
        loadFavorites(inf);
        return inf;
    }

    private void loadFavorites(View v){
        recipeFavoritesListView = (ListView) v.findViewById(R.id.recipeFavoritesList);
        // open file with recipe favorite json
        String FILENAME = "recipe_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis =  v.getContext().openFileInput(FILENAME);
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        // loop through recipes and add to ArrayList
        for (int i = 0; i < jA.length(); i++) {
            try {
                JSONObject fave = jA.getJSONObject(i);
                String id = fave.optString("id");
                String title = fave.optString("title");
                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("title", title);
                hmap.put("id", id);

                Recipe r = new Recipe(hmap);
                recipeFavoritesListItems.add(r);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // pass to custom array adapter
        recipeFavoritesListAdapter = new RecipeListArrayAdapter(v.getContext(), android.R.layout.list_content, recipeFavoritesListItems, mListener);
        recipeFavoritesListView.setAdapter(recipeFavoritesListAdapter);
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
