package com.juniperbushes_99.hongry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import android.util.Log;
import android.os.AsyncTask;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by ucoleda on 9/23/15.
 */
public class YummlySearch extends AsyncTask<ArrayList<String>, Void, String> {

    private static final String TAG = "YelpSearch";
    private int responseCode;
    private String message;

    private String response;

    public YummlySearch(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {
        ArrayList<String> args = params[0];
        String keyword = args.get(0);
        String cuisine = args.get(1);
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
            apiRequest = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getRecipeList(ArrayList<String>... params){
        return "";
    }

    public String getRecipeDetails(String id){
        String details = "";
        String url = Constants.yummlyAPIEndPoint + "/api/recipe/" + id + "?_app_id="+Constants.yummlyAppID+"&_app_key="+Constants.yummlyKey;
        HttpGet request = new HttpGet(url);
        executeRequest(request, url);
        return response;
    }

    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();

        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
