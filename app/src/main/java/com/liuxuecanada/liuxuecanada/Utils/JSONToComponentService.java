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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONToComponentService {

    private static int id;
    private static String alignment;
    private static String relation;
    private static String relationId;

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

        return tv;
    }

    public static EditText createEditText(JSONObject jsonObject, Context context) {
        String hint;

        Log.d("7s73hs82h ", "AAA");

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
        String alignment;
        String values;

        Log.d("7s73hs82h ", "AAA");

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
            Log.d("asd8d ", " 3 " + valueArray[count3]);
            objects.add(new ContentItem(valueArray[count3], PaintService.paintLevelIconDrawable(context, "S")));
            count3++;
        }

        ListAdapter adapter = new ListAdapter(context, objects);
        ListView lv = new ListView(context);

        lv.setAdapter(adapter);
        setId(lv, getId(jsonObject));
        setAlignment(lv, getAlignment(jsonObject), p);
        setRelations(lv, getRelation(jsonObject), getRelationId(jsonObject), p);
        lv.setVerticalScrollBarEnabled(true);
        lv.setScrollbarFadingEnabled(false);
        lv.setBackgroundColor(Color.TRANSPARENT);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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

    public static SeekBar createSeekBarView(JSONObject jsonObject, TextView seekresult, Context context) {
        final String name;

        try {
            jsonObject.getString("type").equals("progressbar");
            name = jsonObject.getString("name");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        SeekBar seekBar = new SeekBar(context);
        setId(seekBar, getId(jsonObject));
        setAlignment(seekBar, getAlignment(jsonObject), p);
        setRelations(seekBar, getRelation(jsonObject), getRelationId(jsonObject), p);

        final TextView seekBarResult = seekresult;
        seekBarResult.setText("" + name + ": " + seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                seekBarResult.setText("" + name + ": " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarResult.setText("" + name + ": " + score);
                //mCallback.updateProceedButton();
            }
        });

        return seekBar;
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
            Log.d("7s73hs82h ", "B");
            int myRelation = Integer.parseInt(relationArray[count]);
            int myRelatonId = Integer.parseInt(relationIdArray[count]);
            if ((myRelation != 0) && (myRelatonId != 0)) {
                p.addRule(myRelation, myRelatonId);
            }
            count++;
        }
    }
}
