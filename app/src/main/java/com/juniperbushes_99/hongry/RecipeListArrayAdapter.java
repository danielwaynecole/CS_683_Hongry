package com.juniperbushes_99.hongry;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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
 * For recipe favorites list
 */


public class RecipeListArrayAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private RecFaveStart.OnFragmentInteractionListener mListener;
    public RecipeListArrayAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipes, RecFaveStart.OnFragmentInteractionListener mListener) {
        super(context, textViewResourceId, recipes);
        this.mListener = mListener;
        this.recipes = recipes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.recipelistitem, null);
        }

        final Recipe recipe = recipes.get(position);
        final int pos = position;
        if (recipe != null) {
            final String curRecipeID = recipe.getId();
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView delete = (TextView) v.findViewById(R.id.delete);
            if (title != null) {
                title.setText(recipe.getTitle());
                title.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecipeDetails(curRecipeID);
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

    private void showRecipeDetails(String id){
        // load details
        mListener.onFragmentInteraction("recDetails", id);
    }

    private void deleteFavorite(int position){
        final int curID = position;
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.deleteRecipeFaveConfirmTitle)
                .setMessage(R.string.deleteRecipeFaveConfirm)
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
        Recipe toRemove = this.getItem(position);
        String id = toRemove.getId();

        // remove from file
        String FILENAME = "recipe_favorites";
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
        } catch (IOException | JSONException e) {
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
