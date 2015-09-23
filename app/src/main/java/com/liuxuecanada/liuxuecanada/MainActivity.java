package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;

public class MainActivity extends Activity {

    private Button studentchoices_button;
    private Button student_Previouspage_button;
    private Button student_nextpage_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentchoices_button = (Button) findViewById(R.id.studentchoices_button);

    }

    public void goStudentChoices(View view) {
        Intent myIntent = new Intent(MainActivity.this, EnterStudentChoicesActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goChoicesFeedback(View view) {
        Intent myIntent = new Intent(MainActivity.this, ChoicesFeedbackActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

}
