package com.liuxuecanada.liuxuecanada.Utils;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
                        final String nextPage = item.getString("nextPage");

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
                                ServerResponse pud = new ServerResponse((AsyncResponse) currentActivity);
                                pud.execute(mainURL + nextPage);
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
                } else if (item.getString("type").equals("wheelselectorview")) {
                    ws = JSONToComponentService.createWheelSelectorView(item, currentActivity);
                    ll.addLast(ws);
                    someView.addView(ws);
                } else if (item.getString("type").equals("seekbar")) {
                    int seekresultid;

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
    }

    public static void clearAllContainers(Activity activity) {
        ((RelativeLayout) activity.findViewById(R.id.fragment_top_container)).removeAllViews();
        ((RelativeLayout) activity.findViewById(R.id.fragment_container)).removeAllViews();
        ((RelativeLayout) activity.findViewById(R.id.fragment_bottom_container)).removeAllViews();
    }


}
