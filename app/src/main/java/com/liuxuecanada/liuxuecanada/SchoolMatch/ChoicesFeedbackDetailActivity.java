package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.Legend;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.OnChartValueSelectedListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.BarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.HorizontalBarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.PieChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent.RadarChart;
import com.liuxuecanada.liuxuecanada.R;


public class ChoicesFeedbackDetailActivity extends FragmentActivity implements OnChartValueSelectedListener {

    //这是测试数据等待从服务器读入数据
    protected String[] mParties = new String[]{"Lakehead University", "York University", "Toronto University", "university of alberta"};
    protected float[] mValue = new float[]{30, 40, 30, 10};
    protected float[] mValue1 = new float[]{40, 23, 60, 43};
    protected float[] mValue2 = new float[]{30, 41, 65, 40};
    protected String[] year = new String[]{"2010", "2011", "2012", "2013", "2014", "2015"};
    protected float[] mYearValue = new float[]{30, 40, 30, 10, 25, 26};
    private String[] mCategory = new String[]{"GPA", "University Rank", "IELTS", "Other"};
    private PieChart mPieChart;
    private RadarChart mRadarChart;
    private BarChart mBarChart;
    private HorizontalBarChart mHorizontalBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //从FeedbackViewAdapter.java 中的 onBindViewHolder 方法获取intent对象的参数
        Intent intent = getIntent();
        //生成饼图对象
        if (intent.hasExtra("PieChart")) {
            setContentView(R.layout.activity_choicesfeedback_piechart);
            mPieChart = (PieChart) findViewById(R.id.piechartID);

            //为生成的图形赋值：学校名称和对应的概率
            mPieChart.setCenterText("选校概率分析图");
            mPieChart.setHoleRadius(58f);
            mPieChart.setTransparentCircleColor(Color.WHITE);
            mPieChart.setTransparentCircleAlpha(110);
            mPieChart.setTransparentCircleRadius(61f);
            mPieChart.setRotationEnabled(true);
            mPieChart.setOnChartValueSelectedListener(this);
            mPieChart.setData(mParties, mValue);


        }
        //生成雷达图对象
        else if (intent.hasExtra("RadarChart")) {
            setContentView(R.layout.activity_choicesfeedback_radarchart);
            mRadarChart = (RadarChart) findViewById(R.id.radarchartID);

            mRadarChart.setDescription("");
            mRadarChart.setWebLineWidth(1.5f);
            mRadarChart.setWebLineWidthInner(0.75f);
            mRadarChart.setWebAlpha(100);
            XAxis xAxis = mRadarChart.getXAxis();
            xAxis.setTextSize(9f);
            YAxis yAxis = mRadarChart.getYAxis();
            yAxis.setLabelCount(5, false);
            yAxis.setTextSize(9f);
            yAxis.setStartAtZero(true);
            Legend l = mRadarChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(5f);
            mRadarChart.setData(mCategory, mValue1, mValue2);

        } else if (intent.hasExtra("BarChart")) {
            setContentView(R.layout.activity_choicesfeedback_barchart);
            mBarChart = (BarChart) findViewById(R.id.barchartID);

            mBarChart.setDrawBarShadow(false);
            mBarChart.setDrawValueAboveBar(true);
            mBarChart.setDescription("");
            // if more than 60 entries are displayed in the chart, no values will be drawn
            mBarChart.setMaxVisibleValueCount(60);
            // scaling can now only be done on x- and y-axis separately
            mBarChart.setPinchZoom(false);
            // draw shadows for each bar that show the maximum value
            // mChart.setDrawBarShadow(true);
            // mChart.setDrawXLabels(false);
            mBarChart.setDrawGridBackground(false);
            // mChart.setDrawYLabels(false);
            XAxis xAxis = mBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(2);
            mBarChart.setData(year, mYearValue);

        } else if (intent.hasExtra("HorizontalBarChart")) {
            setContentView(R.layout.activity_choicesfeedback_horizontalbarchart);
            mHorizontalBarChart = (HorizontalBarChart) findViewById(R.id.horizontalBarChartID);

            mHorizontalBarChart.setDrawBarShadow(false);
            mHorizontalBarChart.setDrawValueAboveBar(true);
            mHorizontalBarChart.setDescription("");
            // if more than 60 entries are displayed in the chart, no values will be drawn
            mHorizontalBarChart.setMaxVisibleValueCount(60);
            // scaling can now only be done on x- and y-axis separately
            mHorizontalBarChart.setPinchZoom(false);
            // draw shadows for each bar that show the maximum value
            // mChart.setDrawBarShadow(true);
            // mChart.setDrawXLabels(false);
            mHorizontalBarChart.setDrawGridBackground(false);
            XAxis xl = mHorizontalBarChart.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(true);
            xl.setGridLineWidth(0.3f);
            YAxis yl = mHorizontalBarChart.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setGridLineWidth(0.3f);
            // yl.setInverted(true);
            YAxis yr = mHorizontalBarChart.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            //yr.setInverted(true);
            mHorizontalBarChart.setData(year, mYearValue);
            mHorizontalBarChart.animateY(2500);
        }
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