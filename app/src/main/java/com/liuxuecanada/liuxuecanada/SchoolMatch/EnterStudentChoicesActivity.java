package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.liuxuecanada.liuxuecanada.R;

public class EnterStudentChoicesActivity extends FragmentActivity {
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
                FragmentProgram firstFragment = new FragmentProgram();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            } else if (whichFragment == "languagetest") {
                //animateFade(R.id.arts_button,true);
                FragmentLanguagetest firstFragment = new FragmentLanguagetest();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                FragmentTransaction transactionLanguagetest = getSupportFragmentManager().beginTransaction();
                transactionLanguagetest.replace(R.id.fragment_container, firstFragment);
                transactionLanguagetest.addToBackStack(null);
                transactionLanguagetest.commit();

            }
        }
    }

    private void animateFade(int id, boolean dofadeout){
        Button bt = (Button)findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        bt.startAnimation(fadeout);
    }

    public void clickStudentProgramButton(View view) {
        setFragmentView("languagetest");
    }
}
