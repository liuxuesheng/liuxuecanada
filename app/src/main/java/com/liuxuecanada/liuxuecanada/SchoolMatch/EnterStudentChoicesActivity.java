package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.PieChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.RadarChart;
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
            clearAllContainers();
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
            setMaintURL("http://10.135.53.114/liuxuecanadaserver/tests/test1/index.php?page=");
            setNextURL("1");
        } else if (intent.hasExtra("login")) {
            setMaintURL("http://10.135.53.114/liuxuecanadaserver/login/index.php?page=");
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

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

            // Add the fragment to the 'fragment_container' FrameLayout
            transaction.replace(R.id.fragment_main_container, firstFragment).addToBackStack(null).commit();
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
            clearAllContainers();
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
        PieChart pc = null;
        RadarChart rc = null;

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

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    transaction.replace(R.id.fragment_main_container, nextFragment).addToBackStack(null).commit();
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
                    lv = JSONToComponentService.createListView(item, currentActivity);
                    ll.addLast(lv);
                    someView.addView(lv);
                } else if (item.getString("type").equals("piechart")) {
                    pc = JSONToComponentService.createPieChart(item, currentActivity);
                    ll.addLast(pc);
                    someView.addView(pc);
                } else if (item.getString("type").equals("radarchart")) {
                    Log.d("sdads0ds ","A");
                    rc = JSONToComponentService.createRadarChart(item, currentActivity);
                    Log.d("sdads0ds ","B");
                    ll.addLast(rc);
                    Log.d("sdads0ds ", "C");
                    someView.addView(rc);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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

    private void clearAllContainers() {
        ((RelativeLayout) findViewById(R.id.fragment_top_container_d)).removeAllViews();
        ((RelativeLayout) findViewById(R.id.fragment_container_d)).removeAllViews();
        ((RelativeLayout) findViewById(R.id.fragment_bottom_container_d)).removeAllViews();
    }

}
