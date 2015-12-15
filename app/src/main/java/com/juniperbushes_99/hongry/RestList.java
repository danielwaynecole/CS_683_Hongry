package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestList extends Fragment {
    private static final String TAG = "ListRestaurants";
    ListView listView;
    ArrayList<Restaurant> listItems;
    ArrayAdapter<Restaurant> adapter;
    JSONArray jsonArray;
    String json;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestList.
     */
    // TODO: Rename and change types and number of parameters
    public static RestList newInstance() {
        return new RestList();
    }

    public RestList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString("restaurantList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_rest_list, container, false);
        listView = (ListView) inf.findViewById(R.id.restaurantList);
        listItems = new ArrayList<>();

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
        }
        assert jsonRootObject != null;
        jsonArray = jsonRootObject.optJSONArray("businesses");
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, String> hmap = new HashMap<>();
            assert jsonObject != null;
            hmap.put("title", jsonObject.optString("name"));
            hmap.put("id", jsonObject.optString("id"));
            hmap.put("json", jsonObject.toString());
            Restaurant restaurant = new Restaurant(hmap);
            Log.i(TAG, restaurant.getJson());
            listItems.add(restaurant);
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Restaurant restaurant = (Restaurant) adapter.getItemAtPosition(position);
                showRestaurantDetails(restaurant.getJson());
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
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

    private void showRestaurantDetails(String json){
       mListener.onFragmentInteraction("restDetails", json);
    }

}
