package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;

import java.util.LinkedList;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener, FragmentTop.OnProgressCirclePageUpdateListener, FragmentProgram.OnTextColorUpdateListener, ViewTreeObserver.OnGlobalLayoutListener {

    Bitmap bm = null;
    LinearLayout layout = null;
    LinkedList<TextView> ll = null;
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

        layout = (LinearLayout) findViewById(R.id.fragment_main_container);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        int width = layout.getMeasuredWidth();
        int height = layout.getMeasuredHeight();
        Log.d("asdasdas", " " + width + " " + height);

        bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        LinearGradient linearGradient = new LinearGradient(0, 0, width, height, new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.33f, 0.66f, 1}, Shader.TileMode.REPEAT);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        paint.setDither(true);
        canvas.drawRect(0, 0, width, height, paint);

        layout.setBackground(new BitmapDrawable(getResources(), bm));

        ll = new LinkedList<TextView>();


        LinkedList<TextView> tvlist = findAllTextView(layout);
        Log.d("asdasdas", " ll " + ll.size());

        for (TextView tv : tvlist) {
            int[] k = getViewCoordinates(tv);
            Log.d("asdasdas", " k0 " + k[0] + " k1 " + k[1]);

            int convertedColor = convertColor(bm.getPixel(k[0], k[1]));

            ((TextView) tv).setTextColor(convertedColor);
        }
    }

    public void updateColor() {
        ll = new LinkedList<TextView>();


        LinkedList<TextView> tvlist = findAllTextView(layout);
        Log.d("asdasdas", " ll " + ll.size());

        for (TextView tv : tvlist) {
            int[] k = getViewCoordinates(tv);
            Log.d("asdasdas", " k0 " + k[0] + " k1 " + k[1]);

            int convertedColor = convertColor(bm.getPixel(k[0], k[1]));

            ((TextView) tv).setTextColor(convertedColor);
        }
    }

    private LinkedList<TextView> findAllTextView(ViewGroup viewgroup) {
        int count = viewgroup.getChildCount();

        for (int i = 0; i < count; i++) {
            View view = viewgroup.getChildAt(i);
            if (view instanceof ViewGroup)
                findAllTextView((ViewGroup) view);
            else if (view instanceof TextView)
                ll.addLast((TextView) view);
        }

        return ll;
    }

    private int[] getViewCoordinates(View view) {
        View globalView = findViewById(R.id.fragment_main_container);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int topOffset = dm.heightPixels - globalView.getMeasuredHeight();

        // the view to locate
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        int centreX = (int) (view.getWidth() / 2);
        int centreY = (int) (view.getHeight() / 2);

        int x = loc[0] + centreX;
        int y = loc[1] - topOffset + centreY;
        Log.d("asdasdas", " loc0 " + loc[0] + " loc1 " + loc[1]);
        Log.d("asdasdas", " x " + x + " y " + y + " offset " + topOffset);
        Log.d("asdasdas", " centreX " + centreX + " centreY " + centreY + " " + view.getHeight() + " " + view.getY());
        return new int[]{x, y};

    }

    private int convertColor(int colorCode) {
        int red = Color.red(colorCode);
        int green = Color.green(colorCode);
        int blue = Color.blue(colorCode);
        float[] hsv = new float[3];

        Color.RGBToHSV(red, green, blue, hsv);
        Log.d("asdasdas", "Before H " + hsv[0] + " S " + hsv[1] + " V " + hsv[2]);

        if (hsv[2] < 0.6f) {
            hsv[2] = 1.0f;
        } else {
            hsv[0] = (hsv[0] - 10.0f) < 0 ? hsv[0] + 350.0f : hsv[0] - 10.0f;
            hsv[1] = hsv[1] > 0.5 ? hsv[1] - 0.3f : hsv[1] + 0.3f;
            hsv[2] = 1.0f;
        }

        Log.d("asdasdas", "After H " + hsv[0] + " S " + hsv[1] + " V " + hsv[2]);

        return Color.HSVToColor(hsv);
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

    public void updateTextColor() {


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
            //updateTitleText(fragment);
            disabledButton = true;
            //updateColor();
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
