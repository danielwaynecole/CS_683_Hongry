package com.juniperbushes_99.hongry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

public class SearchResturants extends AppCompatActivity {

    NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resturants);

        String[] displayedValues = {"1 mile", "2 miles", "3 miles", "5 miles", "10 miles", "25 miles"};
        np = (NumberPicker) findViewById(R.id.radiusPicker);
        np.setMinValue(0);
        np.setMaxValue(5);
        np.setDisplayedValues(displayedValues);
        np.setWrapSelectorWheel(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_resturants, menu);
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
