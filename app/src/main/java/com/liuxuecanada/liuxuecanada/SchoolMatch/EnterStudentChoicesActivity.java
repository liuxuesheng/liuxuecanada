package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener, FragmentTop.OnProgressCirclePageUpdateListener {

    private Fragment fragTop = null;
    private Fragment fragTutorial = null;
    private Fragment fragBottom = null;
    private Fragment frag = null;
    private String currentFragment = null;
    private String nextFragment = null;
    private String previousFragment = null;
    private Button proceedButton = null;
    private TextView topText = null;
    private boolean disabledButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studentchoicesmain);

        if ((findViewById(R.id.fragment_container) != null) && (findViewById(R.id.fragment_top_container) != null) && (findViewById(R.id.fragment_bottom_container) != null)) {
            frag = new FragmentProgram();
            fragTop = new FragmentTop();
            fragBottom = new FragmentBottom();
            frag.setArguments(getIntent().getExtras());
            fragTop.setArguments(getIntent().getExtras());
            fragBottom.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_top_container, fragTop).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_bottom_container, fragBottom).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).addToBackStack(null).commit();
            setCurrentFragment("program");

        }
    }

    @Override
    public void onBackPressed() {
        if (getPreviousFragment() == null) {
            finish();
        } else {
            String fragment = getPreviousFragment();
            setFragmentView(fragment, false);
            setCurrentFragment(fragment);
            updateTitleText(fragment);
        }
    }

    public void updateProceedButton() {
        proceedButton = (Button) findViewById(R.id.proceed_studentchoices_button);
        proceedButton.setBackgroundColor(getResources().getColor(R.color.Green500));
        disabledButton = false;
    }

    public void updateTitleText(String fragmentName) {
        String chineseName = null;
        if (fragmentName == "program") {
            chineseName = "专业方向";
        } else if (fragmentName == "languagetest") {
            chineseName = "语言考试";
        } else if (fragmentName == "toefl") {
            chineseName = "托福";
        } else if (fragmentName == "ielts") {
            chineseName = "雅思";
        } else if (fragmentName == "gpa") {
            chineseName = "GPA成绩";
        } else if (fragmentName == "gpacalculator") {
            chineseName = "GPA计算器";
        }
        topText = (TextView) findViewById(R.id.topTextString);
        topText.setText(chineseName);
    }

    public void updateProgressCircle() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbarid);
        progressBar.setProgress(3);
        TextView textProgress = (TextView) findViewById(R.id.textView1);
        textProgress.setText("2/5");
    }

    private void setFragmentView(String whichFragment, Boolean forward) {
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
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (forward)
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
            else
                transaction.setCustomAnimations(R.anim.popenter, R.anim.popexit);
            transaction.replace(R.id.fragment_container, frag).addToBackStack(null).commit();
        }
    }

    private void addTutorialFragment() {
        if (fragTutorial == null) {
            fragTutorial = new FragmentTutorial();
            fragTutorial.setArguments(getIntent().getExtras());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutorial_container, fragTutorial).addToBackStack(null).commit();
    }

    private void removeTutorialFragment() {
        Fragment fragment = (FragmentTutorial) getSupportFragmentManager().findFragmentById(R.id.fragment_tutorial_container);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        fragTutorial = null;
    }

    private void animateFade(int id, boolean dofadeout) {
        Button bt = (Button) findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.exit);
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

    private void setBlurBackground() {
        View beneathView = findViewById(R.id.fragment_main_container);
        View blurView = findViewById(R.id.fragment_tutorial_container);
        BlurDrawable blurDrawable = new BlurDrawable(beneathView, 40);
        blurView.setBackground(blurDrawable);
    }

    public void clickProceedButton(View view) {
        if (disabledButton) {
            if (fragTutorial == null) {
                addTutorialFragment();
                setBlurBackground();
            }
        } else {
            proceedButton.setBackgroundColor(getResources().getColor(R.color.Grey500));
            String fragment = getNextFragment();
            setFragmentView(fragment, true);
            setCurrentFragment(fragment);
            updateTitleText(fragment);
            disabledButton = true;
        }
    }

    public void clickTutorialButton(View view) {
        View blurView = findViewById(R.id.fragment_tutorial_container);
        blurView.setBackgroundResource(0);
        removeTutorialFragment();
    }

    public void clickProgramButton(View view) {
        updateProceedButton();
    }

    public void clickLanguageTestButton(View view) {
        updateProceedButton();
    }
}
