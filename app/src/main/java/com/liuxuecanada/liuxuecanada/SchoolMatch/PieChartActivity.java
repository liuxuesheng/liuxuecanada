package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.charts.PieChart;
import com.liuxuecanada.liuxuecanada.Utils.data.Entry;
import com.liuxuecanada.liuxuecanada.Utils.data.PieData;
import com.liuxuecanada.liuxuecanada.Utils.data.PieDataSet;
import com.liuxuecanada.liuxuecanada.Utils.formatter.PercentFormatter;
import com.liuxuecanada.liuxuecanada.Utils.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.Utils.utils.ColorTemplate;
import com.liuxuecanada.liuxuecanada.Utils.listener.OnChartValueSelectedListener;
import java.util.ArrayList;

public class PieChartActivity extends Activity implements OnChartValueSelectedListener{

    private  PieChart mChart;
    protected String[] mParties = new String[] {
            "Lakehead University", "York University", "Toronto University", "university of alberta"
    };

    protected float[] mValue = new float[]{30,40,30,10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicesfeedback_piechart);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        //mChart.setDescription("hello worlde");

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(false);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);



        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.setCenterText("选校饼图");

        setData(mParties.length, mValue);


    }


    private void setData(int count, float[] value) {


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < value.length; i++) {
            yVals1.add(new Entry(value[i], i));
        }


        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add(mParties[i % mParties.length]);
        }


        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);


        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
