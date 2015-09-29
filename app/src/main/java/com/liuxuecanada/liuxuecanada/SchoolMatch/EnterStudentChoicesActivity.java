package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

public class EnterStudentChoicesActivity extends FragmentActivity {

    private Fragment frag = null;
    private String currentFragment = null;
    private String nextFragment = null;
    private String nextTitle = null;
    private Button proceedButton = null;
    private TextView tv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentchoicesmain);

        proceedButton = (Button) findViewById(R.id.proceed_studentchoices_button);
        setTitleText("选择方向");

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
                frag.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).commit();
                setCurrentFragment(whichFragment);
            } else {
                if (whichFragment == "languagetest") {
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
                setCurrentFragment(whichFragment);
            }
        }
    }

    private void animateFade(int id, boolean dofadeout) {
        Button bt = (Button) findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        bt.startAnimation(fadeout);
    }

    private void setTitleText(String titleText){
        if (this.tv == null)
            tv = (TextView) findViewById(R.id.topTextString);
        Log.d("adg ", titleText);
        tv.setText(titleText);
    }

    private void setCurrentFragment(String currentFragment){
        this.currentFragment = currentFragment;
    }

    private String getCurrentFragment(){
        return this.currentFragment;
    }

    private String getNextTitle(){
        if (getCurrentFragment() == "program")
            nextFragment = "选择语言考试";
        else if(getCurrentFragment() == "languagetest")
            nextFragment = "雅思成绩";
        else if(getCurrentFragment() == "toefl")
            nextFragment = "GPA数值";
        else if(getCurrentFragment() == "ielts")
            nextFragment = "GPA数值";
        else if(getCurrentFragment() == "gpa")
            nextFragment = "GPA计算器";
        return this.nextFragment;
    }

    private String getNextFragment(){
        if (getCurrentFragment() == "program")
            nextTitle = "languagetest";
        else if(getCurrentFragment() == "languagetest")
            nextTitle = "ielts";
        else if(getCurrentFragment() == "toefl")
            nextTitle = "gpa";
        else if(getCurrentFragment() == "ielts")
            nextTitle = "gpa";
        else if(getCurrentFragment() == "gpa")
            nextTitle = "gpacalculator";
        return this.nextTitle;
    }

    public void clickProceedButton(View view){
        proceedButton.setVisibility(View.INVISIBLE);
        setTitleText(getNextTitle());
        setFragmentView(getNextFragment());
    }

    public void clickProgramButton(View view){
        proceedButton.setVisibility(View.VISIBLE);
    }

    public void clickLanguageTestButton(View view){
        proceedButton.setVisibility(View.VISIBLE);
    }
}
