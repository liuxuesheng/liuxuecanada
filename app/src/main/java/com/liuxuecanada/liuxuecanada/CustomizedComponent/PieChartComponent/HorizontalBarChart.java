package com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.Legend;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarEntry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.HorizontalBarHighlighter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.HorizontalBarChartRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.XAxisRendererHorizontalBarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.YAxisRendererHorizontalBarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.TransformerHorizontalBarChart;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Utils;

import java.util.ArrayList;

public class HorizontalBarChart extends BarChart {

    public HorizontalBarChart(Context context) {
        super(context);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mLeftAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);
        mRightAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);

        mRenderer = new HorizontalBarChartRenderer(this, mAnimator, mViewPortHandler);
        mHighlighter = new HorizontalBarHighlighter(this);

        mAxisRendererLeft = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        mAxisRendererRight = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisRight, mRightAxisTransformer);
        mXAxisRenderer = new XAxisRendererHorizontalBarChart(mViewPortHandler, mXAxis, mLeftAxisTransformer, this);
    }

    @Override
    public void calculateOffsets() {

        float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

        // setup offsets for legend
        if (mLegend != null && mLegend.isEnabled()) {

            if (mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART || mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {

                offsetRight += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent())
                        + mLegend.getXOffset() * 2f;

            } else if (mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART
                    || mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {

                offsetLeft += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent())
                        + mLegend.getXOffset() * 2f;

            } else if (mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT
                    || mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT
                    || mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {

                // It's possible that we do not need this offset anymore as it
                //   is available through the extraOffsets, but changing it can mean
                //   changing default visibility for existing apps.
                float yOffset = mLegend.mTextHeightMax * 2.f;

                offsetBottom += Math.min(mLegend.mNeededHeight + yOffset, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

            } else if (mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT
                    || mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT
                    || mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {

                // It's possible that we do not need this offset anymore as it
                //   is available through the extraOffsets, but changing it can mean
                //   changing default visibility for existing apps.
                float yOffset = mLegend.mTextHeightMax * 2.f;

                offsetTop += Math.min(mLegend.mNeededHeight + yOffset, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
            }
        }

        // offsets for y-labels
        if (mAxisLeft.needsOffset()) {
            offsetTop += mAxisLeft.getRequiredHeightSpace(mAxisRendererLeft.getPaintAxisLabels());
        }

        if (mAxisRight.needsOffset()) {
            offsetBottom += mAxisRight.getRequiredHeightSpace(mAxisRendererRight.getPaintAxisLabels());
        }

        float xlabelwidth = mXAxis.mLabelWidth;

        if (mXAxis.isEnabled()) {

            // offsets for x-labels
            if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {

                offsetLeft += xlabelwidth;

            } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {

                offsetRight += xlabelwidth;

            } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {

                offsetLeft += xlabelwidth;
                offsetRight += xlabelwidth;
            }
        }

        offsetTop += getExtraTopOffset();
        offsetRight += getExtraRightOffset();
        offsetBottom += getExtraBottomOffset();
        offsetLeft += getExtraLeftOffset();

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft),
                Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight),
                Math.max(minOffset, offsetBottom));

        if (mLogEnabled) {
            Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: "
                    + offsetBottom);
            Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    @Override
    protected void prepareValuePxMatrix() {
        mRightAxisTransformer.prepareMatrixValuePx(mAxisRight.mAxisMinimum, mAxisRight.mAxisRange, mDeltaX, mXChartMin);
        mLeftAxisTransformer.prepareMatrixValuePx(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisRange, mDeltaX, mXChartMin);
    }

    @Override
    protected void calcModulus() {
        float[] values = new float[9];
        mViewPortHandler.getMatrixTouch().getValues(values);

        mXAxis.mAxisLabelModulus = (int) Math.ceil((mData.getXValCount() * mXAxis.mLabelHeight)
                / (mViewPortHandler.contentHeight() * values[Matrix.MSCALE_Y]));

        if (mXAxis.mAxisLabelModulus < 1)
            mXAxis.mAxisLabelModulus = 1;
    }

    @Override
    public PointF getPosition(Entry e, YAxis.AxisDependency axis) {

        if (e == null)
            return null;

        float[] vals = new float[] { e.getVal(), e.getXIndex() };

        getTransformer(axis).pointValuesToPixel(vals);

        return new PointF(vals[0], vals[1]);
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
            return mHighlighter.getHighlight(y, x); // switch x and y
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

        float[] pts = new float[] { mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom() };

        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int) (((pts[1] <= 0) ? 0 : ((pts[1])) / div) + 1);
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

        float[] pts = new float[] { mViewPortHandler.contentLeft(), mViewPortHandler.contentTop() };

        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int) ((pts[1] >= getXChartMax()) ? getXChartMax() / div : (pts[1] / div));
    }

    /**
     *
     * @param year
     * @param value
     */
    public void setData(String[] year, float value[]) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < year.length; i++) {
            xVals.add(year[i % year.length]);
            yVals1.add(new BarEntry(value[i], i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "年度统计");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        this.setData(data);
        XAxis xl = this.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);
        YAxis yl = this.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        YAxis yr = this.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
    }
}

