package com.juniperbushes_99.hongry;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ucoleda on 11/23/15.
 */


public class RestaurantListArrayAdapter extends ArrayAdapter<Restaurant> {
    private ArrayList<Restaurant> restaurants;
    private RestFaveStart.OnFragmentInteractionListener mListener;

    public RestaurantListArrayAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> restaurants, RestFaveStart.OnFragmentInteractionListener mListener) {
        super(context, textViewResourceId, restaurants);
        this.restaurants = restaurants;
        this.mListener = mListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.restaurantlistitem, null);
        }

        final Restaurant restaurant = restaurants.get(position);
        final int pos = position;
        if (restaurant != null) {
            final String json = restaurant.getJson();
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView delete = (TextView) v.findViewById(R.id.delete);
            if (title != null) {
                title.setText(restaurant.getName());
                title.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRestaurantDetails(json);
                    }
                });
            }
            if (delete != null){
                delete.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFavorite(pos);
                    }
                });
            }
        }
        return v;
    }

    private void showRestaurantDetails(String json){
        mListener.onFragmentInteraction("restDetails", json);
    }

    private void deleteFavorite(int position){
        final int curID = position;
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.deleteRestaurantFaveConfirmTitle)
                .setMessage(R.string.deleteRestaurantFaveConfirm)
                .setPositiveButton(R.string.confirmYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishDelete(curID);
                    }

                })
                .setNegativeButton(R.string.confirmNo, null)
                .show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void finishDelete(int position){
        Toast.makeText(getContext(), "Deleting from favorites...", Toast.LENGTH_LONG).show();
        Restaurant toRemove = this.getItem(position);
        String id = toRemove.getId();

        // remove from file
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
            JSONObject jO = new JSONObject(json.toString());
            jA = jO.getJSONArray("favorites");
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // remove unwanted element
        for(int i = 0; i < jA.length(); i++){
            try {
                JSONObject r = jA.getJSONObject(i);
                if(r.optString("id").equalsIgnoreCase(id)){
                    jA.remove(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // recreate json
        JSONObject newJO = new JSONObject();
        try {
            newJO.put("favorites", jA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FileOutputStream fos;
        try {
            fos =  getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(newJO.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.remove(toRemove);
    }
}
