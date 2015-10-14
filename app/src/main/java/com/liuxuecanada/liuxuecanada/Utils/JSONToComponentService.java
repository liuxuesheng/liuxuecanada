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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.adapters.ArrayWheelAdapter;
import com.liuxuecanada.liuxuecanada.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONToComponentService {

    public static TextView createTextView(JSONObject jsonObject, Context context) {
        int id;
        String name;
        int relation;
        int relationid;
        int textsize;
        String alignment;

        try {
            jsonObject.getString("type").equals("textview");
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
            textsize = jsonObject.getInt("size");
            alignment = jsonObject.getString("alignment");
        } catch (JSONException ex) {
            return null;
        }

        String[] alignmentArray = alignment.split(",");
        int count = 0;

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        while(count < alignmentArray.length) {
            Log.d("7s73hs82h ", "B");
            int alignmentInt = Integer.parseInt(alignmentArray[count]);
            p.addRule(alignmentInt);
            count++;
        }

        if ((relation != 0) && (relationid != 0)) {
            Log.d("asdasdasad ", " k " + relation + " " + relationid);
            p.addRule(relation, relationid);
        }
        TextView tv = new TextView(context);
        tv.setId(id);
        tv.setText(name);
        tv.setLayoutParams(p);
        tv.setBackgroundColor(Color.TRANSPARENT);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);

        return tv;
    }

    public static EditText createEditText(JSONObject jsonObject, Context context) {
        int id;
        //String name;
        String relation;
        String relationid;
        String hint;
        //int textsize;

        Log.d("7s73hs82h ", "AAA");

        try {
            jsonObject.getString("type").equals("edittext");
            id = jsonObject.getInt("id");
            //name = jsonObject.getString("name");
            relation = jsonObject.getString("relation");
            relationid = jsonObject.getString("relationid");
            hint = jsonObject.getString("hint");
        } catch (JSONException ex) {
            return null;
        }

        Log.d("7s73hs82h ", "A");
        String[] relationArray = relation.split(",");
        String[] relationidArray = relationid.split(",");
        int count = 0;

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);

        while(count < relationArray.length) {
            Log.d("7s73hs82h ", "B");
            int relatonInt = Integer.parseInt(relationArray[count]);
            int relatonidInt = Integer.parseInt(relationidArray[count]);
            if ((relatonInt != 0) && (relatonidInt != 0)) {
                p.addRule(relatonInt, relatonidInt);
            }
            count++;
        }
        Log.d("7s73hs82h ", "C");
        EditText et = new EditText(context);
        et.setId(id);
        et.setHint(hint);
        et.setLayoutParams(p);
        et.setBackgroundColor(Color.TRANSPARENT);
        //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);

        return et;
    }

    public static WheelSelector createWheelSelectorView(JSONObject jsonObject, Context context) {
        int id;
        String name;
        String values;
        int relation;
        int relationid;

        try {
            jsonObject.getString("type").equals("wheelselectorview");
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            values = jsonObject.getString("values");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
        } catch (JSONException ex) {
            return null;
        }

        String[] universities = values.split(",");

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);

        Log.d("asdasdasad ", " X ");
        if ((relation != 0) && (relationid != 0)) {
            Log.d("asdasdasad ", " " + relation + " " + relationid);
            p.addRule(relation, relationid);
        }

        WheelSelector universitySelector = new WheelSelector(context);

        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, universities);
        adapter.setTextSize(12);
        universitySelector.setViewAdapter(adapter);
        universitySelector.setCurrentItem(universities.length / 2);
        universitySelector.setVisibleItems(5);
        universitySelector.setLayoutParams(p);
        universitySelector.setId(id);
        return universitySelector;
    }

    public static ProgressBar createProgressBarView(JSONObject jsonObject, Context context) {
        int id;
        int relation;
        int relationid;

        try {
            jsonObject.getString("type").equals("progressbar");
            id = jsonObject.getInt("id");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);
        ProgressBar pb = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        pb.setId(id);
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
        int id;
        int relation;
        int relationid;
        final String name;

        try {
            jsonObject.getString("type").equals("progressbar");
            id = jsonObject.getInt("id");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
            name = jsonObject.getString("name");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);

        SeekBar seekBar = new SeekBar(context);
        seekBar.setId(id);
        seekBar.setLayoutParams(p);

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
}
