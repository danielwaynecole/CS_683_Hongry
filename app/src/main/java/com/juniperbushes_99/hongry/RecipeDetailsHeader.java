package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsHeader.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsHeader#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsHeader extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "RECIPE_DETAILS_HEAD";
    private static final String IMAGE_URL = "imageURL";
    private static final String TITLE = "title";
    private static final String SERVING_INFO = "servingInfo";
    private static final String ID = "id";
    private static final String ISFAVE = "isFave";


    // TODO: Rename and change types of parameters
    private String title;
    private String imageURL;
    private String id;
    private String servingInfo;
    private boolean isFave;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageURL Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment RecipeDetailsHeader.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsHeader newInstance(String id, String imageURL, String title, String servingInfo, boolean isFave) {
        RecipeDetailsHeader fragment = new RecipeDetailsHeader();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageURL);
        args.putString(TITLE, title);
        args.putString(SERVING_INFO, servingInfo);
        args.putString(ID, id);
        args.putBoolean(ISFAVE, isFave);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDetailsHeader() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ID);
            title = getArguments().getString(TITLE);
            servingInfo = getArguments().getString(SERVING_INFO);
            imageURL = getArguments().getString(IMAGE_URL);
            isFave = getArguments().getBoolean(ISFAVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_recipe_details_header, container, false);
        TextView faveTextView = (TextView) inf.findViewById(R.id.fa_heart_icon);
        //Select TextView we want to change the Font
        Typeface font = Typeface.createFromAsset(inf.getContext().getAssets(), "fontawesome-webfont.ttf");
        //Set the typeface
        faveTextView.setTypeface(font);
        final TextView addToFavorites = (TextView) inf.findViewById(R.id.fa_heart_icon);
        if(!isFave) {
            addToFavorites.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        addToFavorites();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            faveTextView.setText(R.string.already_in_favorites);
            faveTextView.setTextColor(Color.parseColor("#B00000"));
        }


        // add image to header
        Log.i(TAG, "image url: " + imageURL + "\n");
        ImageView img = (ImageView) inf.findViewById(R.id.recipeImage);
        ArrayList<String> imageFetchParams = new ArrayList<String>();
        imageFetchParams.add(imageURL);
        Bitmap bitmap = null;
        try {
            InputStream input = new ImageFetch().execute(imageFetchParams).get();
            bitmap = BitmapFactory.decodeStream(input);
            img.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // add title
        TextView titleElement = (TextView) inf.findViewById(R.id.title);
        titleElement.setText(title);

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

    private void addToFavorites() throws JSONException {
        // show toast
        Toast.makeText(getActivity().getApplicationContext(), "Saving to favorites...", Toast.LENGTH_LONG).show();

        String FILENAME = "recipe_favorites";
        StringBuilder json = new StringBuilder();
        JSONArray jA = new JSONArray();
        try {
            FileInputStream fis =  getActivity().openFileInput(FILENAME);
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
        JSONObject newFave = new JSONObject();
        newFave.put("id", id);
        newFave.put("title", title);
        jA.put(newFave);

        // recreate json
        JSONObject newJO = new JSONObject();
        newJO.put("favorites", jA);
        Log.i(TAG, JSONObject.quote(newJO.toString()));
        FileOutputStream fos;
        try {
            fos =  getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(newJO.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
