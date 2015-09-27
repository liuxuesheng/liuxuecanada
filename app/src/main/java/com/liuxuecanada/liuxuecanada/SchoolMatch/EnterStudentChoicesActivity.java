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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentchoicesmain);

        FragmentProgram firstFragment = new FragmentProgram();
        setFragmentView("program");

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
            }else if (whichFragment == "gpa") {
                frag = new FragmentGPA();
            }else if (whichFragment == "gpacalculator") {
                frag = new FragmentGPACalculator();
            }

            frag.setArguments(getIntent().getExtras());
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
        }
    }

    private void animateFade(int id, boolean dofadeout) {
        Button bt = (Button) findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        bt.startAnimation(fadeout);
    }

    public void clickStudentProgramButton(View view) {
        setFragmentView("languagetest");
    }

    public void clickTOEFLButton(View view) {
        setFragmentView("toefl");
    }

    public void clickIELTSButton(View view) {
        setFragmentView("ielts");
    }

    public void clickTOEFLProceedButton(View view) {
        setFragmentView("gpa");
    }

    public void clickIELTSProceedButton(View view) {
        setFragmentView("gpa");
    }
}
