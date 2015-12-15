package com.juniperbushes_99.hongry;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import android.util.Log;



/**
 * Created by ucoleda on 9/23/15.
 * Search Yummly for recipes
 */
public class YummlySearch extends Search {

    private static final String TAG = "YelpSearch";

    @SafeVarargs
    @Override
    protected final String doInBackground(ArrayList<String>... params) {
        ArrayList<String> args = params[0];
        String keyword = args.get(0);
        try {
            keyword = URLEncoder.encode(keyword, Charset.forName("UTF-8").name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String json = "";
        // create an HTTP request to a protected resource
        String urlString = Constants.yummlyAPIEndPoint + "/api/recipes?_app_id="+Constants.yummlyAppID+"&_app_key="+Constants.yummlyKey+"&q="+keyword;
        Log.i(TAG, urlString);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection apiRequest = null;
        try {
            assert url != null;
            apiRequest = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Sending request...");
        try {
            assert apiRequest != null;
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
}
