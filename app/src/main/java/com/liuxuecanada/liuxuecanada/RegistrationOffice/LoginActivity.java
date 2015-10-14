package com.liuxuecanada.liuxuecanada.RegistrationOffice;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.ComponentsInViewService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;

public class LoginActivity extends Activity
        implements AsyncResponse,
        ViewTreeObserver.OnGlobalLayoutListener{

    private final String mainURL = "http://10.135.50.41/liuxuecanadaserver/register/index.php?page=";
    private LinearLayout layout = null;
    private JSONArray feedbackJSONArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintService.setBackgroundPainted(false);
        PaintService.setTextPainted(false);

        setContentView(R.layout.flow_main);

        ServerResponse pud = new ServerResponse(this);
        pud.execute(mainURL + 1);

        layout = (LinearLayout) findViewById(R.id.fragment_main_container);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    public void onGlobalLayout() {
        //layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        if (PaintService.getBackgroundPainted() == false) {
            PaintService.paintBackground(this, layout);
            PaintService.setBackgroundPainted(true);
        }

        if (PaintService.getTextPainted() == false) {
            PaintService.paintText(this, layout);
            PaintService.setTextPainted(true);
        }

    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            feedbackJSONArray = new JSONArray((String) out);
            ComponentsInViewService.addObjectsToView(feedbackJSONArray, this, mainURL);

            /*if (pagell == null)
                pagell = new LinkedList<JSONArray>();

            pagell.addLast(arr);*/

            PaintService.setTextPainted(false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskStart() {

    }
}
