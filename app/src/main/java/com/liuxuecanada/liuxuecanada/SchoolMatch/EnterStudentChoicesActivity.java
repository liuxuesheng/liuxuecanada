package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.BlurDrawable;
import com.liuxuecanada.liuxuecanada.Utils.ComponentsInViewService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;

public class EnterStudentChoicesActivity extends FragmentActivity
        implements
        ViewTreeObserver.OnGlobalLayoutListener,
        AsyncResponse {

    private final String mainURL = "http://10.135.51.51/liuxuecanadaserver/tests/test1/index.php?page=";
    LinearLayout layout = null;
    LinkedList<JSONArray> pagell = null;
    JSONArray arr = null;

    private static HashMap<String, String> userSelection;

    public static void setUserSelection(String selectionName, String selectionValue) {
        if (userSelection == null) {
            userSelection = new HashMap<String, String>();
        }
        userSelection.put(selectionName, selectionValue);

        Log.d("userselection: ", "" + userSelection.toString());

    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            ComponentsInViewService.clearAllContainers(this);

            arr = new JSONArray((String) out);
            ComponentsInViewService.addObjectsToView(arr, this, mainURL);

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

        setContentView(R.layout.flow_main);

        findViewById(R.id.fragment_top_container).setBackgroundColor(Color.rgb(30, 136, 229));
        findViewById(R.id.fragment_container).setBackgroundColor(Color.rgb(238, 238, 238));
        findViewById(R.id.fragment_bottom_container).setBackgroundColor(Color.rgb(238, 238, 238));

        ServerResponse pud = new ServerResponse(this);

        pud.execute(mainURL + 1);

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
            ComponentsInViewService.clearAllContainers(this);
            PaintService.setTextPainted(false);
            ComponentsInViewService.addObjectsToView(pagell.getLast(), this, mainURL);

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
}
