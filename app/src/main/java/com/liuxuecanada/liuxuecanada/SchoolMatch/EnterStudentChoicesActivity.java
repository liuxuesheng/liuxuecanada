package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.CharacterDrawableComponent.CharacterDrawable;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ContentItem;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ListAdapter;
import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;
import com.liuxuecanada.liuxuecanada.Utils.ComponentsInViewService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;
import com.liuxuecanada.liuxuecanada.Utils.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener,
        FragmentTop.OnProgressCirclePageUpdateListener,
        FragmentAcdemicType.OnFragmentCreatedListener,
        FragmentProgram.OnFragmentCreatedListener,
        FragmentLanguageTest.OnFragmentCreatedListener,
        ViewTreeObserver.OnGlobalLayoutListener,
        AsyncResponse {

    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    private final String mainURL = "http://10.135.50.41/liuxuecanadaserver/tests/test1/index.php?page=";
    LinearLayout layout = null;
    LinkedList<JSONArray> pagell = null;
    JSONArray jsonArray3 = null;
    JSONObject jObj = null;
    JSONArray arr = null;
    private LinkedList<String> allFlowItemNames = null;
    private Fragment fragTutorial = null;
    private Fragment frag = null;
    private String currentFragment = null;
    private Button proceedButton = null;
    private TextView topText = null;
    private boolean disabledButton = true;

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);
            ComponentsInViewService.addObjectsToView(arr, this, mainURL);

            if (pagell == null)
                pagell = new LinkedList<JSONArray>();

            pagell.addLast(arr);

            PaintService.setTextPainted(false);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintService.setBackgroundPainted(false);
        PaintService.setTextPainted(false);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        allFlowItemNames = new LinkedList<>();

        setContentView(R.layout.flow_main);

        ServerResponse pud = new ServerResponse(this);
        pud.execute(mainURL + 1);

/*        if ((findViewById(R.id.fragment_container) != null) && (findViewById(R.id.fragment_top_container) != null) && (findViewById(R.id.fragment_bottom_container) != null)) {
            frag = new FragmentAcdemicType();
            fragTop = new FragmentTop();
            fragBottom = new FragmentBottom();
            frag.setArguments(getIntent().getExtras());
            fragTop.setArguments(getIntent().getExtras());
            fragBottom.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_top_container, fragTop).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_bottom_container, fragBottom).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag).addToBackStack(null).commit();
            setCurrentFragment("acdemicstudy");
            allFlowItemNames.addLast("acdemicstudy");
        }*/

        layout = (LinearLayout) findViewById(R.id.fragment_main_container);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(this);


        RelativeLayout middleLayout = (RelativeLayout) findViewById(R.id.fragment_container);
        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        Drawable d = ContextCompat.getDrawable(this, R.drawable.circular);


        //Drawable drawable = new CharacterDrawable('A', 0xFF805781, this);

        //button.setBackgroundDrawable(drawable);

        objects.add(new ContentItem("school 1", PaintService.paintTextIconDrawable(this, "S")));
        objects.add(new ContentItem("school 2", PaintService.paintTextIconDrawable(this, "A")));
        objects.add(new ContentItem("school 3", PaintService.paintTextIconDrawable(this, "B")));
        objects.add(new ContentItem("school 4", PaintService.paintTextIconDrawable(this, "C")));
        objects.add(new ContentItem("school 5", PaintService.paintTextIconDrawable(this, "D")));
        objects.add(new ContentItem("school 6", PaintService.paintTextIconDrawable(this, "E")));
        objects.add(new ContentItem("school 7", d));
        objects.add(new ContentItem("school 2", PaintService.paintTextIconDrawable(this, "A")));
        objects.add(new ContentItem("school 3", PaintService.paintTextIconDrawable(this, "B")));
        objects.add(new ContentItem("school 4", PaintService.paintTextIconDrawable(this, "C")));
        objects.add(new ContentItem("school 5", PaintService.paintTextIconDrawable(this, "D")));
        objects.add(new ContentItem("school 6", PaintService.paintTextIconDrawable(this, "E")));
        objects.add(new ContentItem("school 2", PaintService.paintTextIconDrawable(this, "A")));
        objects.add(new ContentItem("school 3", PaintService.paintTextIconDrawable(this, "B")));
        objects.add(new ContentItem("school 4", PaintService.paintTextIconDrawable(this, "C")));
        objects.add(new ContentItem("school 5", PaintService.paintTextIconDrawable(this, "D")));
        objects.add(new ContentItem("school 6", PaintService.paintTextIconDrawable(this, "E")));
        objects.add(new ContentItem("school 2", PaintService.paintTextIconDrawable(this, "A")));
        objects.add(new ContentItem("school 3", PaintService.paintTextIconDrawable(this, "B")));
        objects.add(new ContentItem("school 4", PaintService.paintTextIconDrawable(this, "C")));
        objects.add(new ContentItem("school 5", PaintService.paintTextIconDrawable(this, "D")));
        objects.add(new ContentItem("school 6", PaintService.paintTextIconDrawable(this, "E")));


        ListAdapter adapter = new ListAdapter(this, objects);
        ListView lv = new ListView(this);

        lv.setAdapter(adapter);
        middleLayout.addView(lv);

    }

    private WheelView createWheelView() {
        WheelView wv = new WheelView(this);
        wv.setItems(Arrays.asList(PLANETS));
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            }
        });
        return wv;
    }


    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        if (PaintService.getBackgroundPainted() == false) {
            PaintService.paintBackground(this, layout);
            PaintService.setBackgroundPainted(true);
        }

