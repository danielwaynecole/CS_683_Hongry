package com.juniperbushes_99.hongry;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.content.Context;
import android.view.View;
import android.location.Location;
import android.location.LocationListener;
import android.content.IntentSender;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog;

//public class SearchResturants extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class SearchResturants extends AppCompatActivity {
    final Context context = this;
    private double latitude = 42.3100;
    private double longitude = -71.1117;
    private static final String TAG = "SearchRestaurants";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    private LocationRequest mLocationRequest;
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
        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        */
    }

    public void searchRestaurantsGo(View view){
        EditText keyword = (EditText) findViewById(R.id.restaurantSearchKeywordInput);

        ArrayList<String> params = new ArrayList<String>();

        // add keyword to params
        String k = keyword.getText().toString();
        params.add(k);

        // add search radius
        NumberPicker radiusPicker = (NumberPicker) findViewById(R.id.radiusPicker);
        int r = radiusPicker.getValue();
        params.add(String.valueOf(r));
        params.add(String.valueOf(latitude));
        params.add(String.valueOf(longitude));
        try {
            String json = new YelpSearch().execute(params).get();
            Intent i = new Intent(this, ListRestaurants.class);

            //Create the bundle
            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putString("restaurantList", json);

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
    /*
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    connectionResult.getErrorCode(),
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            errorDialog.show();
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode() + ". Message: " + connectionResult.getErrorMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void handleNewLocation(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    };
    */
}
