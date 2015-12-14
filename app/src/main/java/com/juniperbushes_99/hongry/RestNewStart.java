package com.juniperbushes_99.hongry;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import android.location.LocationListener;
import android.content.IntentSender;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestNewStart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestNewStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestNewStart extends Fragment implements com.google.android.gms.location.LocationListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "SearchRestaurants";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    private LocationRequest mLocationRequest;
    NumberPicker np;
    private double latitude;
    private double longitude;
    private View view;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestNewStart.
     */
    public static RestNewStart newInstance() {
        RestNewStart fragment = new RestNewStart();
        return fragment;
    }

    public RestNewStart() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_rest_new_start, container, false);
        String[] displayedValues = {"1 mile", "2 miles", "3 miles", "5 miles", "10 miles", "25 miles"};
        np = (NumberPicker) inf.findViewById(R.id.radiusPicker);
        np.setMinValue(0);
        np.setMaxValue(5);
        np.setDisplayedValues(displayedValues);
        np.setWrapSelectorWheel(false);
        // Google location services
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        final Button eatOutSearchStartButton = (Button) inf.findViewById(R.id.searchRestaurantsButton);
        eatOutSearchStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchRestaurantsGo();
            }
        });
        view = inf;
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

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.i(TAG, "location is null");
            final TextView addressLabel = (TextView) view.findViewById(R.id.addressLabel);
            final EditText address = (EditText) view.findViewById(R.id.address);
            addressLabel.setVisibility(view.VISIBLE);
            address.setVisibility(view.VISIBLE);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
        final TextView addressLabel = (TextView) view.findViewById(R.id.addressLabel);
        final EditText address = (EditText) view.findViewById(R.id.address);
        addressLabel.setVisibility(view.VISIBLE);
        address.setVisibility(view.VISIBLE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            /*** Normally I would show this dialog, to update Google Play, for example, but since it's not possible in emulator, disabling
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    connectionResult.getErrorCode(),
                    getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            errorDialog.show();
            */
            final TextView addressLabel = (TextView) view.findViewById(R.id.addressLabel);
            final EditText address = (EditText) view.findViewById(R.id.address);
            addressLabel.setVisibility(view.VISIBLE);
            address.setVisibility(view.VISIBLE);

            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode() + ". Message: " + connectionResult.getErrorCode());
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
        Log.i(TAG, "location is null");
        final TextView addressLabel = (TextView) view.findViewById(R.id.addressLabel);
        final EditText address = (EditText) view.findViewById(R.id.address);
        addressLabel.setVisibility(view.VISIBLE);
        address.setVisibility(view.VISIBLE);
    }

    private void handleNewLocation(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    };
    public void searchRestaurantsGo(){
        EditText keyword = (EditText) view.findViewById(R.id.restaurantSearchKeywordInput);
        String k = keyword.getText().toString();
        EditText address = (EditText) view.findViewById(R.id.address);
        String a = address.getText().toString();

        // add search radius
        NumberPicker radiusPicker = (NumberPicker) view.findViewById(R.id.radiusPicker);
        int r = radiusPicker.getValue();

        ArrayList<String> params = new ArrayList<String>();
        params.add(k);
        params.add(String.valueOf(r));
        params.add(String.valueOf(latitude));
        params.add(String.valueOf(longitude));
        params.add(a);

        try {
            String json = new YelpSearch().execute(params).get();
            mListener.onFragmentInteraction("restList", json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
