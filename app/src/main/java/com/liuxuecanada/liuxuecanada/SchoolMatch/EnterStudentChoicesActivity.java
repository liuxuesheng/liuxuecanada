package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;
import com.liuxuecanada.liuxuecanada.Utils.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements FragmentIELTS.OnSeekBarUpdateListener,
        FragmentTop.OnProgressCirclePageUpdateListener,
        FragmentAcdemicType.OnFragmentCreatedListener,
        FragmentProgram.OnFragmentCreatedListener,
        FragmentLanguageTest.OnFragmentCreatedListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private static final String[] acdemictypeitems = {"Undergraduate", "Postgraduate"};
    Bitmap bm = null;
    LinearLayout layout = null;
    LinkedList<TextView> ll = null;
    LinkedList<JSONArray> pagell = null;
    JSONArray jsonArray0 = null;
    JSONArray jsonArray1 = null;
    JSONArray jsonArray2 = null;
    JSONArray jsonArray3 = null;


    private LinkedList<String> allFlowItemNames = null;
    private int backgroundColorChangeCounter = 0;
    private int textColorChangeCounter = 0;
    private Fragment fragTutorial = null;
    private Fragment frag = null;
    private String currentFragment = null;
    private Button proceedButton = null;
    private TextView topText = null;
    private boolean disabledButton = true;
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        allFlowItemNames = new LinkedList<>();
        pagell = new LinkedList<>();

        Log.d("asdasdasize ", " now " + pagell.size());

        setContentView(R.layout.fragment_studentchoices_main);

        asda();


        createTopObjects();
        createPage1Objects();
        createBottomObjects();

        createPage2Objects();

        addObjectsToView(jsonArray0, R.id.fragment_top_container);
        addObjectsToView(jsonArray1, R.id.fragment_container);
        addObjectsToView(jsonArray2, R.id.fragment_bottom_container);

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

    private WheelView createWheelView(){
        WheelView wv = new WheelView(this);
        wv.setItems(Arrays.asList(PLANETS));
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            }
        });
        return wv;
    }

    private TextView createTextView(int id, String name, int relation, int relationid, int textsize) {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);

        if ((relation != 0) && (relationid != 0)) {
            p.addRule(relation, relationid);
        }
        final TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(name);
        tv.setLayoutParams(p);
        tv.setBackgroundColor(Color.TRANSPARENT);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
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
        return tv;
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

    private void addObjectsToView(JSONArray jsonArray, int viewId) {
        RelativeLayout middleView = (RelativeLayout) findViewById(viewId);
        LinkedList<View> ll = new LinkedList<>();
        TextView tv = null;
        ProgressBar pb = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject item = jsonArray.getJSONObject(i);
                if (item.getString("type").equals("textview")) {
                    tv = createTextView(item.getInt("id"), item.getString("name"), item.getInt("relation"), item.getInt("relationid"), 24);
                    ll.addLast(tv);
                } else if (item.getString("type").equals("progressbar")) {
                    pb = createProgressBar(item.getInt("id"), item.getInt("relation"), item.getInt("relationid"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        for (View view : ll) {
            middleView.addView(view);
        }
    }

    private void createTopObjects() {
        jsonArray0 = new JSONArray();

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 730);
            item1.put("type", "textview");
            item1.put("name", "Title");
            item1.put("relation", 0);
            item1.put("relationid", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item2 = new JSONObject();
        try {
            item2.put("id", 731);
            item2.put("type", "textview");
            item2.put("name", "Progress");
            item2.put("relation", RelativeLayout.RIGHT_OF);
            item2.put("relationid", 730);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item3 = new JSONObject();
        try {
            item3.put("id", 732);
            item3.put("type", "progressbar");
            item3.put("relation", RelativeLayout.RIGHT_OF);
            item3.put("relationid", 730);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray0.put(item1);
        jsonArray0.put(item2);
        jsonArray0.put(item3);
    }

    private void createBottomObjects() {
        jsonArray2 = new JSONArray();

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 649);
            item1.put("type", "textview");
            item1.put("name", "Continue");
            item1.put("relation", 0);
            item1.put("relationid", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray2.put(item1);
    }

    private void createPage1Objects() {
        jsonArray1 = new JSONArray();

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 1);
            item1.put("type", "textview");
            item1.put("name", "Undergraduate");
            item1.put("relation", 0);
            item1.put("relationid", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item2 = new JSONObject();
        try {
            item2.put("id", 2);
            item2.put("type", "textview");
            item2.put("name", "Postgraduate");
            item2.put("relation", RelativeLayout.BELOW);
            item2.put("relationid", 1);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray1.put(item1);
        jsonArray1.put(item2);

        pagell.addLast(jsonArray1);
    }

    private void createPage2Objects() {
        jsonArray3 = new JSONArray();

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 1);
            item1.put("type", "textview");
            item1.put("name", "Page 2");
            item1.put("relation", 0);
            item1.put("relationid", 0);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray3.put(item1);

    }

    private void asda(){
        RelativeLayout middleView = (RelativeLayout) findViewById(R.id.fragment_container);
        middleView.addView(createWheelView());
    }

    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        Log.d("asdasdas1", " 2" + getCurrentFragment());
        if (backgroundColorChangeCounter == 0) {

/*            Paint paint2 = new Paint();
            paint2.setColor(Color.GREEN);

            Paint paint3 = new Paint();
            paint3.setColor(Color.RED);

            Paint paint4 = new Paint();
            paint4.setColor(Color.BLUE);

            Paint paint5 = new Paint();
            paint5.setColor(Color.YELLOW);*/



            Log.d("asdasdas1", " back");
            int width = layout.getMeasuredWidth();
            int height = layout.getMeasuredHeight();
            Log.d("asdasdas", " " + width + " " + height);

            bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            //LinearGradient linearGradient = new LinearGradient(0, 0, width, height, new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.33f, 0.66f, 1}, Shader.TileMode.REPEAT);
            RadialGradient radicalGradient = new RadialGradient(width, (int)(height*1.3), (int) (Math.sqrt(height*height+width*width)*1.5), new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.3f, 0.6f, 0.9f}, Shader.TileMode.REPEAT);
            Paint paint = new Paint();
            paint.setShader(radicalGradient);
            paint.setDither(true);
            canvas.drawRect(0, 0, width, height, paint);

  /*          RectF rf = new RectF();
            rf.set(40  , 10, 280, 250);
            canvas.drawArc(rf, 0,360,true,paint2);
            canvas.drawArc(rf, 0,240,true,paint3);*/
            //canvas.drawArc(rf, 90,180,false,paint4);
            //canvas.drawArc(rf, 180,260,false,paint5);


            layout.setBackground(new BitmapDrawable(getResources(), bm));
            backgroundColorChangeCounter++;
        }

        if (textColorChangeCounter == 0) {
            Log.d("asdasdas1", " text");
            ll = new LinkedList<TextView>();

            LinkedList<TextView> tvlist = findAllTextView(layout);
            Log.d("asdasdas", " ll " + ll.size());

            for (TextView tv : tvlist) {
                int[] k = getViewCoordinates(tv);
                Log.d("asdasdas", " k0 " + k[0] + " k1 " + k[1]);

                int convertedColor = convertColor(bm.getPixel(k[0], k[1]));

                ((TextView) tv).setTextColor(convertedColor);
            }
            textColorChangeCounter++;
        }

    }

    public void resetTextColorCount() {
        this.textColorChangeCounter = 0;
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
            if (view instanceof WheelView)
                continue;
            else if (view instanceof ViewGroup)
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
        if (pagell.size() == 1) {
            finish();
        } else {
            Log.d("asdasdasize ", "" + pagell.size());
            pagell.removeLast();
            clearMiddleContainer();
            addObjectsToView(pagell.getLast(), R.id.fragment_container);

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
