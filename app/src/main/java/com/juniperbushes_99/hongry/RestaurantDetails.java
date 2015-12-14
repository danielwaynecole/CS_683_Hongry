package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RestaurantDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantDetails extends Fragment {
    private static final String TAG = "RESTAURANT_DETAILS";
    private String json;
    private JSONObject rObj = null;
    private View view;
    private Restaurant restaurant;
    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestaurantDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantDetails newInstance() {
        RestaurantDetails fragment = new RestaurantDetails();
        return fragment;
    }

    public RestaurantDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString("restaurantData");
        }
        try {
            rObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("title", rObj.optString("name").toString());
        hmap.put("id", rObj.optString("id").toString());
        hmap.put("json", rObj.toString());
        restaurant = new Restaurant(hmap);

        // restaurant name
        TextView nameTV = (TextView) inf.findViewById(R.id.name);
        nameTV.setText(rObj.optString("name"));

        // add onclick to add to favorites button
        Button faveButton = (Button) inf.findViewById(R.id.addToFavorites);
        //Select TextView we want to change the Font
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
        //Set the typeface
        faveButton.setTypeface(font);
        if(!isInFavorites()) {
            faveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        addToFavorites();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            faveButton.setText(R.string.already_in_favorites);
            faveButton.setTextColor(Color.parseColor("#B00000"));
        }

        // set address
        TextView address = (TextView) inf.findViewById(R.id.address);
        JSONObject location = null;
        try {
            location = rObj.getJSONObject("location");
            JSONArray jAddress = location.getJSONArray("display_address");
            StringBuilder displayAddress = new StringBuilder();
            Log.i(TAG, String.valueOf(jAddress.length()));
            for(int i = 0; i < jAddress.length(); i++){
                Log.i(TAG, jAddress.get(i).toString());
                displayAddress.append(jAddress.get(i).toString() + "\n");
            }
            address.setText(displayAddress.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject finalLocation = location;
        address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    JSONObject coordinate = finalLocation.getJSONObject("coordinate");
                    coordinate.put("title", rObj.optString("name"));
                    mListener.onFragmentInteraction("restMap", coordinate.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // set phone number
        TextView phone = (TextView) inf.findViewById(R.id.phone);
        phone.setText(rObj.optString("display_phone"));
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:" + rObj.optString("display_phone")));
                    startActivity(call);
                } catch(Exception e){
                    Log.i(TAG, e.getMessage());
                }
            }
        });

        Bitmap bitmap = null;

        // add image to header
        String restImageURL = rObj.optString("image_url");
        Log.i(TAG, "restaurant image url: " + restImageURL + "\n");
        ImageView restImg = (ImageView) inf.findViewById(R.id.restImage);
        ArrayList<String> imageFetchParams = new ArrayList<String>();
        imageFetchParams.add(restImageURL);
        try {
            InputStream input = new ImageFetch().execute(imageFetchParams).get();
            bitmap = BitmapFactory.decodeStream(input);
            restImg.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // add image to header
        String ratingsImageURL = rObj.optString("rating_img_url_small");
        Log.i(TAG, "ratings image url: " + restImageURL + "\n");
        ImageView ratingsImg = (ImageView) inf.findViewById(R.id.ratingImage);
        ArrayList<String> ratingsImageFetchParams = new ArrayList<String>();
        ratingsImageFetchParams.add(ratingsImageURL);
        try {
            InputStream input = new ImageFetch().execute(ratingsImageFetchParams).get();
            bitmap = BitmapFactory.decodeStream(input);
            ratingsImg.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        view = inf;
        return inf;
    }

    private void addToFavorites() throws JSONException {
        // show toast
        Toast.makeText(getContext(), "Saving to favorites...", Toast.LENGTH_LONG).show();

        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis = getContext().openFileInput(FILENAME);
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

        // add new favorite
        JSONObject newFave = new JSONObject(restaurant.getJson());
        jA.put(newFave);

        // recreate json
        JSONObject newJO = new JSONObject();
        newJO.put("favorites", jA);
        Log.i(TAG, JSONObject.quote(newJO.toString()));
        FileOutputStream fos;
        try {
            fos =  getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(newJO.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isInFavorites() {
        boolean isInFavorites = false;
        // remove from file
        String FILENAME = "restaurant_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis = getContext().openFileInput(FILENAME);
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
        for (int i = 0; i < jA.length(); i++) {
            try {
                JSONObject r = jA.getJSONObject(i);
                if (r.optString("id").equalsIgnoreCase(restaurant.getId())) {
                    isInFavorites = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return isInFavorites;
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
