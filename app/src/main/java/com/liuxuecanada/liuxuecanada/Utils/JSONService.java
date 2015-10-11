package com.liuxuecanada.liuxuecanada.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.adapters.ArrayWheelAdapter;
import com.liuxuecanada.liuxuecanada.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONService {

    public static TextView createTextView(JSONObject jsonObject,Context context) {
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
        p.addRule(RelativeLayout.CENTER_IN_PARENT);

        if ((relation != 0) && (relationid != 0)) {
            Log.d("asdasdasad ", " k " + relation+ " "+relationid);
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
    public static WheelSelector createWheelSelectorView(JSONObject jsonObject,Context context) {
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
        p.addRule(RelativeLayout.CENTER_IN_PARENT);

        Log.d("asdasdasad ", " X ");
        if ((relation != 0) && (relationid != 0)) {
            Log.d("asdasdasad ", " " + relation+ " "+relationid);
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
        return universitySelector;
    }
}
