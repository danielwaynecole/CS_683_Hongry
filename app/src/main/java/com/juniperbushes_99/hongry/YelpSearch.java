package com.juniperbushes_99.hongry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ucoleda on 9/23/15.
 */
public class YelpSearch extends AsyncTask<ArrayList<String>, Void, String> {
    private static final String TAG = "YelpSearch";

    @Override
    protected String doInBackground(ArrayList<String>... params) {
        ArrayList<String> args = params[0];
        String keyword = args.get(0);
        String location = args.get(4);
        String queryString = null;
        try {
            queryString = "search?term=" + URLEncoder.encode(keyword, Charset.forName("UTF-8").name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            double radiusInMeters = Integer.parseInt(args.get(1)) * 1609.344;
            double radius = (radiusInMeters > 40000) ? 40000 : radiusInMeters;
            double latitude = Double.parseDouble(args.get(2));
            double longitude = Double.parseDouble(args.get(3));
            if(!(latitude == 0 && longitude == 0)) {
                queryString += "&ll=" + latitude + "," + longitude + "&radius_filter=" + radius;
            } else {
                try {
                    queryString += "&location=" + URLEncoder.encode(location, Charset.forName("UTF-8").name());
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        } catch(NumberFormatException e){
            try {
                queryString += "&location=" + URLEncoder.encode(location, Charset.forName("UTF-8").name());
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        String json = "";

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        // create a consumer object and configure it with the access
        // token and token secret obtained from the service provider
        OAuthConsumer consumer = new DefaultOAuthConsumer(Constants.yelpKey,
                Constants.yelpSecret);
        consumer.setTokenWithSecret(Constants.yelpToken, Constants.yelpTokenSecret);

        // create an HTTP request to a protected resource
        String urlString = Constants.yelpAPIEndPoint + queryString;
        Log.i(TAG, urlString);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection apiRequest = null;
        try {
            apiRequest = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            consumer.sign(apiRequest);
        } catch (OAuthMessageSignerException e) {
            Log.i(TAG, e.toString());
        } catch (OAuthExpectationFailedException e) {
            Log.i(TAG, e.toString());
        } catch (OAuthCommunicationException e) {
            Log.i(TAG, e.toString());
        }
        Log.i(TAG, "Sending request...");
        try {
            apiRequest.connect();
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        }
        Log.i(TAG, "Expiration " + apiRequest.getExpiration());
        Log.i(TAG, "Timeout " + apiRequest.getConnectTimeout());
        Log.i(TAG, "URL " + apiRequest.getURL());
        Log.i(TAG, "Method " + apiRequest.getRequestMethod());
        try {
            Log.i(TAG, "Response: " + apiRequest.getResponseCode() + " " + apiRequest.getResponseMessage());
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        }
        try {
            if (apiRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = null; try {

                    inputStream = apiRequest.getInputStream();

                    json = processResult(inputStream);

                }catch (IOException e) {

                    Log.i(TAG,e.getMessage());

                }

                finally {

                    if (inputStream != null) {

                        inputStream.close();

                    }

                }

            }
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
        return json;
    }

    private String processResult(final InputStream inputStream) throws IOException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader( inputStream, Charset.forName("UTF-8")), 8192);

        String line = "";
        String json = "";
        while ((line = reader.readLine()) != null) {
            json += line;
            Log.i(TAG, line);

        }
        return json;
    }
}
