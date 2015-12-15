package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.app.FragmentManager;
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
 * {@link RestFaveStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestFaveStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestFaveStart extends Fragment {
    private static final String TAG = "RESTAURANT_FAVORITES";

    ListView restaurantFavoritesListView;
    RestaurantListArrayAdapter restaurantFavoritesListAdapter;
    ArrayList<Restaurant> restaurantFavoritesListItems = new ArrayList<>();
    View view;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestFaveStart.
     */
    // TODO: Rename and change types and number of parameters
    public static RestFaveStart newInstance() {
        return new RestFaveStart();
    }

    public RestFaveStart() {
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
        View inf = inflater.inflate(R.layout.fragment_rest_fave_start, container, false);
        view = inf;
        if(savedInstanceState == null) {
            loadFavorites();
        }
        return inf;
    }

    private void loadFavorites(){
        restaurantFavoritesListView = (ListView) view.findViewById(R.id.restaurantFavoritesList);
        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis =  getContext().openFileInput(FILENAME);
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
        if(jA.length() > 0) {
            // loop through restaurants and create Restaurant objects, then add to custom array adapter
            for (int i = 0; i < jA.length(); i++) {
                try {
                    JSONObject fave = jA.getJSONObject(i);
                    String id = fave.optString("id");
                    String name = fave.optString("name");
                    HashMap<String, String> hmap = new HashMap<>();
                    hmap.put("title", name);
                    hmap.put("id", id);
                    hmap.put("json", fave.toString());

                    Restaurant r = new Restaurant(hmap);
                    restaurantFavoritesListItems.add(r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            restaurantFavoritesListAdapter = new RestaurantListArrayAdapter(getContext(), android.R.layout.list_content, restaurantFavoritesListItems, mListener);
            restaurantFavoritesListView.setAdapter(restaurantFavoritesListAdapter);
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

    @Override
    public void onPause() {
        super.onPause();
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.saveFragmentInstanceState(RestFaveStart.this);
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
