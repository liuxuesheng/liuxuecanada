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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.adapters.ArrayWheelAdapter;
import com.liuxuecanada.liuxuecanada.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONService {

    public static TextView createTextView(JSONObject jsonObject, Context context) {
        int id;
        String name;
        int relation;
        int relationid;
        int textsize;

        try {
            jsonObject.getString("type").equals("textview");
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
            textsize = jsonObject.getInt("size");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);

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
        int seekresultid;

        try {
            jsonObject.getString("type").equals("progressbar");
            id = jsonObject.getInt("id");
            relation = jsonObject.getInt("relation");
            relationid = jsonObject.getInt("relationid");
            seekresultid = jsonObject.getInt("seekresultid");
        } catch (JSONException ex) {
            return null;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);

        SeekBar seekBar = new SeekBar(context);
        seekBar.setId(id);
        seekBar.setLayoutParams(p);

        final TextView seekBarResult = seekresult;
        seekBarResult.setText("Score: " + seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                seekBarResult.setText("Score: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarResult.setText("Score: " + score);
                //mCallback.updateProceedButton();
            }
        });

        return seekBar;
    }
}
