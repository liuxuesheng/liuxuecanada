package com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarEntry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.BarHighlighter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces.BarDataProvider;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.BarChartRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.XAxisRendererBarChart;

import java.util.ArrayList;


public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider {

    /**
     * flag that enables or disables the highlighting arrow
     */
    private boolean mDrawHighlightArrow = false;

    /**
     * if set to true, all values are drawn above their bars, instead of below their top
     */
    private boolean mDrawValueAboveBar = true;

    /**
     * if set to true, a grey area is drawn behind each bar that indicates the maximum value
     */
    private boolean mDrawBarShadow = false;

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BarChartRenderer(this, mAnimator, mViewPortHandler);
        mXAxisRenderer = new XAxisRendererBarChart(mViewPortHandler, mXAxis, mLeftAxisTransformer, this);

        mHighlighter = new BarHighlighter(this);

        mXChartMin = -0.5f;
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        // increase deltax by 1 because the bars have a width of 1
        mDeltaX += 0.5f;

        // extend xDelta to make space for multiple datasets (if ther are one)
        mDeltaX *= mData.getDataSetCount();

        float groupSpace = mData.getGroupSpace();
        mDeltaX += mData.getXValCount() * groupSpace;
        mXChartMax = mDeltaX - mXChartMin;
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at the given touch point
     * inside the BarChart.
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public Highlight getHighlightByTouchPoint(float x, float y) {

        if (mDataNotSet || mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else
            return mHighlighter.getHighlight(x, y);
    }

    /**
     * returns true if drawing the highlighting arrow is enabled, false if not
     *
     * @return
     */
    public boolean isDrawHighlightArrowEnabled() {
        return mDrawHighlightArrow;
    }

    /**
     * If set to true, all values are drawn above their bars, instead of below their top.
     *
     * @param enabled
     */
    public void setDrawValueAboveBar(boolean enabled) {
        mDrawValueAboveBar = enabled;
    }

    /**
     * returns true if drawing values above bars is enabled, false if not
     *
     * @return
     */
    public boolean isDrawValueAboveBarEnabled() {
        return mDrawValueAboveBar;
    }


    /**
     * If set to true, a grey area is drawn behind each bar that indicates the maximum value. Enabling his will reduce
     * performance by about 50%.
     *
     * @param enabled
     */
    public void setDrawBarShadow(boolean enabled) {
        mDrawBarShadow = enabled;
    }

    /**
     * returns true if drawing shadows (maxvalue) for each bar is enabled, false if not
     *
     * @return
     */
    public boolean isDrawBarShadowEnabled() {
        return mDrawBarShadow;
    }

    @Override
    public BarData getBarData() {
        return mData;
    }

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on the chart.
     *
     * @return
     */
    @Override
    public int getLowestVisibleXIndex() {

        float step = mData.getDataSetCount();
        float div = (step <= 1) ? 1 : step + mData.getGroupSpace();

        float[] pts = new float[]{mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom()};

        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int) ((pts[0] <= getXChartMin()) ? 0 : (pts[0] / div) + 1);
    }

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible on the chart.
     *
     * @return
     */
    @Override
    public int getHighestVisibleXIndex() {

        float step = mData.getDataSetCount();
        float div = (step <= 1) ? 1 : step + mData.getGroupSpace();

        float[] pts = new float[]{mViewPortHandler.contentRight(), mViewPortHandler.contentBottom()};

        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int) ((pts[0] >= getXChartMax()) ? getXChartMax() / div : (pts[0] / div));
    }

    /**
     * @param year
     * @param value
     */
    public void setData(String[] year, float value[]) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < year.length; i++) {
            xVals.add(year[i % year.length]);
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
        this.setData(data);
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(2);
    }
}
