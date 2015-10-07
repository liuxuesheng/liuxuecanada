package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import java.util.ArrayList;
import java.util.List;

public class ChoicesFeedbackActivity extends Activity implements AsyncResponse {
    public String DEBUGSTRING = "app_debug";
    private TextView textViewButton;
    private ServerResponse pud;
    private ProgressBar spinner = null;

    // cardview testing
    private RecyclerView recyclerView;
    private List<ChoicesFeedbackItem> choicesFeedbackItems;
    private FeedbackViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicesfeedback);

        Button feedBackButton = (Button) findViewById(R.id.getFeedBackButton);
        textViewButton = (TextView) findViewById(R.id.getTextViewButton);

        getSpinner().setVisibility(View.GONE);

        //allow network access in the main thread for testing purpose
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView= (RecyclerView) findViewById(R.id.choices_feedback_View);

        initPersonData();
        adapter=new FeedbackViewAdapter(choicesFeedbackItems,ChoicesFeedbackActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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

    private void initPersonData() {
        choicesFeedbackItems =new ArrayList<>();

        choicesFeedbackItems.add(new ChoicesFeedbackItem("This is 1 feedback", "Click me please!",R.drawable.feedback1));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("This is 2 feedback", "Click me please!",R.drawable.feedback2));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("This is 3 feedback", "Click me please!",R.drawable.feedback3));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("This is 4 feedback", "Click me please!",R.drawable.feedback4));

    }

}