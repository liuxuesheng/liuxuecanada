package com.liuxuecanada.liuxuecanada.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ContentItem;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ListAdapter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.adapters.ArrayWheelAdapter;
import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONToComponentService {

    private static int id;
    private static String alignment;
    private static String relation;
    private static String relationId;
    //private static int index=-1;

    public static TextView createTextView(JSONObject jsonObject, Context context) {
        String name;
        int textsize;

        try {
            jsonObject.getString("type").equals("textview");
            name = jsonObject.getString("name");
            textsize = jsonObject.getInt("size");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        setId(tv, getId(jsonObject));
        setAlignment(tv, getAlignment(jsonObject), p);
        setRelations(tv, getRelation(jsonObject), getRelationId(jsonObject), p);
        tv.setText(name);
        tv.setBackgroundColor(Color.TRANSPARENT);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
        tv.setTextColor(Color.parseColor(getTextColor(jsonObject)));

        return tv;
    }

    public static Button createButton(JSONObject jsonObject, Context context) {
        String name;
        int textsize;

        try {
            jsonObject.getString("type").equals("button");
            name = jsonObject.getString("name");
            textsize = jsonObject.getInt("size");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button bt = new Button(context);
        setId(bt, getId(jsonObject));
        setAlignment(bt, getAlignment(jsonObject), p);
        setRelations(bt, getRelation(jsonObject), getRelationId(jsonObject), p);
        bt.setText(name);
        //bt.setBackgroundColor(Color.TRANSPARENT);
        setBackgroundColor(bt, getBackgroundColor(jsonObject));
        bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
        bt.setTextColor(Color.parseColor(getTextColor(jsonObject)));
        bt.setElevation(2);
        return bt;
    }

    public static EditText createEditText(JSONObject jsonObject, Context context) {
        String hint;

        try {
            jsonObject.getString("type").equals("edittext");
            hint = jsonObject.getString("hint");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        EditText et = new EditText(context);
        setId(et, getId(jsonObject));
        setAlignment(et, getAlignment(jsonObject), p);
        setRelations(et, getRelation(jsonObject), getRelationId(jsonObject), p);
        et.setHint(hint);
        et.setBackgroundColor(Color.TRANSPARENT);
        //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);

        return et;
    }

    public static ListView createListView(JSONObject jsonObject, Context context) {
        String values;

        try {
            jsonObject.getString("type").equals("listview");
            values = jsonObject.getString("values");

        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        String[] valueArray = values.split(",");
        int count3 = 0;
        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
        while (count3 < valueArray.length) {
            objects.add(new ContentItem(valueArray[count3], PaintService.paintLevelIconDrawable(context, "S")));
            count3++;
        }

        final ListAdapter adapter = new ListAdapter(context, objects);
        final ListView lv = new ListView(context);

        lv.setAdapter(adapter);
        setId(lv, getId(jsonObject));
        setAlignment(lv, getAlignment(jsonObject), p);
        setRelations(lv, getRelation(jsonObject), getRelationId(jsonObject), p);
        lv.setVerticalScrollBarEnabled(true);
        lv.setScrollbarFadingEnabled(false);
        lv.setBackgroundColor(Color.TRANSPARENT);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("userselection: ", "lv " + ((ContentItem) lv.getItemAtPosition(position)).getName() + " index= " + adapter.getIndex());
                EnterStudentChoicesActivity.setUserSelection(Integer.toString(lv.getId()), ((ContentItem) lv.getItemAtPosition(position)).getName());
                if (adapter.getIndex() == -1) {
                    view.setBackgroundColor(Color.rgb(255, 165, 0));
                } else if (adapter.getIndex() != position) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                    //parent.getChildAt(adapter.getIndex()).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.rgb(255, 165, 0));
                }
                adapter.setIndex(position);
            }
        });

        return lv;
    }

    public static WheelSelector createWheelSelectorView(JSONObject jsonObject, Context context) {
        String name;
        String values;

        try {
            jsonObject.getString("type").equals("wheelselectorview");
            name = jsonObject.getString("name");
            values = jsonObject.getString("values");
        } catch (JSONException ex) {
            return null;
        }

        String[] universities = values.split(",");

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        WheelSelector universitySelector = new WheelSelector(context);

        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, universities);
        adapter.setTextSize(12);
        universitySelector.setViewAdapter(adapter);
        universitySelector.setCurrentItem(universities.length / 2);
        universitySelector.setVisibleItems(5);

        setId(universitySelector, getId(jsonObject));
        setAlignment(universitySelector, getAlignment(jsonObject), p);
        setRelations(universitySelector, getRelation(jsonObject), getRelationId(jsonObject), p);
        return universitySelector;
    }

    public static ProgressBar createProgressBarView(JSONObject jsonObject, Context context) {
        try {
            jsonObject.getString("type").equals("progressbar");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ProgressBar pb = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        setId(pb, getId(jsonObject));
        setRelations(pb, getRelation(jsonObject), getRelationId(jsonObject), p);
        pb.setLayoutParams(p);
        pb.setBackgroundColor(Color.TRANSPARENT);
        pb.setProgressDrawable(createDrawable(context));
        return pb;
    }

    private static Drawable createDrawable(Context context) {

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

    public static View[] createSeekBarView(JSONObject jsonObject, Context context) {
        View[] va;
        final String name;

        int textid, seekbarid;

        try {
            jsonObject.getString("type").equals("seekbar");
            name = jsonObject.getString("textname");
            textid = jsonObject.getInt("textid");
            seekbarid = jsonObject.getInt("seekbarid");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        final TextView tv = new TextView(context);
        final SeekBar seekBar = new SeekBar(context);

        seekBar.setId(seekbarid);
        tv.setId(textid);

        setAlignment(tv, getAlignment(jsonObject), p1);
        setRelations(tv, getRelation(jsonObject), getRelationId(jsonObject), p1);

        p2.addRule(RelativeLayout.BELOW, textid);
        seekBar.setLayoutParams(p2);

        int max = getSeekBarMaxValue(jsonObject);
        final int min = getSeekBarMinValue(jsonObject);
        final double factor = getSeekBarFactor(jsonObject);

        seekBar.setMax((int) ((max - min) / factor));

        tv.setText("" + name + ": " + getFormatedString(factor, seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                tv.setText("" + name + ": " + (getFormatedString(factor, (double) min + ((double) progress * factor))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EnterStudentChoicesActivity.setUserSelection(Integer.toString(seekBar.getId()), tv.getText().toString().substring(5));
            }
        });

        return new View[]{tv, seekBar};
    }

    public static View[] createDoubleSeekBarView(JSONObject jsonObject, Context context) {
        View[] va;
        final String name1;
        final String name2;

        int textid1, textid2, seekbarid1, seekbarid2;

        try {
            jsonObject.getString("type").equals("2seekbar");
            name1 = jsonObject.getString("textname1");
            name2 = jsonObject.getString("textname2");
            textid1 = jsonObject.getInt("textid1");
            textid2 = jsonObject.getInt("textid2");
            seekbarid1 = jsonObject.getInt("seekbarid1");
            seekbarid2 = jsonObject.getInt("seekbarid2");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        final TextView tv1 = new TextView(context);
        final TextView tv2 = new TextView(context);
        final SeekBar seekBar1 = new SeekBar(context);
        final SeekBar seekBar2 = new SeekBar(context);

        seekBar1.setId(seekbarid1);
        seekBar2.setId(seekbarid2);
        tv1.setId(textid1);
        tv2.setId(textid2);

        setAlignment(tv1, getAlignment(jsonObject), p1);
        setRelations(tv1, getRelation(jsonObject), getRelationId(jsonObject), p1);

        p2.addRule(RelativeLayout.BELOW, textid1);
        seekBar1.setLayoutParams(p2);

        p3.addRule(RelativeLayout.BELOW, seekbarid1);
        tv2.setLayoutParams(p3);

        p4.addRule(RelativeLayout.BELOW, textid2);
        seekBar2.setLayoutParams(p4);


        int max1 = 0;
        int max2 = 0;
        int min1 = 0;
        int min2 = 0;
        double factor1 = 0;
        double factor2 = 0;
        try {
            max1 = jsonObject.getInt("maxvalue1");
            max2 = jsonObject.getInt("maxvalue2");
            min1 = jsonObject.getInt("minvalue1");
            min2 = jsonObject.getInt("minvalue2");
            factor1 = jsonObject.getDouble("factor1");
            factor2 = jsonObject.getDouble("factor2");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        seekBar1.setMax((int) ((max1 - min1) / factor1));
        seekBar2.setMax((int) ((max2 - min2) / factor2));

        /*try {
            tv1 = (TextView) ((Activity)context).findViewById(textid1);
            tv2 = (TextView) ((Activity)context).findViewById(textid2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/


        tv1.setText("" + name1 + ": " + getFormatedString(factor1, seekBar1.getProgress()));
        tv2.setText("" + name2 + ": " + getFormatedString(factor2, seekBar2.getProgress()));

        final int minmin1 = min1;
        final double factorfactor1 = factor1;
        final int minmin2 = min2;
        final double factorfactor2 = factor2;

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                tv1.setText("" + name1 + ": " + (getFormatedString(factorfactor1, (double) minmin1 + ((double) progress * factorfactor1))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv2.setText("" + name2 + ": ");
                seekBar2.setProgress(0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EnterStudentChoicesActivity.removeUserSelection(Integer.toString(seekBar2.getId()));
                EnterStudentChoicesActivity.setUserSelection(Integer.toString(seekBar.getId()), tv1.getText().toString().substring(7));
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                tv2.setText("" + name2 + ": " + (getFormatedString(factorfactor2, (double) minmin2 + ((double) progress * factorfactor2))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv1.setText("" + name1 + ": ");
                seekBar1.setProgress(0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EnterStudentChoicesActivity.removeUserSelection(Integer.toString(seekBar1.getId()));
                EnterStudentChoicesActivity.setUserSelection(Integer.toString(seekBar.getId()), tv2.getText().toString().substring(7));
            }
        });

        return new View[]{tv1, seekBar1, tv2, seekBar2};
    }

    private static int getId(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    private static void setId(View view, int id) {
        view.setId(id);
    }

    private static String[] getAlignment(JSONObject jsonObject) {
        try {
            alignment = jsonObject.getString("alignment");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return alignment.split(",");
    }

    private static void setAlignment(View view, String[] alignmentArray, RelativeLayout.LayoutParams p) {
        int count = 0;
        while (count < alignmentArray.length) {
            int alignmentInt = Integer.parseInt(alignmentArray[count]);
            p.addRule(alignmentInt);
            Log.d("djskc8csdn9 ", "" + RelativeLayout.CENTER_HORIZONTAL);
            Log.d("djskc8csdn9 ", "" + RelativeLayout.CENTER_VERTICAL);
            Log.d("djskc8csdn9 ", "" + RelativeLayout.CENTER_IN_PARENT);
            Log.d("djskc8csdn9 ", "" + RelativeLayout.BELOW);
            Log.d("djskc8csdn9 ", "" + RelativeLayout.ABOVE);
            count++;
        }
        view.setLayoutParams(p);
    }

    private static String[] getRelation(JSONObject jsonObject) {
        try {
            relation = jsonObject.getString("relation");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return relation.split(",");
    }

    private static String[] getRelationId(JSONObject jsonObject) {
        try {
            relationId = jsonObject.getString("relationid");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return relationId.split(",");
    }

    private static void setRelations(View view, String[] relationArray, String[] relationIdArray, RelativeLayout.LayoutParams p) {
        int count = 0;
        while (count < relationArray.length) {
            int myRelation = Integer.parseInt(relationArray[count]);
            int myRelatonId = Integer.parseInt(relationIdArray[count]);
            if ((myRelation != 0) && (myRelatonId != 0)) {
                p.addRule(myRelation, myRelatonId);
            }
            count++;
        }
        view.setLayoutParams(p);
    }

    private static String getTextColor(JSONObject jsonObject) {
        String textColor = "";
        try {
            textColor = jsonObject.getString("textcolor");
        } catch (JSONException ex) {
            return "#000000";
        }
        return textColor;
    }

    private static int getSeekBarMaxValue(JSONObject jsonObject) {
        int max = 0;
        try {
            max = jsonObject.getInt("maxvalue");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return max;
    }

    private static int getSeekBarMinValue(JSONObject jsonObject) {
        int min = 0;
        try {
            min = jsonObject.getInt("minvalue");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return min;
    }

    private static double getSeekBarFactor(JSONObject jsonObject) {
        double factor = 0;
        try {
            factor = jsonObject.getDouble("factor");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return factor;
    }

    private static String getFormatedString(double factor, double number) {
        if ((factor >= 0.1) && (factor < 1))
            return String.format("%.1f", number);
        else if ((factor % 1) == 0)
            return String.format("%s", (int) number);
        else
            return String.format("%s", number);
    }

    private static String[] getBackgroundColor(JSONObject jsonObject) {
        String rgb = "";
        try {
            rgb = jsonObject.getString("backgroundcolor");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        String[] rgbArray = rgb.split(",");

        return rgbArray;
    }

    private static void setBackgroundColor(View view, String[] colorArray) {
        try {
            view.setBackgroundColor(Color.rgb(Integer.parseInt(colorArray[0]), Integer.parseInt(colorArray[1]), Integer.parseInt(colorArray[2])));
        } catch (Exception ex) {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
