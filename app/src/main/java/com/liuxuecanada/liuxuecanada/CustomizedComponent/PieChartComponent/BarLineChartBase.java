package com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.Legend;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.XAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarLineScatterCandleBubbleData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarLineScatterCandleBubbleDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.ChartHighlighter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces.BarLineScatterCandleBubbleDataProvider;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.BarLineChartTouchListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.OnDrawListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.XAxisRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.YAxisRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Transformer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Utils;


@SuppressLint("RtlHardcoded")
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends BarLineScatterCandleBubbleDataSet<? extends Entry>>>
        extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {

    /**
     * the maximum number of entried to which values will be drawn
     */
    protected int mMaxVisibleCount = 100;
    /**
     * flag that indicates if pinch-zoom is enabled. if true, both x and y axis
     * can be scaled with 2 fingers, if false, x and y axis can be scaled
     * separately
     */
    protected boolean mPinchZoomEnabled = false;
    /**
     * flag that indicates if double tap zoom is enabled or not
     */
    protected boolean mDoubleTapToZoomEnabled = true;
    /**
     * flag that indicates if highlighting per dragging over a fully zoomed out
     * chart is enabled
     */
    protected boolean mHighlightPerDragEnabled = true;
    /**
     * if true, data filtering is enabled
     */
    protected boolean mFilterData = false;
    /**
     * paint object for the (by default) lightgrey background of the grid
     */
    protected Paint mGridBackgroundPaint;
    protected Paint mBorderPaint;
    /**
     * flag indicating if the grid background should be drawn or not
     */
    protected boolean mDrawGridBackground = true;
    protected boolean mDrawBorders = false;
    /**
     * Sets the minimum offset (padding) around the chart, defaults to 10
     */
    protected float mMinOffset = 10.f;
    /**
     * the listener for user drawing on the chart
     */
    protected OnDrawListener mDrawListener;
    /**
     * the object representing the labels on the y-axis, this object is prepared
     * in the pepareYLabels() method
     */
    protected YAxis mAxisLeft;
    protected YAxis mAxisRight;
    /**
     * the object representing the labels on the x-axis
     */
    protected XAxis mXAxis;
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;
    protected Transformer mLeftAxisTransformer;
    protected Transformer mRightAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;
    /**
     * flag that indicates if auto scaling on the y axis is enabled
     */
    private boolean mAutoScaleMinMaxEnabled = false;
    private Integer mAutoScaleLastLowestVisibleXIndex = null;
    private Integer mAutoScaleLastHighestVisibleXIndex = null;
    /**
     * if true, dragging is enabled for the chart
     */
    private boolean mDragEnabled = true;
    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;

    // /** the approximator object used for data filtering */
    // private Approximator mApproximator;
    // for performance tracking
    private long totalTime = 0;
    private long drawCycles = 0;
    /**
     * flag that indicates if a custom viewport offset has been set
     */
    private boolean mCustomViewPortEnabled = false;

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BarLineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);

        mXAxis = new XAxis();

        mLeftAxisTransformer = new Transformer(mViewPortHandler);
        mRightAxisTransformer = new Transformer(mViewPortHandler);

        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        mAxisRendererRight = new YAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);

        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

        mHighlighter = new ChartHighlighter(this);

        mChartTouchListener = new BarLineChartTouchListener(this, mViewPortHandler.getMatrixTouch());

        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setStyle(Paint.Style.FILL);
        // mGridBackgroundPaint.setColor(Color.WHITE);
        mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240)); // light
        // grey

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataNotSet)
            return;

        long starttime = System.currentTimeMillis();
        calcModulus();

        mXAxisRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);
        mRenderer.calcXBounds(this, mXAxis.mAxisLabelModulus);

        // execute all drawing commands
        drawGridBackground(canvas);

        if (mAxisLeft.isEnabled())
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum);
        if (mAxisRight.isEnabled())
            mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum);

        mXAxisRenderer.renderAxisLine(canvas);
        mAxisRendererLeft.renderAxisLine(canvas);
        mAxisRendererRight.renderAxisLine(canvas);

        if (mAutoScaleMinMaxEnabled) {
            final int lowestVisibleXIndex = getLowestVisibleXIndex();
            final int highestVisibleXIndex = getHighestVisibleXIndex();

            if (mAutoScaleLastLowestVisibleXIndex == null ||
                    mAutoScaleLastLowestVisibleXIndex != lowestVisibleXIndex ||
                    mAutoScaleLastHighestVisibleXIndex == null ||
                    mAutoScaleLastHighestVisibleXIndex != highestVisibleXIndex) {

                calcMinMax();
                calculateOffsets();

                mAutoScaleLastLowestVisibleXIndex = lowestVisibleXIndex;
                mAutoScaleLastHighestVisibleXIndex = highestVisibleXIndex;
            }
        }

        // make sure the graph values and grid cannot be drawn outside the
        // content-rect
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());

        mXAxisRenderer.renderGridLines(canvas);
        mAxisRendererLeft.renderGridLines(canvas);
        mAxisRendererRight.renderGridLines(canvas);

        if (mXAxis.isDrawLimitLinesBehindDataEnabled())
            mXAxisRenderer.renderLimitLines(canvas);

        if (mAxisLeft.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererLeft.renderLimitLines(canvas);

        if (mAxisRight.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererRight.renderLimitLines(canvas);

        mRenderer.drawData(canvas);

        if (!mXAxis.isDrawLimitLinesBehindDataEnabled())
            mXAxisRenderer.renderLimitLines(canvas);

        if (!mAxisLeft.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererLeft.renderLimitLines(canvas);

        if (!mAxisRight.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererRight.renderLimitLines(canvas);

        // if highlighting is enabled
        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        mRenderer.drawExtras(canvas);

        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
        mAxisRendererRight.renderAxisLabels(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);


        drawDescription(canvas);

        if (mLogEnabled) {
            long drawtime = (System.currentTimeMillis() - starttime);
            totalTime += drawtime;
            drawCycles += 1;
            long average = totalTime / drawCycles;
            Log.i(LOG_TAG, "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: "
                    + drawCycles);
        }
    }

    /**
     * RESET PERFORMANCE TRACKING FIELDS
     */
    public void resetTracking() {
        totalTime = 0;
        drawCycles = 0;
    }

    protected void prepareValuePxMatrix() {

        if (mLogEnabled)
            Log.i(LOG_TAG, "Preparing Value-Px Matrix, xmin: " + mXChartMin + ", xmax: "
                    + mXChartMax + ", xdelta: " + mDeltaX);

        mRightAxisTransformer.prepareMatrixValuePx(mXChartMin, mDeltaX, mAxisRight.mAxisRange,
                mAxisRight.mAxisMinimum);
        mLeftAxisTransformer.prepareMatrixValuePx(mXChartMin, mDeltaX, mAxisLeft.mAxisRange,
                mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {

        mRightAxisTransformer.prepareMatrixOffset(mAxisRight.isInverted());
        mLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted());
    }

    @Override
    public void notifyDataSetChanged() {

        if (mDataNotSet) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing... DATA NOT SET.");
            return;
        } else {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing...");
        }

        if (mRenderer != null)
            mRenderer.initBuffers();

        calcMinMax();

//        if (mAxisLeft.needsDefaultFormatter())
//            mAxisLeft.setValueFormatter(mDefaultFormatter);
//        if (mAxisRight.needsDefaultFormatter())
//            mAxisRight.setValueFormatter(mDefaultFormatter);

        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum);
        mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum);

        mXAxisRenderer.computeAxis(mData.getXValAverageLength(), mData.getXVals());

        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    @Override
    protected void calcMinMax() {

        if (mAutoScaleMinMaxEnabled)
            mData.calcMinMax(getLowestVisibleXIndex(), getHighestVisibleXIndex());

        float minLeft = mData.getYMin(YAxis.AxisDependency.LEFT);
        float maxLeft = mData.getYMax(YAxis.AxisDependency.LEFT);
        float minRight = mData.getYMin(YAxis.AxisDependency.RIGHT);
        float maxRight = mData.getYMax(YAxis.AxisDependency.RIGHT);

        float leftRange = Math.abs(maxLeft - (mAxisLeft.isStartAtZeroEnabled() ? 0 : minLeft));
        float rightRange = Math.abs(maxRight - (mAxisRight.isStartAtZeroEnabled() ? 0 : minRight));

        // in case all values are equal
        if (leftRange == 0f) {
            maxLeft = maxLeft + 1f;
            if (!mAxisLeft.isStartAtZeroEnabled())
                minLeft = minLeft - 1f;
        }

        if (rightRange == 0f) {
            maxRight = maxRight + 1f;
            if (!mAxisRight.isStartAtZeroEnabled())
                minRight = minRight - 1f;
        }

        float topSpaceLeft = leftRange / 100f * mAxisLeft.getSpaceTop();
        float topSpaceRight = rightRange / 100f * mAxisRight.getSpaceTop();
        float bottomSpaceLeft = leftRange / 100f * mAxisLeft.getSpaceBottom();
        float bottomSpaceRight = rightRange / 100f * mAxisRight.getSpaceBottom();

        mXChartMax = mData.getXVals().size() - 1;
        mDeltaX = Math.abs(mXChartMax - mXChartMin);

        // Consider sticking one of the edges of the axis to zero (0.0)

        if (mAxisLeft.isStartAtZeroEnabled()) {
            if (minLeft < 0.f && maxLeft < 0.f) {
                // If the values are all negative, let's stay in the negative zone
                mAxisLeft.mAxisMinimum = Math.min(0.f, !Float.isNaN(mAxisLeft.getAxisMinValue()) ? mAxisLeft.getAxisMinValue() : (minLeft - bottomSpaceLeft));
                mAxisLeft.mAxisMaximum = 0.f;
            } else if (minLeft >= 0.0) {
                // We have positive values only, stay in the positive zone
                mAxisLeft.mAxisMinimum = 0.f;
                mAxisLeft.mAxisMaximum = Math.max(0.f, !Float.isNaN(mAxisLeft.getAxisMaxValue()) ? mAxisLeft.getAxisMaxValue() : (maxLeft + topSpaceLeft));
            } else {
                // Stick the minimum to 0.0 or less, and maximum to 0.0 or more (startAtZero for negative/positive at the same time)
                mAxisLeft.mAxisMinimum = Math.min(0.f, !Float.isNaN(mAxisLeft.getAxisMinValue()) ? mAxisLeft.getAxisMinValue() : (minLeft - bottomSpaceLeft));
                mAxisLeft.mAxisMaximum = Math.max(0.f, !Float.isNaN(mAxisLeft.getAxisMaxValue()) ? mAxisLeft.getAxisMaxValue() : (maxLeft + topSpaceLeft));
            }
        } else {
            // Use the values as they are
            mAxisLeft.mAxisMinimum = !Float.isNaN(mAxisLeft.getAxisMinValue()) ? mAxisLeft.getAxisMinValue() : (minLeft - bottomSpaceLeft);
            mAxisLeft.mAxisMaximum = !Float.isNaN(mAxisLeft.getAxisMaxValue()) ? mAxisLeft.getAxisMaxValue() : (maxLeft + topSpaceLeft);
        }

        if (mAxisRight.isStartAtZeroEnabled()) {
            if (minRight < 0.f && maxRight < 0.f) {
                // If the values are all negative, let's stay in the negative zone
                mAxisRight.mAxisMinimum = Math.min(0.f, !Float.isNaN(mAxisRight.getAxisMinValue()) ? mAxisRight.getAxisMinValue() : (minRight - bottomSpaceRight));
                mAxisRight.mAxisMaximum = 0.f;
            } else if (minRight >= 0.f) {
                // We have positive values only, stay in the positive zone
                mAxisRight.mAxisMinimum = 0.f;
                mAxisRight.mAxisMaximum = Math.max(0.f, !Float.isNaN(mAxisRight.getAxisMaxValue()) ? mAxisRight.getAxisMaxValue() : (maxRight + topSpaceRight));
            } else {
                // Stick the minimum to 0.0 or less, and maximum to 0.0 or more (startAtZero for negative/positive at the same time)
                mAxisRight.mAxisMinimum = Math.min(0.f, !Float.isNaN(mAxisRight.getAxisMinValue()) ? mAxisRight.getAxisMinValue() : (minRight - bottomSpaceRight));
                mAxisRight.mAxisMaximum = Math.max(0.f, !Float.isNaN(mAxisRight.getAxisMaxValue()) ? mAxisRight.getAxisMaxValue() : (maxRight + topSpaceRight));
            }
        } else {
            mAxisRight.mAxisMinimum = !Float.isNaN(mAxisRight.getAxisMinValue()) ? mAxisRight.getAxisMinValue() : (minRight - bottomSpaceRight);
            mAxisRight.mAxisMaximum = !Float.isNaN(mAxisRight.getAxisMaxValue()) ? mAxisRight.getAxisMaxValue() : (maxRight + topSpaceRight);
        }

        mAxisLeft.mAxisRange = Math.abs(mAxisLeft.mAxisMaximum - mAxisLeft.mAxisMinimum);
        mAxisRight.mAxisRange = Math.abs(mAxisRight.mAxisMaximum - mAxisRight.mAxisMinimum);
    }

    @Override
    public void calculateOffsets() {

        if (!mCustomViewPortEnabled) {

            float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

            // setup offsets for legend
            if (mLegend != null && mLegend.isEnabled()) {

                if (mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART
                        || mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {

                    offsetRight += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth()
                            * mLegend.getMaxSizePercent())
                            + mLegend.getXOffset() * 2f;

                } else if (mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART
                        || mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {

                    offsetLeft += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth()
                            * mLegend.getMaxSizePercent())
                            + mLegend.getXOffset() * 2f;

                } else if (mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT
                        || mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT
                        || mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {

                    // It's possible that we do not need this offset anymore as it
                    //   is available through the extraOffsets, but changing it can mean
                    //   changing default visibility for existing apps.
                    float yOffset = mLegend.mTextHeightMax;

                    offsetBottom += Math.min(mLegend.mNeededHeight + yOffset,
                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                } else if (mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT
                        || mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT
                        || mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {

                    // It's possible that we do not need this offset anymore as it
                    //   is available through the extraOffsets, but changing it can mean
                    //   changing default visibility for existing apps.
                    float yOffset = mLegend.mTextHeightMax;

                    offsetTop += Math.min(mLegend.mNeededHeight + yOffset,
                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                }
            }

            // offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft
                        .getPaintAxisLabels());
            }

            if (mAxisRight.needsOffset()) {
                offsetRight += mAxisRight.getRequiredWidthSpace(mAxisRendererRight
                        .getPaintAxisLabels());
            }

            if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

                float xlabelheight = mXAxis.mLabelHeight * 2f;

                // offsets for x-labels
                if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {

                    offsetBottom += xlabelheight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {

                    offsetTop += xlabelheight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {

                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight;
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
                Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                        + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
            }
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /**
     * calculates the modulus for x-labels and grid
     */
    protected void calcModulus() {

        if (mXAxis == null || !mXAxis.isEnabled())
            return;

        if (!mXAxis.isAxisModulusCustom()) {

            float[] values = new float[9];
            mViewPortHandler.getMatrixTouch().getValues(values);

            mXAxis.mAxisLabelModulus = (int) Math
                    .ceil((mData.getXValCount() * mXAxis.mLabelWidth)
                            / (mViewPortHandler.contentWidth() * values[Matrix.MSCALE_X]));

        }

        if (mLogEnabled)
            Log.i(LOG_TAG, "X-Axis modulus: " + mXAxis.mAxisLabelModulus + ", x-axis label width: "
                    + mXAxis.mLabelWidth + ", content width: " + mViewPortHandler.contentWidth());

        if (mXAxis.mAxisLabelModulus < 1)
            mXAxis.mAxisLabelModulus = 1;
    }

    /**
     * draws the grid background
     */
    protected void drawGridBackground(Canvas c) {

        if (mDrawGridBackground) {

            // draw the grid background
            c.drawRect(mViewPortHandler.getContentRect(), mGridBackgroundPaint);
        }

        if (mDrawBorders) {
            c.drawRect(mViewPortHandler.getContentRect(), mBorderPaint);
        }
    }

    /**
     * Returns the Transformer class that contains all matrices and is
     * responsible for transforming values into pixels on the screen and
     * backwards.
     *
     * @return
     */
    public Transformer getTransformer(YAxis.AxisDependency which) {
        if (which == YAxis.AxisDependency.LEFT)
            return mLeftAxisTransformer;
        else
            return mRightAxisTransformer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (mChartTouchListener == null || mDataNotSet)
            return false;

        // check if touch gestures are enabled
        if (!mTouchEnabled)
            return false;
        else
            return mChartTouchListener.onTouch(this, event);
    }

    @Override
    public void computeScroll() {

        if (mChartTouchListener instanceof BarLineChartTouchListener)
            ((BarLineChartTouchListener) mChartTouchListener).computeScroll();
    }

    /**
     * Zooms in by 1.4f, into the charts center. center.
     */
    public void zoomIn() {
        Matrix save = mViewPortHandler.zoomIn(getWidth() / 2f, -(getHeight() / 2f));
        mViewPortHandler.refresh(save, this, true);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms out by 0.7f, from the charts center. center.
     */
    public void zoomOut() {
        Matrix save = mViewPortHandler.zoomOut(getWidth() / 2f, -(getHeight() / 2f));
        mViewPortHandler.refresh(save, this, true);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms in or out by the given scale factor. x and y are the coordinates
     * (in pixels) of the zoom center.
     *
     * @param scaleX if < 1f --> zoom out, if > 1f --> zoom in
     * @param scaleY if < 1f --> zoom out, if > 1f --> zoom in
     * @param x
     * @param y
     */
    public void zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = mViewPortHandler.zoom(scaleX, scaleY, x, -y);
        mViewPortHandler.refresh(save, this, true);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Returns the position (in pixels) the provided Entry has inside the chart
     * view or null, if the provided Entry is null.
     *
     * @param e
     * @return
     */
    public PointF getPosition(Entry e, YAxis.AxisDependency axis) {

        if (e == null)
            return null;

        float[] vals = new float[]{
                e.getXIndex(), e.getVal()
        };

        getTransformer(axis).pointValuesToPixel(vals);

        return new PointF(vals[0], vals[1]);
    }

    /**
     * sets the number of maximum visible drawn values on the chart only active
     * when setDrawValues() is enabled
     *
     * @param count
     */
    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }


    public boolean isHighlightPerDragEnabled() {
        return mHighlightPerDragEnabled;
    }

    /**
     * Returns true if dragging is enabled for the chart, false if not.
     *
     * @return
     */
    public boolean isDragEnabled() {
        return mDragEnabled;
    }


    public boolean isScaleXEnabled() {
        return mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return mScaleYEnabled;
    }


    /**
     * Returns true if zooming via double-tap is enabled false if not.
     *
     * @return
     */
    public boolean isDoubleTapToZoomEnabled() {
        return mDoubleTapToZoomEnabled;
    }

    /**
     * set this to true to draw the grid background, false if not
     *
     * @param enabled
     */
    public void setDrawGridBackground(boolean enabled) {
        mDrawGridBackground = enabled;
    }


    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the
     * selected value at the given touch point inside the Line-, Scatter-, or
     * CandleStick-Chart.
     *
     * @param x
     * @param y
     * @return
     */
    public Highlight getHighlightByTouchPoint(float x, float y) {

        if (mDataNotSet || mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else
            return mHighlighter.getHighlight(x, y);
    }

    /**
     * returns the DataSet object displayed at the touched position of the chart
     *
     * @param x
     * @param y
     * @return
     */
    public BarLineScatterCandleBubbleDataSet<? extends Entry> getDataSetByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return mData.getDataSetByIndex(h.getDataSetIndex());
        }
        return null;
    }

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on
     * the chart.
     *
     * @return
     */
    @Override
    public int getLowestVisibleXIndex() {
        float[] pts = new float[]{
                mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom()
        };
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (pts[0] <= 0) ? 0 : (int) (pts[0] + 1);
    }

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible
     * on the chart.
     *
     * @return
     */
    @Override
    public int getHighestVisibleXIndex() {
        float[] pts = new float[]{
                mViewPortHandler.contentRight(), mViewPortHandler.contentBottom()
        };
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (pts[0] >= mData.getXValCount()) ? mData.getXValCount() - 1 : (int) pts[0];
    }

    /**
     * returns the current x-scale factor
     */
    public float getScaleX() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleX();
    }

    /**
     * returns the current y-scale factor
     */
    public float getScaleY() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleY();
    }

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    public boolean isFullyZoomedOut() {
        return mViewPortHandler.isFullyZoomedOut();
    }

    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the
     * top axis.
     *
     * @return
     */
    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    /**
     * Returns the right y-axis object. In the horizontal bar-chart, this is the
     * bottom axis.
     *
     * @return
     */
    public YAxis getAxisRight() {
        return mAxisRight;
    }

    /**
     * Returns the y-axis object to the corresponding AxisDependency. In the
     * horizontal bar-chart, LEFT == top, RIGHT == BOTTOM
     *
     * @param axis
     * @return
     */
    public YAxis getAxis(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT)
            return mAxisLeft;
        else
            return mAxisRight;
    }

    @Override
    public boolean isInverted(YAxis.AxisDependency axis) {
        return getAxis(axis).isInverted();
    }

    /**
     * Returns the object representing all x-labels, this method can be used to
     * acquire the XAxis object and modify it (e.g. change the position of the
     * labels)
     *
     * @return
     */
    public XAxis getXAxis() {
        return mXAxis;
    }


    /**
     * If set to true, both x and y axis can be scaled simultaneously with 2 fingers, if false,
     * x and y axis can be scaled separately. default: false
     *
     * @param enabled
     */
    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    /**
     * returns true if pinch-zoom is enabled, false if not
     *
     * @return
     */
    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }


    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }


    public float getYChartMax() {
        return Math.max(mAxisLeft.mAxisMaximum, mAxisRight.mAxisMaximum);
    }

    public float getYChartMin() {
        return Math.min(mAxisLeft.mAxisMinimum, mAxisRight.mAxisMinimum);
    }

    /**
     * Returns true if either the left or the right or both axes are inverted.
     *
     * @return
     */
    public boolean isAnyAxisInverted() {
        if (mAxisLeft.isInverted())
            return true;
        if (mAxisRight.isInverted())
            return true;
        return false;
    }

    @Override
    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                mGridBackgroundPaint = p;
                break;
        }
    }

    @Override
    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null)
            return p;

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                return mGridBackgroundPaint;
        }

        return null;
    }
}

