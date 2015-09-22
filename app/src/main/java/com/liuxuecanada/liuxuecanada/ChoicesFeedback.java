package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChoicesFeedback extends Activity {
    private TextView textViewButton;
    private ProcessUserData pud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices_feedback);

        Button feedBackButton = (Button) findViewById(R.id.getFeedBackButton);
        textViewButton = (TextView) findViewById(R.id.getTextViewButton);

        //allow network access in the main thread for testing purpose
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        pud = new ProcessUserData();
        pud.execute("hi");
    }

    public void display(View view){
        textViewButton.setText(pud.getFeedbackData());
    }
}