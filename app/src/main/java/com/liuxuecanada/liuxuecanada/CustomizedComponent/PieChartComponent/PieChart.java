package com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.PieData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.PieDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter.PercentFormatter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.PieChartRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.ColorTemplate;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends PieRadarChartBase<PieData> {

    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    protected float mTransparentCircleRadiusPercent = 55f;
    /**
     * rect object that represents the bounds of the piechart, needed for drawing the circle
     */
    private RectF mCircleBox = new RectF();
    /**
     * flag indicating if the x-labels should be drawn or not
     */
    private boolean mDrawXLabels = true;
    /**
     * array that holds the width of each pie-slice in degrees
     */
    private float[] mDrawAngles;
    /**
     * array that holds the absolute angle in degrees of each slice
     */
    private float[] mAbsoluteAngles;
    /**
     * if true, the white hole inside the chart will be drawn
     */
    private boolean mDrawHole = true;
    /**
     * if true, the values inside the piechart are drawn as percent values
     */
    private boolean mUsePercentValues = false;
    /**
     * if true, the slices of the piechart are rounded
     */
    private boolean mDrawRoundedSlices = false;
    /**
     * variable for the text that is drawn in the center of the pie-chart. If this value is null, the default is "Total Value\n + getYValueSum()"
     */
    private String mCenterText = "";
    /**
     * indicates the size of the hole in the center of the piechart, default: radius / 2
     */
    private float mHoleRadiusPercent = 50f;
    /**
     * if enabled, centertext is drawn
     */
    private boolean mDrawCenterText = true;

    private boolean mCenterTextWordWrapEnabled = false;

    private float mCenterTextRadiusPercent = 1.f;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new PieChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataNotSet)
            return;

        mRenderer.drawData(canvas);

        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        mRenderer.drawExtras(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);
    }

    @Override
    public void calculateOffsets() {

        super.calculateOffsets();

        // prevent nullpointer when no data set
        if (mDataNotSet)
            return;

        float diameter = getDiameter();
        float radius = diameter / 2f;

        PointF c = getCenterOffsets();

        final List<PieDataSet> dataSets = mData.getDataSets();
        float maxShift = 0.f;
        for (int i = 0; i < dataSets.size(); i++) {
            final float shift = dataSets.get(i).getSelectionShift();
            if (shift > maxShift)
                maxShift = shift;
        }

        final float halfMaxShift = maxShift / 2.f;

        // create the circle box that will contain the pie-chart (the bounds of the pie-chart)
        mCircleBox.set(c.x - radius + halfMaxShift,
                c.y - radius + halfMaxShift,
                c.x + radius - halfMaxShift,
                c.y + radius - halfMaxShift);

    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        calcAngles();
    }

    /**
     * calculates the needed angles for the chart slices
     */
    private void calcAngles() {

        mDrawAngles = new float[mData.getYValCount()];
        mAbsoluteAngles = new float[mData.getYValCount()];

        List<PieDataSet> dataSets = mData.getDataSets();

        int cnt = 0;

        for (int i = 0; i < mData.getDataSetCount(); i++) {

            PieDataSet set = dataSets.get(i);
            List<Entry> entries = set.getYVals();

            for (int j = 0; j < entries.size(); j++) {

                mDrawAngles[cnt] = calcAngle(Math.abs(entries.get(j).getVal()));

                if (cnt == 0) {
                    mAbsoluteAngles[cnt] = mDrawAngles[cnt];
                } else {
                    mAbsoluteAngles[cnt] = mAbsoluteAngles[cnt - 1] + mDrawAngles[cnt];
                }

                cnt++;
            }
        }

    }

    /**
     * checks if the given index in the given DataSet is set for highlighting or not
     *
     * @param xIndex
     * @param dataSetIndex
     * @return
     */
    public boolean needsHighlight(int xIndex, int dataSetIndex) {

        // no highlight
        if (!valuesToHighlight() || dataSetIndex < 0)
            return false;

        for (int i = 0; i < mIndicesToHighlight.length; i++)

            // check if the xvalue for the given dataset needs highlight
            if (mIndicesToHighlight[i].getXIndex() == xIndex
                    && mIndicesToHighlight[i].getDataSetIndex() == dataSetIndex)
                return true;

        return false;
    }

    /**
     * calculates the needed angle for a given value
     *
     * @param value
     * @return
     */
    private float calcAngle(float value) {
        return value / mData.getYValueSum() * 360f;
    }

    @Override
    public int getIndexForAngle(float angle) {

        // take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        for (int i = 0; i < mAbsoluteAngles.length; i++) {
            if (mAbsoluteAngles[i] > a)
                return i;
        }

        return -1; // return -1 if no index found
    }

    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     *
     * @return
     */
    public float[] getDrawAngles() {
        return mDrawAngles;
    }

    /**
     * returns the absolute angles of the different chart slices (where the
     * slices end)
     *
     * @return
     */
    public float[] getAbsoluteAngles() {
        return mAbsoluteAngles;
    }

    /**
     * Returns true if the hole in the center of the PieChart is transparent,false if not.
     *
     * @return true if hole is transparent.
     */
    public boolean isHoleTransparent() {
        return ((PieChartRenderer) mRenderer).getPaintHole().getXfermode() != null;
    }

    /**
     * returns true if the hole in the center of the pie-chart is set to be visible, false if not
     *
     * @return
     */
    public boolean isDrawHoleEnabled() {
        return mDrawHole;
    }

    /**
     * returns the text that is drawn in the center of the pie-chart
     *
     * @return
     */
    public String getCenterText() {
        return mCenterText;
    }

    /**
     * sets the text that is displayed in the center of the pie-chart. By
     * default, the text is "Total Value + sumofallvalues"
     *
     * @param text
     */
    public void setCenterText(String text) {
        mCenterText = text;
    }

    /**
     * returns true if drawing the center text is enabled
     *
     * @return
     */
    public boolean isDrawCenterTextEnabled() {
        return mDrawCenterText;
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 2.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return 0;
    }

    @Override
    public float getRadius() {
        if (mCircleBox == null)
            return 0;
        else
            return Math.min(mCircleBox.width() / 2f, mCircleBox.height() / 2f);
    }

    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     *
     * @return
     */
    public RectF getCircleBox() {
        return mCircleBox;
    }

    /**
     * returns the center of the circlebox
     *
     * @return
     */
    public PointF getCenterCircleBox() {
        return new PointF(mCircleBox.centerX(), mCircleBox.centerY());
    }

    /**
     * Returns the size of the hole radius in percent of the total radius.
     *
     * @return
     */
    public float getHoleRadius() {
        return mHoleRadiusPercent;
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of the maximum radius (max = the radius of the whole chart), default 50%
     *
     * @param percent
     */
    public void setHoleRadius(final float percent) {
        mHoleRadiusPercent = percent;
    }

    /**
     * Sets the color the transparent-circle should have.
     *
     * @param color
     */
    public void setTransparentCircleColor(int color) {

        Paint p = ((PieChartRenderer) mRenderer).getPaintTransparentCircle();
        int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }

    public float getTransparentCircleRadius() {
        return mTransparentCircleRadiusPercent;
    }

    /**
     * sets the radius of the transparent circle that is drawn next to the hole in the piechart in percent of the maximum radius (max = the radius of the
     * whole chart), default 55% -> means 5% larger than the center-hole by default
     *
     * @param percent
     */
    public void setTransparentCircleRadius(final float percent) {
        mTransparentCircleRadiusPercent = percent;
    }

    /**
     * Sets the amount of transparency the transparent circle should have 0 = fully transparent, 255 = fully opaque. Default value is 100.
     *
     * @param alpha 0-255
     */
    public void setTransparentCircleAlpha(int alpha) {
        ((PieChartRenderer) mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }

    /**
     * returns true if drawing x-values is enabled, false if not
     *
     * @return
     */
    public boolean isDrawSliceTextEnabled() {
        return mDrawXLabels;
    }

    /**
     * Returns true if the chart is set to draw each end of a pie-slice "rounded".
     *
     * @return
     */
    public boolean isDrawRoundedSlicesEnabled() {
        return mDrawRoundedSlices;
    }

    /**
     * Returns true if using percentage values is enabled for the chart.
     *
     * @return
     */
    public boolean isUsePercentValuesEnabled() {
        return mUsePercentValues;
    }

    /**
     * should the center text be word wrapped? note that word wrapping takes a toll on performance if word wrapping is disabled, newlines are still respected
     */
    public boolean isCenterTextWordWrapEnabled() {
        return mCenterTextWordWrapEnabled;
    }

    /**
     * the rectangular radius of the bounding box for the center text, as a percentage of the pie hole default 1.f (100%)
     */
    public float getCenterTextRadiusPercent() {
        return mCenterTextRadiusPercent;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }

    public void setData(String[] mParties, float[] value) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < value.length; i++) {
            yVals1.add(new Entry(value[i], i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < mParties.length; i++) {
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
        this.setData(data);
        // undo all highlights
        this.highlightValues(null);
        this.invalidate();
    }
}
