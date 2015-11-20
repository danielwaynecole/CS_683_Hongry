package com.juniperbushes_99.hongry;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ucoleda on 9/23/15.
 */
public abstract class Search<T> extends AsyncTask<ArrayList<String>, Void, String> {
    private static final String TAG = "Search";

    private String key;
    private String response;
    private String message;
    private int responseCode;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();

        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            setResponseCode(httpResponse.getStatusLine().getStatusCode());
            setMessage(httpResponse.getStatusLine().getReasonPhrase());

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                setResponse(convertStreamToString(instream));

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


    public String processResult(final InputStream inputStream) throws IOException {

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
        return getMessage();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
