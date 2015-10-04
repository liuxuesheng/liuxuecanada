package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener, FragmentTop.OnProgressCirclePageUpdateListener, FragmentProgram.OnTextColorUpdateListener {

    int color = 0;
    Bitmap bm = null;
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
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

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

        LinearLayout layout = (LinearLayout) findViewById(R.id.fragment_main_container);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.fragment_container);

/*
        int width = layout.getWidth();
        int height = layout.getHeight();
*/

        int width = 150;
        int height = 300;

        Log.d("asdasdas"," "+width+" "+height);

        bm = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        LinearGradient linearGradient = new LinearGradient(0, 0, width, height, new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.40f, 0.60f, 1}, Shader.TileMode.REPEAT);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        paint.setDither(true);
        canvas.drawRect(0, 0, width, height, paint);

        layout.setBackground(new BitmapDrawable(getResources(), bm));

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
        proceedButton.setTextColor(getResources().getColor(R.color.Red500));
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
        progressBar.setProgress(60);
        TextView textProgress = (TextView) findViewById(R.id.textView1);
        textProgress.setText("3/5");
    }

    public int updateTextColor() {
        int myx = 100;
        int myy = 200;

        Log.d("asdasdas"," "+myx+" "+myy);

        color = bm.getPixel( myx, myy);

        Log.d("asdasdas"," "+color+" !"+ Color.RED);
        return color;

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
            proceedButton.setTextColor(getResources().getColor(R.color.white));
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
