package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

public class ChoicesFeedbackActivity extends Activity implements AsyncResponse {
    public String DEBUGSTRING = "app_debug";
    private TextView textViewButton;
    private ServerResponse pud;
    private ProgressBar spinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices_feedback);

        Button feedBackButton = (Button) findViewById(R.id.getFeedBackButton);
        textViewButton = (TextView) findViewById(R.id.getTextViewButton);

        getSpinner().setVisibility(View.GONE);

        //allow network access in the main thread for testing purpose
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
    }

    private ProgressBar getSpinner() {
        if (this.spinner == null)
            spinner = (ProgressBar) findViewById(R.id.progressBar);
        return this.spinner;
    }

    @Override
    public void onTaskComplete(Object output) {
        getSpinner().setVisibility(View.GONE);
        textViewButton.setText((String) output);
    }

    @Override
    public void onTaskStart() {
        getSpinner().setVisibility(View.VISIBLE);
        textViewButton.setText("Processing");
    }

    public void display(View view) {
        pud = new ServerResponse(ChoicesFeedbackActivity.this);
        pud.execute("hi");
    }
}