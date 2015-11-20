package com.juniperbushes_99.hongry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RecipeDetails extends AppCompatActivity {
    private static final String TAG = "RecipeDetails";
    ListView ingredientListView;
    ArrayList<String> ingredientListItems;
    ArrayAdapter<String> ingredientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ingredientListView = (ListView) findViewById(R.id.ingredients);
        ingredientListItems = new ArrayList<String>();
        Bundle bundle = getIntent().getExtras();

        ArrayList<String> params = new ArrayList<String>();

        //Extract the data…
        String id = bundle.getString("recipeID");
        // add keyword to params
        params.add(id);
        String json = null;
        try {
            json = new YummlyDetails().execute(params).get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject jsonRootObject = null;
        JSONObject imageObject = null;
        JSONObject sourceObject = null;

        try {
            jsonRootObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // recipe image
        JSONArray jsonImageArray = jsonRootObject.optJSONArray("images");
        try {
            imageObject = jsonImageArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageURL = imageObject.optString("hostedMediumUrl").toString();
        Log.i(TAG, "image url: "+imageURL+"\n");
        ImageView img = (ImageView) findViewById(R.id.recipeImage);
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

        // build the ingredients listview
        JSONArray ingredients = jsonRootObject.optJSONArray("ingredientLines");
        String ingredientsString = "";
        for(int i=0; i < ingredients.length(); i++) {
            try {
                ingredientListItems.add(ingredients.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ingredientListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientListItems);
        ingredientListView.setAdapter(ingredientListAdapter);

        // set the recipe title
        String title = jsonRootObject.optString("name");
        TextView titleElement = (TextView) findViewById(R.id.title);
        titleElement.setText(title);

        // add onclick listener to directions button
        // recipe image
        try {
            JSONObject jsonSourceObject = jsonRootObject.getJSONObject("source");
            final String sourceURL = jsonSourceObject.optString("sourceRecipeUrl").toString();
            Button directionsButton = (Button) findViewById(R.id.RecipeDetailsIngredientsButton);
            directionsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(sourceURL));
                    startActivity(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
