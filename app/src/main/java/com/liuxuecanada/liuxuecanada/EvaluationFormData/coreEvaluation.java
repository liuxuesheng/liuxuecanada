package com.liuxuecanada.liuxuecanada.EvaluationFormData;

import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class coreEvaluation {

    public static JSONArray createPage1() {
        JSONArray jsonArray0 = new JSONArray();

        JSONObject item0 = new JSONObject();
        try {
            item0.put("id", 730);
            item0.put("type", "textview");
            item0.put("name", "学术背景");
            item0.put("relation", 0);
            item0.put("relationid", 0);
            item0.put("size", 24);
            item0.put("inlayout", "top");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item1 = new JSONObject();
        try {
            item1.put("id", 731);
            item1.put("type", "textview");
            item1.put("name", "进度");
            item1.put("relation", RelativeLayout.RIGHT_OF);
            item1.put("relationid", 730);
            item1.put("size", 18);
            item1.put("inlayout", "top");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item2 = new JSONObject();
        try {
            item2.put("id", 732);
            item2.put("type", "progressbar");
            item2.put("relation", RelativeLayout.RIGHT_OF);
            item2.put("relationid", 730);
            item2.put("inlayout", "top");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item3 = new JSONObject();
        try {
            item3.put("id", 649);
            item3.put("type", "textview");
            item3.put("name", "下一步");
            item3.put("relation", 0);
            item3.put("relationid", 0);
            item3.put("size", 18);
            item3.put("inlayout", "bottom");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item4 = new JSONObject();
        try {
            item4.put("id", 309);
            item4.put("type", "wheelselectorview");
            item4.put("name", "universities");
            item4.put("values", "北京大学,清华大学,复旦大学,武汉大学,中国人民大学,浙江大学,上海交通大学,南京大学,中国科学技术大学,国防科学技术大学");
            item4.put("relation", RelativeLayout.BELOW);
            item4.put("relationid", 311);
            item4.put("inlayout", "middle");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item5 = new JSONObject();
        try {
            item5.put("id", 310);
            item5.put("type", "wheelselectorview");
            item5.put("name", "majorcategory");
            item5.put("values", "哲学,经济学,法学,教育学,文学,历史学,理学,工学,农学,医学,军事学,管理学");
            item5.put("relation", RelativeLayout.BELOW);
            item5.put("relationid", 312);
            item5.put("inlayout", "middle");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item6 = new JSONObject();
        try {
            item6.put("id", 311);
            item6.put("type", "textview");
            item6.put("name", "毕业院校");
            item6.put("relation", 0);
            item6.put("relationid", 0);
            item6.put("size", 18);
            item6.put("inlayout", "middle");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JSONObject item7 = new JSONObject();
        try {
            item7.put("id", 312);
            item7.put("type", "textview");
            item7.put("name", "主修专业");
            item7.put("relation", RelativeLayout.BELOW);
            item7.put("relationid", 309);
            item7.put("size", 18);
            item7.put("inlayout", "middle");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        jsonArray0.put(item0);
        jsonArray0.put(item1);
        jsonArray0.put(item2);
        jsonArray0.put(item3);
        jsonArray0.put(item4);
        jsonArray0.put(item5);
        jsonArray0.put(item6);
        jsonArray0.put(item7);

        return jsonArray0;
    }
}
