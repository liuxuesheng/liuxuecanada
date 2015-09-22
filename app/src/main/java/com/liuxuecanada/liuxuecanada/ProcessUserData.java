package com.liuxuecanada.liuxuecanada;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProcessUserData extends AsyncTask<String, Void, String> {
    public String DEBUGSTRING = "app_debug";
    private String out = "";

    protected String doInBackground(String... urls) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://10.135.50.41/liuxuecanadaserver/index.php?sex=1");
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
                e.printStackTrace(); //If you want further info on failure...
            }
        }
        return null;
    }

    protected String getFeedbackData() {
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
