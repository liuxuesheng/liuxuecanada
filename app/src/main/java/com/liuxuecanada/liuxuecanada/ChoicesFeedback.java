package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChoicesFeedback extends Activity {

    public String DEBUGSTRING = "app_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices_feedback);

        Button getFeedBackButton = (Button) findViewById(R.id.getFeedBackButton);

        //allow network access in the main thread for testing purpose
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    public void getFeedBack(View view) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://192.168.0.17/liuxuecanadaserver/index.php?sex=1");

            urlConnection = (HttpURLConnection) url.openConnection();

            //urlConnection.setRequestProperty("sex", "1");
            //OutputStream out = urlConnection.getOutputStream();

            InputStream in = urlConnection.getInputStream();

            String out = readStream(in);
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
    }
}