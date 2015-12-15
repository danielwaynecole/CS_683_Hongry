package com.juniperbushes_99.hongry;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;

/**
 * Created by ucoleda on 11/3/15.
 * Get recipe details from Yummly API
 */
public class YummlyDetails extends Search {
    @SuppressWarnings("deprecation")
    @SafeVarargs
    @Override
    protected final String doInBackground(ArrayList<String>... params) {
        ArrayList<String> args = params[0];
        String id = args.get(0);
        String url = Constants.yummlyAPIEndPoint + "/api/recipe/" + id + "?_app_id="+Constants.yummlyAppID+"&_app_key="+Constants.yummlyKey;
        HttpGet request = new HttpGet(url);
        executeRequest(request);
        return getResponse();
    }
}
