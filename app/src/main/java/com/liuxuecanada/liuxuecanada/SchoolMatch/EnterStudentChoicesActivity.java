package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.EvaluationFormData.coreEvaluation;
import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;
import com.liuxuecanada.liuxuecanada.Utils.JSONService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener,
        FragmentTop.OnProgressCirclePageUpdateListener,
        FragmentAcdemicType.OnFragmentCreatedListener,
        FragmentProgram.OnFragmentCreatedListener,
        FragmentLanguageTest.OnFragmentCreatedListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintService.setBackgroundPainted(false);
        PaintService.setTextPainted(false);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        allFlowItemNames = new LinkedList<>();
        pagell = new LinkedList<>();

        Log.d("asdasdasize ", " now " + pagell.size());

        setContentView(R.layout.fragment_studentchoices_main);

        createWheelSelector();

        //connect();

        createPage2Objects();

        Log.d("asd3cs23 ", " HERE A");
        //addObjectsToView(arr, R.id.fragment_top_container);
        addObjectsToView(coreEvaluation.createPage1());


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


    }

    public void connect() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String out = "";

        Log.d("sa8ah3n", " " + 19);

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://10.135.50.41/liuxuecanadaserver/index.php");
            Log.d("sa8ah3n", " " + 20);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("sa8ah3n", " " + 21);
            InputStream in = urlConnection.getInputStream();
            Log.d("sa8ah3n", " " + 22);
            out = readStream(in);
            Log.d("sa8ah3n", " " + out);
            arr = new JSONArray();
            jObj = new JSONObject(out);
            arr.put(jObj);

            Log.d("sa8ah3n", " " + 23);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
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


    private void clearMiddleContainer() {
        ((RelativeLayout) findViewById(R.id.fragment_container)).removeAllViews();
    }

    private ProgressBar createProgressBar(int id, int relation, int relationid) {

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);
        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setId(id);
        pb.setLayoutParams(p);
        pb.setBackgroundColor(Color.TRANSPARENT);
        pb.setProgressDrawable(createDrawable(this));
        return pb;
    }

    private Drawable createDrawable(Context context) {

        ShapeDrawable shape = new ShapeDrawable();
        shape.getPaint().setStyle(Paint.Style.FILL);
        shape.getPaint().setColor(
                context.getResources().getColor(R.color.Blue700));

        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(4);
        shape.getPaint().setColor(
                context.getResources().getColor(R.color.Red500));

        ShapeDrawable shapeD = new ShapeDrawable();
        shapeD.getPaint().setStyle(Paint.Style.FILL);
        shapeD.getPaint().setColor(
                context.getResources().getColor(R.color.Grey500));
        ClipDrawable clipDrawable = new ClipDrawable(shapeD, Gravity.LEFT,
                ClipDrawable.HORIZONTAL);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                clipDrawable, shape});
        return layerDrawable;
    }

    private void addObjectsToView(JSONArray jsonArray) {
        RelativeLayout topView = (RelativeLayout) findViewById(R.id.fragment_top_container);
        RelativeLayout middleView = (RelativeLayout) findViewById(R.id.fragment_container);
        RelativeLayout bottomView = (RelativeLayout) findViewById(R.id.fragment_bottom_container);
        RelativeLayout someView;

        LinkedList<View> ll = new LinkedList<>();

        TextView tv = null;
        ProgressBar pb = null;
        WheelSelector ws = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject item = jsonArray.getJSONObject(i);
                if (item.getString("inlayout").equals("middle"))
                    someView = middleView;
                else if (item.getString("inlayout").equals("top"))
                    someView = topView;
                else if (item.getString("inlayout").equals("bottom"))
                    someView = bottomView;
                else
                    break;

                if (item.getString("type").equals("textview")) {
                    tv = JSONService.createTextView(item, this);
                    /*
                            tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMiddleContainer();
                addObjectsToView(jsonArray3, R.id.fragment_container);
                Log.d("asdasdasize ", " now2 " + pagell.size() + " tv name " + tv.getText());
                pagell.addLast(jsonArray3);
                Log.d("asdasdasize ", " now3 " + pagell.size());
            }
        });
                    * */
                    ll.addLast(tv);
                    someView.addView(tv);
                } else if (item.getString("type").equals("progressbar")) {
                    pb = createProgressBar(item.getInt("id"), item.getInt("relation"), item.getInt("relationid"));
                } else if (item.getString("type").equals("wheelselectorview")) {
                    ws = JSONService.createWheelSelectorView(item, this);
                    Log.d("asdasdasad ", " Y " + ws.getVisibleItems());
                    ll.addLast(ws);
                    someView.addView(ws);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    private void createPage2Objects() {
        jsonArray3 = new JSONArray();

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 1);
            item1.put("type", "textview");
            item1.put("name", "page 2");
            item1.put("relation", 0);
            item1.put("relationid", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray3.put(item1);

    }

    private void createWheelSelector() {


    }

    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        Log.d("asdasdas1", " 2" + getCurrentFragment());

        if (PaintService.getBackgroundPainted() == false) {
            PaintService.paintBackground(this, layout);
            PaintService.setBackgroundPainted(true);
        }

        if (PaintService.getTextPainted() == false) {
            Log.d("asdasdas1", " @ ");
            PaintService.paintText(this, layout);
            PaintService.setTextPainted(true);
        }

    }

    public void resetTextColorCount() {
        PaintService.setTextPainted(false);
    }

    @Override
    public void onBackPressed() {
        if (pagell.size() <= 1) {
            finish();
        } else {
            Log.d("asdasdasize ", "" + pagell.size());
            pagell.removeLast();
            clearMiddleContainer();
            //addObjectsToView(jsonArray0);

/*            String fragment = getPreviousFragment(getCurrentFragment());
            setFragmentView(fragment, false);
            setCurrentFragment(fragment);
            updateTitleText(fragment);*/
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
