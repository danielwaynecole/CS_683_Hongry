package com.juniperbushes_99.hongry;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ucoleda on 11/3/15.
 * Fetches an image from a URL for display in app
 */
public class ImageFetch extends AsyncTask<ArrayList<String>, Void, InputStream> {
    private static final String TAG = "ImageFetch";

    @SuppressWarnings("deprecation")
    @SafeVarargs
    @Override
    protected final InputStream doInBackground(ArrayList<String>... params) {
        InputStream input;
        ArrayList<String> args = params[0];
        String imageURL = args.get(0);
        try {
            URL url = new URL(imageURL);
            HttpGet httpRequest;
            httpRequest = new HttpGet(url.toURI());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient
                    .execute(httpRequest);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
            input = b_entity.getContent();
            return input;
        } catch (Exception ex) {
            Log.d(TAG, "image fetch error: " + ex.toString() + "\n");
        }
        return null;
    }
}