/*        if (PaintService.getTextPainted() == false) {
            PaintService.paintText(this, layout);
            PaintService.setTextPainted(true);
        }*/

    }

    public void resetTextColorCount() {
        PaintService.setTextPainted(false);
    }

    @Override
    public void onBackPressed() {
        if (pagell == null || pagell.size() <= 1) {
            finish();
        } else {
            pagell.removeLast();
            ComponentsInViewService.clearAllContainers(this);
            PaintService.setTextPainted(false);
            ComponentsInViewService.addObjectsToView(pagell.getLast(), this, mainURL);

        }
    }

    public void updateProceedButton() {
        proceedButton = (Button) findViewById(R.id.proceed_studentchoices_button);
        proceedButton.setTextColor(getResources().getColor(R.color.Red500));
        disabledButton = false;
    }

    public void updateTitleText(String fragmentName) {
        String chineseName = null;
        if (fragmentName == "acdemicstudy")
            chineseName = "Choose Acdemic Study";
        if (fragmentName == "program") {
            chineseName = "Choose Acdemic Area";
        } else if (fragmentName == "languagetest") {
            chineseName = "Choose Language Test";
        } else if (fragmentName == "toefl") {
            chineseName = "Enter TOEFL Results";
        } else if (fragmentName == "ielts") {
            chineseName = "Enter IELTS Results";
        } else if (fragmentName == "gpa") {
            chineseName = "Enter your GPA";
        } else if (fragmentName == "gpacalculator") {
            chineseName = "Use GPA Calculator";
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
            if (whichFragment == "acdemicstudy")
                frag = new FragmentAcdemicType();
            else if (whichFragment == "program")
                frag = new FragmentProgram();
            else if (whichFragment == "languagetest")
                frag = new FragmentLanguageTest();
            else if (whichFragment == "toefl")
                frag = new FragmentTOEFL();
            else if (whichFragment == "ielts")
                frag = new FragmentIELTS();
            else if (whichFragment == "gpa")
                frag = new FragmentGPA();
            else if (whichFragment == "gpacalculator")
                frag = new FragmentGPACalculator();

            frag.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (forward)
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
            else
                transaction.setCustomAnimations(R.anim.popenter, R.anim.popexit);
            transaction.replace(R.id.fragment_container, frag).addToBackStack(null).commit();

            if (forward)
                allFlowItemNames.addLast(whichFragment);
            else
                allFlowItemNames.removeLast();
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

    private String getPreviousFragment(String currentFragment) {
        int index = -1;
        for (int i = 0; i < allFlowItemNames.size(); i++) {
            if (allFlowItemNames.get(i).equals(currentFragment)) {
                index = i;
                break;
            }
        }

        if (index == -1 || index == 0)
            return null;
        else
            return allFlowItemNames.get(index - 1);
    }

    private String getNextFragment(String currentFragment) {
        int index = -1;
        for (int i = 0; i < allFlowItemNames.size(); i++) {
            if (allFlowItemNames.get(i).equals(currentFragment)) {
                index = i;
                break;
            }
        }

        if (index == -1 || index == allFlowItemNames.size() - 1)
            return null;
        else
            return allFlowItemNames.get(index + 1);
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
            String fragment = getNextFragment(getCurrentFragment());
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
