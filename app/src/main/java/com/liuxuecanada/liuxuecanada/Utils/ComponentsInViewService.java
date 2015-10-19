package com.liuxuecanada.liuxuecanada.Utils;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.adapters.ArrayWheelAdapter;
import com.liuxuecanada.liuxuecanada.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ComponentsInViewService {

    public static void addObjectsToView(JSONArray jsonArray, Activity activity, String url) {
        final Activity currentActivity = activity;
        final String mainURL = url;
        final RelativeLayout topView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_top_container);
        final RelativeLayout middleView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_container);
        final RelativeLayout bottomView = (RelativeLayout) currentActivity.findViewById(R.id.fragment_bottom_container);

        RelativeLayout someView;

        final LinkedList<View> ll = new LinkedList<>();

        ProgressBar pb = null;
        WheelSelector ws = null;
        SeekBar sb = null;
        EditText et = null;
        ListView lv = null;

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

                    Log.d("asdasdas2da2ad ", " ABC1 ");
                    try{
                        final String nextPage = item.getString("nextPage");
                        final String savedatatype = item.getString("savedatatype");
                        Log.d("asdasdas2da2ad ", " ABC2 ");

                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (savedatatype.equals("wheelselectorview")) {
                                    try {
                                        int savedataid = item.getInt("savedataid");
                                        WheelSelector ws = (WheelSelector) currentActivity.findViewById(savedataid);
                                        int index = ws.getCurrentViewIndex();
                                        Log.d("asdasdas2da2ad ", " ABC6 " + ((ArrayWheelAdapter<String>) ws.getViewAdapter()).getItemText(index));
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                clearAllContainers(currentActivity);
                                Log.d("asdasdas2da2ad ", " ABC3 ");
                                ServerResponse pud = new ServerResponse((AsyncResponse) currentActivity);
                                pud.execute(mainURL + nextPage);

                            }
                        });
                    }catch (JSONException ex){

                    }

                    ll.addLast(tv);
                    someView.addView(tv);
                } else if (item.getString("type").equals("progressbar")) {
                    pb = JSONToComponentService.createProgressBarView(item, currentActivity);
                    ll.addLast(pb);
                    someView.addView(pb);
                } else if (item.getString("type").equals("wheelselectorview")) {
                    ws = JSONToComponentService.createWheelSelectorView(item, currentActivity);
                    Log.d("asdasdasad ", " Y " + ws.getVisibleItems());
                    ll.addLast(ws);
                    someView.addView(ws);
                } else if (item.getString("type").equals("seekbar")) {
                    int seekresultid;

                    try {
                        seekresultid = item.getInt("seekresultid");
                        TextView seekresult = (TextView) currentActivity.findViewById(seekresultid);
                        sb = JSONToComponentService.createSeekBarView(item, seekresult, currentActivity);
                        ll.addLast(sb);
                        someView.addView(sb);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                } else if (item.getString("type").equals("edittext")){
                    et = JSONToComponentService.createEditText(item, currentActivity);
                    ll.addLast(et);
                    someView.addView(et);
                } else if (item.getString("type").equals("listview")){
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
    }

    public static void clearAllContainers(Activity activity) {
        ((RelativeLayout) activity.findViewById(R.id.fragment_top_container)).removeAllViews();
        ((RelativeLayout) activity.findViewById(R.id.fragment_container)).removeAllViews();
        ((RelativeLayout) activity.findViewById(R.id.fragment_bottom_container)).removeAllViews();
    }


}
