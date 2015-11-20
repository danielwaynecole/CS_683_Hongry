package com.juniperbushes_99.hongry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchRecipes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Spinner spinner = (Spinner)findViewById(R.id.cuisineSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        for(int i = 0; i < Constants.yummlyCuisines.length; i++){
            String cuisine = Constants.yummlyCuisines[i];
            spinnerAdapter.add(cuisine);
        }
        spinnerAdapter.notifyDataSetChanged();

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_recipes, menu);
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

    public void searchRecipesGo(View view){
        EditText keyword = (EditText) findViewById(R.id.recipeKeyword);

        ArrayList<String> params = new ArrayList<String>();

        // add keyword to params
        String k = keyword.getText().toString();
        params.add(k);

        // add search radius
        Spinner cuisinePicker = (Spinner) findViewById(R.id.cuisineSpinner);
        String c = cuisinePicker.getSelectedItem().toString();
        params.add(String.valueOf(c));
        try {
            String json = new YummlySearch().execute(params).get().toString();
            Intent i = new Intent(this, ListRecipes.class);

            //Create the bundle
            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putString("recipeList", json);

            //Add the bundle to the intent
            i.putExtras(bundle);

            //Fire that second activity
            startActivity(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
