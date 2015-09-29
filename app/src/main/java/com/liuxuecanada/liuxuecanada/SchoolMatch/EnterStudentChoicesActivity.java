package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.liuxuecanada.liuxuecanada.R;

public class EnterStudentChoicesActivity extends FragmentActivity {

    private Fragment frag = null;
    private String currentFragment = null;
    private String nextFragment = null;
    private String previousFragment = null;
    private Button proceedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentchoicesmain);

        if (findViewById(R.id.fragment_container) != null) {
            frag = new FragmentProgram();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).addToBackStack(null).commit();
            setCurrentFragment("program");
        }
    }

    @Override
    public void onBackPressed() {
        if (getPreviousFragment() == null) {
            finish();
        } else {
            setFragmentView(getPreviousFragment());
            setCurrentFragment(getPreviousFragment());
        }
    }

    private void setFragmentView(String whichFragment) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
/*            if (savedInstanceState != null) {
                return;
            }*/
            if (whichFragment == "program") {
                frag = new FragmentProgram();
            } else if (whichFragment == "languagetest") {
                frag = new FragmentLanguageTest();
            } else if (whichFragment == "toefl") {
                frag = new FragmentTOEFL();
            } else if (whichFragment == "ielts") {
                frag = new FragmentIELTS();
            } else if (whichFragment == "gpa") {
                frag = new FragmentGPA();
            } else if (whichFragment == "gpacalculator") {
                frag = new FragmentGPACalculator();
            }
            frag.setArguments(getIntent().getExtras());
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
            //addToBackStack(null)

        }
    }

    private void animateFade(int id, boolean dofadeout) {
        Button bt = (Button) findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        bt.startAnimation(fadeout);
    }

    private String getCurrentFragment() {
        return this.currentFragment;
    }

    private void setCurrentFragment(String fragment) {
        this.currentFragment = fragment;
    }

    private String getPreviousFragment() {
        if (getCurrentFragment() == "program")
            previousFragment = null;
        else if (getCurrentFragment() == "languagetest")
            previousFragment = "program";
        else if (getCurrentFragment() == "toefl")
            previousFragment = "languagetest";
        else if (getCurrentFragment() == "ielts")
            previousFragment = "languagetest";
        else if (getCurrentFragment() == "gpa")
            previousFragment = "languagetest";
        return this.previousFragment;
    }

    private String getNextFragment() {
        if (getCurrentFragment() == "program")
            nextFragment = "languagetest";
        else if (getCurrentFragment() == "languagetest")
            nextFragment = "ielts";
        else if (getCurrentFragment() == "toefl")
            nextFragment = "gpa";
        else if (getCurrentFragment() == "ielts")
            nextFragment = "gpa";
        else if (getCurrentFragment() == "gpa")
            nextFragment = "gpacalculator";
        return this.nextFragment;
    }

    public void clickProceedButton(View view) {
        proceedButton.setVisibility(View.INVISIBLE);
        setFragmentView(getNextFragment());
        setCurrentFragment(getNextFragment());
    }

    public void clickProgramButton(View view) {
        proceedButton = (Button) findViewById(R.id.proceed_studentchoices_button);
        proceedButton.setVisibility(View.VISIBLE);
    }

    public void clickLanguageTestButton(View view) {
        proceedButton = (Button) findViewById(R.id.proceed_studentchoices_button);
        proceedButton.setVisibility(View.VISIBLE);
    }
}
