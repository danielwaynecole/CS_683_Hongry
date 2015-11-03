package com.juniperbushes_99.hongry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.BufferedHttpEntity;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);//Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String id = bundle.getString("recipeID");
        YummlySearch ys = new YummlySearch();
        String json = ys.getRecipeDetails(id);
        JSONObject jsonRootObject = null;
        JSONObject imageObject = null;
        try {
            jsonRootObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonImageArray = jsonRootObject.optJSONArray("images");
        try {
            imageObject = jsonImageArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageURL = imageObject.optString("hostedMediumUrl").toString();
        Log.i(TAG, "image url: "+imageURL+"\n");
        ImageView img = (ImageView) findViewById(R.id.recipeImage);
        try {
            URL url = new URL(imageURL);
            //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
            HttpGet httpRequest = null;

            httpRequest = new HttpGet(url.toURI());

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient
                    .execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
            InputStream input = b_entity.getContent();

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            img.setImageBitmap(bitmap);

        } catch (Exception ex) {

        }
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
        String title = jsonRootObject.optString("name");
        TextView titleElement = (TextView) findViewById(R.id.title);
        titleElement.setText(title);
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
