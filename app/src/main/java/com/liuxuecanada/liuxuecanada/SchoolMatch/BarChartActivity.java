package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarEntry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.BarChart;
import com.liuxuecanada.liuxuecanada.R;

import java.util.ArrayList;

public class BarChartActivity extends FragmentActivity {

    protected BarChart mChart;


    protected String[] year = new String[]{"2010", "2011", "2012", "2013", "2014", "2015"};
    protected float[] mValue = new float[]{30, 40, 30, 10, 25, 26};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choicesfeedback_barchart);


        mChart = (BarChart) findViewById(R.id.chart1);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        setData(year.length, mValue);


    }


    private void setData(int count, float value[]) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(year[i % count]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < value.length; i++) {
            yVals1.add(new BarEntry(value[i], i));
        }


        BarDataSet set1 = new BarDataSet(yVals1, "年度统计");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }


}

