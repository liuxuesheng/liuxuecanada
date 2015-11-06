package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;
import com.liuxuecanada.liuxuecanada.Utils.JSONToComponentService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements
        ViewTreeObserver.OnGlobalLayoutListener,
        AsyncResponse,
        TestFragment.OnLayoutCreateListener {

    private static HashMap<String, String> userSelection;
    LinearLayout layout = null;
    LinkedList<JSONArray> pagell = null;
    JSONArray arr = null;
    private String mainURL = "";
    private String nextURL = "";

    public static void setUserSelection(String selectionName, String selectionValue) {
        if (userSelection == null) {
            userSelection = new HashMap<String, String>();
        }
        userSelection.put(selectionName, selectionValue);

        Log.d("userselection: ", "" + userSelection.toString());

    }

    public static void removeUserSelection(String selectionName) {
        try {
            userSelection.remove(selectionName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.d("userselection: ", "" + userSelection.toString());

    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);
            clearAllContainers(this);
            Log.d("asdh8sdd ", "6");

            addObjectsToView(arr, this, getMainURL());
            Log.d("asdh8sdd ", "7");
            Log.d("asd8d ", "4 " + arr);

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

        Intent intent = getIntent();
        if (intent.hasExtra("test1")) {
            setMaintURL("http://10.135.51.51/liuxuecanadaserver/tests/test1/index.php?page=");
            setNextURL("1");
        } else if (intent.hasExtra("login")) {
            setMaintURL("http://10.135.51.51/liuxuecanadaserver/login/index.php?page=");
            setNextURL("1");
        }

        setContentView(R.layout.flow_main);

        if (findViewById(R.id.fragment_main_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            TestFragment firstFragment = new TestFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());


            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main_container, firstFragment).commit();
        }

    }

    public void updateLayout() {
        findViewById(R.id.fragment_top_container_d).setBackgroundColor(Color.rgb(30, 136, 229));
        findViewById(R.id.fragment_container_d).setBackgroundColor(Color.rgb(238, 238, 238));
        findViewById(R.id.fragment_bottom_container_d).setBackgroundColor(Color.rgb(238, 238, 238));

        /*LayoutTransition lt = new LayoutTransition();
        ((RelativeLayout) findViewById(R.id.fragment_top_container_d)).setLayoutTransition(lt);*/

        ServerResponse pud = new ServerResponse(this);
        pud.execute(getNextURL());
    }

    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        /*if (PaintService.getBackgroundPainted() == false) {
            PaintService.paintBackground(this, layout);
            PaintService.setBackgroundPainted(true);
        }*/

/*        if (PaintService.getTextPainted() == false) {
            PaintService.paintText(this, layout);
            PaintService.setTextPainted(true);
        }*/

    }

    @Override
    public void onBackPressed() {
        if (pagell == null || pagell.size() <= 1) {
            finish();
        } else {
            pagell.removeLast();
            clearAllContainers(this);
            PaintService.setTextPainted(false);
            addObjectsToView(pagell.getLast(), this, getMainURL());
        }
    }

    private void animateFade(int id, boolean dofadeout) {
        Button bt = (Button) findViewById(id);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.exit);
        bt.startAnimation(fadeout);
    }

    private void setBlurBackground() {
        View beneathView = findViewById(R.id.fragment_main_container);
        View blurView = findViewById(R.id.fragment_tutorial_container);
        BlurDrawable blurDrawable = new BlurDrawable(beneathView, 40);
        blurView.setBackground(blurDrawable);
    }

    private void addObjectsToView(JSONArray jsonArray, Activity activity, String url) {
        final Activity currentActivity = activity;
        final RelativeLayout topView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_top_container_d);
        final RelativeLayout middleView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_container_d);
        final RelativeLayout bottomView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_bottom_container_d);
        final LinearLayout mainView = (LinearLayout) currentActivity.findViewById(R.id.fragment_main_container);

        RelativeLayout someView;

        final LinkedList<View> ll = new LinkedList<>();

        ProgressBar pb = null;
        SeekBar sb = null;
        EditText et = null;
        ListView lv = null;
        Button bt = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final JSONObject item = jsonArray.getJSONObject(i);
                if (item.getString("inlayout").equals("middle"))
                    someView = middleView;
                else if (item.getString("inlayout").equals("top"))
                    someView = topView;
                else if (item.getString("inlayout").equals("bottom"))
                    someView = bottomView;
                else
                    break;

                if (item.getString("type").equals("textview")) {
                    final TextView tv = JSONToComponentService.createTextView(item, currentActivity);
                    ll.addLast(tv);
                    someView.addView(tv);
                } else if (item.getString("type").equals("button")) {
                    bt = JSONToComponentService.createButton(item, currentActivity);

                    List<String> blockids = null;
                    try {
                        blockids = Arrays.asList(item.getString("blockbyid").split(","));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    final List<String> myBlockIds = blockids;

                    try {
                        setNextURL(item.getString("nextPage"));

                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (myBlockIds != null) {
                                    ArrayList<String> localBlockIds = new ArrayList<String>(myBlockIds);

                                    int singleId;
                                    boolean flag = true;
                                    while (localBlockIds.size() > 0) {
                                        singleId = -1;
                                        try {
                                            singleId = Integer.parseInt(localBlockIds.get(0));
                                        } catch (Exception ex) {
                                            if (!checkRelatedBlockIds(localBlockIds, localBlockIds.get(0))) {
                                                flag = false;
                                                makeToast();
                                                break;
                                            } else {
                                                continue;
                                            }
                                        }

                                        View view = ((Activity) currentActivity).findViewById(singleId);
                                        if ((view instanceof SeekBar) && (((SeekBar) view).getProgress() == 0)) {
                                            flag = false;
                                            makeToast();
                                            break;
                                        } else if ((view instanceof ListView) && (((ListView) view).getCheckedItemPosition() == -1)) {
                                            flag = false;
                                            makeToast();
                                            break;
                                        }
                                        localBlockIds.remove(0);
                                    }

                                    if (flag)
                                        goToNextPage();

                                } else {
                                    goToNextPage();
                                }
                            }

                            private boolean checkRelatedBlockIds(ArrayList<String> idList, String referenceId) {
                                int referenceIdNumber = Integer.parseInt(referenceId.substring(0, referenceId.length() - 1));
                                String relatedLetter = referenceId.substring(referenceId.length() - 1);

                                boolean flag = false;
                                ArrayList toRemove = new ArrayList();

                                for (String item : idList) {
                                    String itemRelatedLetter = item.substring(item.length() - 1);
                                    int singleId = Integer.parseInt(item.substring(0, item.length() - 1));

                                    if (itemRelatedLetter.equals(relatedLetter)) {
                                        View view = ((Activity) currentActivity).findViewById(singleId);
                                        if (view instanceof SeekBar) {
                                            if (((SeekBar) view).getProgress() == 0) {
                                                flag = flag || false;
                                            } else {
                                                flag = flag || true;
                                            }
                                        } else if (view instanceof ListView) {
                                            if (((ListView) view).getCheckedItemPosition() == -1)
                                                flag = flag || false;
                                            else
                                                flag = flag || true;
                                        }
                                    }
                                    toRemove.add(item);
                                }

                                if (toRemove != null)
                                    idList.removeAll(toRemove);

                                return flag;
                            }

                            private void makeToast() {
                                Toast.makeText(currentActivity, "请先选择 =)", Toast.LENGTH_SHORT).show();
                            }

                            private void goToNextPage() {
                                if (mainView != null) {
                                    Log.d("asdh8sdd ", "AC");

                                    /*if (savedInstanceState != null) {
                                        return;
                                    }*/

                                    // Create a new Fragment to be placed in the activity layout
                                    TestFragment nextFragment = new TestFragment();

                                    // In case this activity was started with special instructions from an
                                    // Intent, pass the Intent's extras to the fragment as arguments
                                    nextFragment.setArguments(getIntent().getExtras());

                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.fragment_main_container, nextFragment).commit();
                                }
                            }
                        });
                    } catch (JSONException ex) {

                    }
                    ll.addLast(bt);
                    someView.addView(bt);
                } else if (item.getString("type").equals("progressbar")) {
                    pb = JSONToComponentService.createProgressBarView(item, currentActivity);
                    ll.addLast(pb);
                    someView.addView(pb);
                } else if (item.getString("type").equals("seekbar")) {
                    try {
                        View[] vlist = JSONToComponentService.createSeekBarView(item, currentActivity);
                        for (int k = 0; k < vlist.length; k++) {

                            ll.addLast(vlist[k]);
                            someView.addView(vlist[k]);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (item.getString("type").equals("2seekbar")) {
                    try {
                        View[] vlist = JSONToComponentService.createDoubleSeekBarView(item, currentActivity);

                        for (int k = 0; k < vlist.length; k++) {

                            ll.addLast(vlist[k]);
                            someView.addView(vlist[k]);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (item.getString("type").equals("edittext")) {
                    et = JSONToComponentService.createEditText(item, currentActivity);
                    ll.addLast(et);
                    someView.addView(et);
                } else if (item.getString("type").equals("listview")) {
                    Log.d("asd8d ", " 1 ");
                    lv = JSONToComponentService.createListView(item, currentActivity);
                    Log.d("asd8d ", " 2 ");
                    ll.addLast(lv);
                    Log.d("asd8d ", " 3 ");
                    someView.addView(lv);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1500);

       /* fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/

        //Animation enter = AnimationUtils.loadAnimation(activity, R.anim.enter);

        //topView.startAnimation(enter);
        //middleView.startAnimation(fadeIn);
        //bottomView.startAnimation(enter);
    }

    private String getNextURL() {
        return this.nextURL;
    }

    private void setNextURL(String page) {
        this.nextURL = getMainURL() + page;
    }

    private String getMainURL() {
        return this.mainURL;
    }

    private void setMaintURL(String page) {
        this.mainURL = getMainURL() + page;
    }

    private void clearAllContainers(Activity activity) {
        //public static void clearAllContainers(JSONArray jsonArray, Activity activity, String url) {
        Animation exit = AnimationUtils.loadAnimation(activity, R.anim.exit);
        final Animation enter = AnimationUtils.loadAnimation(activity, R.anim.enter);
        Animation popExit = AnimationUtils.loadAnimation(activity, R.anim.popexit);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //add this
        fadeOut.setDuration(1500);

        RelativeLayout rl1 = ((RelativeLayout) activity.findViewById(R.id.fragment_top_container_d));
        final RelativeLayout rl2 = ((RelativeLayout) activity.findViewById(R.id.fragment_container_d));
        RelativeLayout rl3 = ((RelativeLayout) activity.findViewById(R.id.fragment_bottom_container_d));


        /*final JSONArray ja = jsonArray;
        final Activity ac = activity;
        final String ul = url;*/

        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1500);

        //rl1.startAnimation(exit);
        //fadeOutToBottom(rl2, true);

        //rl2.startAnimation(fadeOut);
        //rl2.startAnimation(exit);

        //rl3.startAnimation(exit);

        rl1.removeAllViews();
        rl2.removeAllViews();
        rl3.removeAllViews();

    }

    private void fadeOutToBottom(View v, boolean animated) {
        v.animate().
                translationYBy(500).
                alpha(0).
                setDuration(animated ? 1000 : 0).
                setInterpolator(new AccelerateInterpolator()).
                start();
    }

    private void slideInToTop(View v, boolean animated) {
        v.animate().
                translationY(0).
                alpha(1).
                setDuration(animated ? 1000 : 0).
                setInterpolator(new AccelerateInterpolator()).
                start();
    }
}
