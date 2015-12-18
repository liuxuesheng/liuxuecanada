package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
    protected float[] mValue1 = new float[]{90, 85, 60, 83, 92, 78};
    protected float[] mValue2 = new float[]{95, 98, 90, 92, 99, 100};
    protected String[] year = new String[]{"6月", "7月", "8月", "9月", "10月", "11月"};
    protected float[] mYearValue = new float[]{1130, 1150, 1110, 1140, 1210, 1205};
    private String[] mCategory = new String[]{"语文", "数学", "英语", "物理", "化学", "生物"};
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

            mRadarChart.setWebLineWidth(1.5f);
            mRadarChart.setWebLineWidthInner(3.75f);
            mRadarChart.setWebAlpha(100);
            mRadarChart.setWebColor(Color.BLUE);
            mRadarChart.setData(mCategory, mValue1, mValue2);

        }
        //生成一般柱状图
        else if (intent.hasExtra("BarChart")) {
            setContentView(R.layout.activity_choicesfeedback_barchart);
            mBarChart = (BarChart) findViewById(R.id.barchartID);

            mBarChart.setDrawValueAboveBar(true);
            //比较的参数不能多余20个
            mBarChart.setMaxVisibleValueCount(20);
            // scaling can now only be done on x- and y-axis separately
            mBarChart.setPinchZoom(true);
            mBarChart.setDrawGridBackground(true);
            mBarChart.setData(year, mYearValue);

        }
        //生成水平柱状图
        else if (intent.hasExtra("HorizontalBarChart")) {
            setContentView(R.layout.activity_choicesfeedback_horizontalbarchart);
            mHorizontalBarChart = (HorizontalBarChart) findViewById(R.id.horizontalBarChartID);

            mHorizontalBarChart.setDrawBarShadow(false);
            mHorizontalBarChart.setDrawValueAboveBar(true);
            //比较的参数不能多余20个
            mHorizontalBarChart.setMaxVisibleValueCount(20);
            // scaling can now only be done on x- and y-axis separately
            mHorizontalBarChart.setPinchZoom(true);
            mHorizontalBarChart.setDrawGridBackground(true);
            mHorizontalBarChart.setData(year, mYearValue);
            mHorizontalBarChart.animateY(1000);
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.getVal() + ", xIndex: " + e.getXIndex() + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}