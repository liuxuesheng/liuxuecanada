package com.liuxuecanada.liuxuecanada.Utils;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerResponse extends AsyncTask<String, Object, Object> {
    public String DEBUGSTRING = "app_debug";
    private AsyncResponse delegate = null;
    private String out = "";

    public ServerResponse(AsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        delegate.onTaskStart();
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPreExecute();
        Log.d(DEBUGSTRING, " POST " + result);
        delegate.onTaskComplete(result);
    }

    @Override
    protected String doInBackground(String... urls) {
        /*StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            out = readStream(in);
            Log.d(DEBUGSTRING, " " + out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    private String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
