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
 * {@link RecList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecList extends Fragment {
    private static final String TAG = "ListRecipes";
    ListView listView;
    ArrayList<Recipe> listItems;
    ArrayAdapter<Recipe> adapter;
    JSONObject jsonRootObject = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecList.
     */
    // TODO: Rename and change types and number of parameters
    public static RecList newInstance() {
        return new RecList();
    }

    public RecList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get the bundle
            Bundle bundle = getArguments();
            //Extract the data…
            String json = bundle.getString("recipeList");
            try {
                jsonRootObject = new JSONObject(json);
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_rec_list, container, false);
        listView = (ListView) inf.findViewById(R.id.recipeList);
        listItems = new ArrayList<>();
        JSONArray jsonArray = jsonRootObject.optJSONArray("matches");
        Log.i(TAG, "blarg: " + jsonArray.toString() + "\n");
        if(jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> hmap = new HashMap<>();
                assert jsonObject != null;
                hmap.put("title", jsonObject.optString("recipeName"));
                hmap.put("id", jsonObject.optString("id"));

                Recipe r = new Recipe(hmap);
                listItems.add(r);
            }

            adapter = new ArrayAdapter<>(inf.getContext(), android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3) {
                    Recipe recipe = (Recipe) adapter.getItemAtPosition(position);
                    mListener.onFragmentInteraction("recDetails", recipe.getId());
                }
            });
        }
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
