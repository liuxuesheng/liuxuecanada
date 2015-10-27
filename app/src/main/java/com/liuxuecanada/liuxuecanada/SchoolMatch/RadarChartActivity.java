package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.RadarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.Legend;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.RadarData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.RadarDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.ColorTemplate;
import com.liuxuecanada.liuxuecanada.R;

import java.util.ArrayList;

public class RadarChartActivity extends FragmentActivity {

    private RadarChart mChart;
    private Typeface tf;
    private String[] mParties = new String[] {"GPA", "University Rank", "IELTS", "Other"};
    protected float[] mValue1 = new float[]{40,23,60,43};
    protected float[] mValue2 = new float[]{30,41,65,40};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choicesfeedback_radarchart);

        mChart = (RadarChart) findViewById(R.id.chart1);


        mChart.setDescription("");

        mChart.setWebLineWidth(1.5f);
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebAlpha(100);


        setData(mParties,mValue1,mValue2);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(9f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(tf);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setStartAtZero(true);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }


    public void setData(String[] party, float[] value1,float[] value2) {

        int count = party.length;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < value1.length; i++) {
            yVals1.add(new Entry(value1[i], i));
        }

        for (int i = 0; i < value2.length; i++) {
            yVals2.add(new Entry(value2[i], i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++)
            xVals.add(party[i % party.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "黄晓明");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "周杰伦");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(false);

        mChart.setData(data);

        mChart.invalidate();
    }
}

