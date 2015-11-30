package com.liuxuecanada.liuxuecanada.CustomizedComponent.PieChartComponent;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.animation.ChartAnimator;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.Legend;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.ChartData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.DataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.Entry;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter.DefaultValueFormatter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter.ValueFormatter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.ChartHighlighter;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.highlight.Highlight;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces.ChartInterface;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.ChartTouchListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.OnChartGestureListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.listener.OnChartValueSelectedListener;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.DataRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.renderer.LegendRenderer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Utils;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

@SuppressLint("NewApi")
public abstract class Chart<T extends ChartData<? extends DataSet<? extends Entry>>> extends ViewGroup implements ChartInterface {

    public static final String LOG_TAG = "LiuxueCanadaISO";
    /**
     * paint for the grid background (only line and barchart)
     */
    public static final int PAINT_GRID_BACKGROUND = 4;
    /**
     * paint for the info text that is displayed when there are no values in the
     * chart
     */
    public static final int PAINT_INFO = 7;
    /**
     * paint for the description text in the bottom right corner
     */
    public static final int PAINT_DESCRIPTION = 11;
    /**
     * paint for the hole in the middle of the pie chart
     */
    public static final int PAINT_HOLE = 13;
    /**
     * paint for the text in the middle of the pie chart
     */
    public static final int PAINT_CENTER_TEXT = 14;
    /**
     * paint used for the legend
     */
    public static final int PAINT_LEGEND_LABEL = 18;
    /**
     * flag that indicates if logging is enabled or not
     */
    protected boolean mLogEnabled = false;
    /**
     * object that holds all data that was originally set for the chart, before it was modified or any filtering algorithms had been applied
     */
    protected T mData = null;
    /**
     * default value-formatter, number of digits depends on provided chart-data
     */
    protected ValueFormatter mDefaultFormatter;
    /**
     * paint object used for drawing the description text in the bottom right
     * corner of the chart
     */
    protected Paint mDescPaint;
    /**
     * paint object for drawing the information text when there are no values in
     * the chart
     */
    protected Paint mInfoPaint;
    /**
     * description text that appears in the bottom right corner of the chart
     */
    protected String mDescription = "";
    /**
     * flag that indicates if the chart has been fed with data yet
     */
    protected boolean mDataNotSet = true;
    /**
     * if true, units are drawn next to the values in the chart
     */
    protected boolean mDrawUnitInChart = false;
    /**
     * the number of x-values the chart displays
     */
    protected float mDeltaX = 1f;
    protected float mXChartMin = 0f;
    protected float mXChartMax = 0f;
    /**
     * if true, touch gestures are enabled on the chart
     */
    protected boolean mTouchEnabled = true;
    /**
     * the legend object containing all data associated with the legend
     */
    protected Legend mLegend;
    /**
     * listener that is called when a value on the chart is selected
     */
    protected OnChartValueSelectedListener mSelectionListener;
    protected ChartTouchListener mChartTouchListener;
    protected LegendRenderer mLegendRenderer;
    /**
     * object responsible for rendering the data
     */
    protected DataRenderer mRenderer;
    protected ChartHighlighter mHighlighter;
    /**
     * object that manages the bounds and drawing constraints of the chart
     */
    protected ViewPortHandler mViewPortHandler;
    /**
     * object responsible for animations
     */
    protected ChartAnimator mAnimator;
    /**
     * paint object used for drawing the bitmap
     */
    protected Paint mDrawPaint;
    /**
     * array of Highlight objects that reference the highlighted slices in the
     * chart
     */
    protected Highlight[] mIndicesToHighlight;

    /**
     * tasks to be done after the view is setup
     */
    protected ArrayList<Runnable> mJobs = new ArrayList<Runnable>();
    /**
     * If set to true, chart continues to scroll after touch up
     */
    private boolean mDragDecelerationEnabled = true;
    /**
     * Deceleration friction coefficient in [0 ; 1] interval, higher values
     * indicate that speed will decrease slowly, for example if it set to 0, it
     * will stop immediately. 1 is an invalid value, and will be converted to
     * 0.999f automatically.
     */
    private float mDragDecelerationFrictionCoef = 0.9f;
    /**
     * text that is displayed when the chart is empty
     */
    private String mNoDataText = "No chart data available.";
    /**
     * Gesture listener for custom callbacks when making gestures on the chart.
     */
    private OnChartGestureListener mGestureListener;
    /**
     * text that is displayed when the chart is empty that describes why the
     * chart is empty
     */
    private String mNoDataTextDescription;
    /**
     * Extra offsets to be appended to the viewport
     */
    private float mExtraTopOffset = 0.f,
            mExtraRightOffset = 0.f,
            mExtraBottomOffset = 0.f,
            mExtraLeftOffset = 0.f;
    /**
     * flag that indicates if offsets calculation has already been done or not
     */
    private boolean mOffsetsCalculated = false;
    /**
     * the custom position of the description text
     */
    private PointF mDescriptionPosition;

    /**
     * default constructor for initialization in code
     */
    public Chart(Context context) {
        super(context);
        init();
    }

    /**
     * constructor for initialization in xml
     */
    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * even more awesome constructor
     */
    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * initialize all paints and stuff
     */
    protected void init() {

        setWillNotDraw(false);

        if (android.os.Build.VERSION.SDK_INT < 11)
            mAnimator = new ChartAnimator();
        else
            mAnimator = new ChartAnimator(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    postInvalidate();
                }
            });

        // initialize the utils
        Utils.init(getContext());

        mDefaultFormatter = new DefaultValueFormatter(1);
        mViewPortHandler = new ViewPortHandler();

        mLegend = new Legend();

        mLegendRenderer = new LegendRenderer(mViewPortHandler, mLegend);

        mDescPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDescPaint.setColor(Color.BLACK);
        mDescPaint.setTextAlign(Align.RIGHT);
        mDescPaint.setTextSize(Utils.convertDpToPixel(9f));

        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(Color.rgb(247, 189, 51)); // orange
        mInfoPaint.setTextAlign(Align.CENTER);
        mInfoPaint.setTextSize(Utils.convertDpToPixel(12f));

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        if (mLogEnabled)
            Log.i("", "Chart.init()");
    }

    /**
     * Clears the chart from all data (sets it to null) and refreshes it (by calling invalidate()).
     */
    public void clear() {
        mData = null;
        mDataNotSet = true;
        mIndicesToHighlight = null;
        invalidate();
    }

    /**
     * Lets the chart know its underlying data has changed and performs all
     * necessary recalculations. It is crucial that this method is called
     * everytime data is changed dynamically. Not calling this method can lead
     * to crashes or unexpected behaviour.
     */
    public abstract void notifyDataSetChanged();

    /**
     * calculates the offsets of the chart to the border depending on the
     * position of an eventual legend or depending on the length of the y-axis
     * and x-axis labels and their position
     */
    protected abstract void calculateOffsets();

    /**
     * calcualtes the y-min and y-max value and the y-delta and x-delta value
     */
    protected abstract void calcMinMax();

    /**
     * calculates the required number of digits for the values that might be
     * drawn in the chart (if enabled), and creates the default-value-formatter
     */
    protected void calculateFormatter(float min, float max) {

        float reference = 0f;

        if (mData == null || mData.getXValCount() < 2) {

            reference = Math.max(Math.abs(min), Math.abs(max));
        } else {
            reference = Math.abs(max - min);
        }

        int digits = Utils.getDecimals(reference);
        mDefaultFormatter = new DefaultValueFormatter(digits);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataNotSet || mData == null || mData.getYValCount() <= 0) {
            // if no data, inform the user
            canvas.drawText(mNoDataText, getWidth() / 2, getHeight() / 2, mInfoPaint);

            if (!TextUtils.isEmpty(mNoDataTextDescription)) {
                float textOffset = -mInfoPaint.ascent() + mInfoPaint.descent();
                canvas.drawText(mNoDataTextDescription, getWidth() / 2, (getHeight() / 2) + textOffset, mInfoPaint);
            }
            return;
        }

        if (!mOffsetsCalculated) {

            calculateOffsets();
            mOffsetsCalculated = true;
        }
    }

    /**
     * draws the description text in the bottom right corner of the chart
     */
    protected void drawDescription(Canvas c) {

        if (!mDescription.equals("")) {

            if (mDescriptionPosition == null) {

                c.drawText(mDescription, getWidth() - mViewPortHandler.offsetRight() - 10,
                        getHeight() - mViewPortHandler.offsetBottom()
                                - 10, mDescPaint);
            } else {
                c.drawText(mDescription, mDescriptionPosition.x, mDescriptionPosition.y, mDescPaint);
            }
        }
    }

    /**
     * Returns the array of currently highlighted values. This might a null or
     * empty array if nothing is highlighted.
     *
     * @return
     */
    public Highlight[] getHighlighted() {
        return mIndicesToHighlight;
    }

    /**
     * Returns true if there are values to highlight, false if there are no
     * values to highlight. Checks if the highlight array is null, has a length
     * of zero or if the first object is null.
     *
     * @return
     */
    public boolean valuesToHighlight() {
        return mIndicesToHighlight == null || mIndicesToHighlight.length <= 0
                || mIndicesToHighlight[0] == null ? false
                : true;
    }

    /**
     * Highlights the values at the given indices in the given DataSets. Provide
     * null or an empty array to undo all highlighting. This should be used to
     * programmatically highlight values. This DOES NOT generate a callback to
     * the OnChartValueSelectedListener.
     *
     * @param highs
     */
    public void highlightValues(Highlight[] highs) {

        // set the indices to highlight
        mIndicesToHighlight = highs;

        if (highs == null || highs.length == 0)
            mChartTouchListener.setLastHighlighted(null);

        // redraw the chart
        invalidate();
    }


    /**
     * ################ ################ ################ ################
     */
    /** BELOW THIS ONLY GETTERS AND SETTERS */

    /**
     * Highlights the value selected by touch gesture. Unlike highlightValues(...), this generates a callback to the OnChartValueSelectedListener.
     *
     * @param high
     */
    public void highlightTouch(Highlight high) {

        Entry e = null;

        if (high == null)
            mIndicesToHighlight = null;
        else {

            if (mLogEnabled)
                Log.i(LOG_TAG, "Highlighted: " + high.toString());

            e = mData.getEntryForHighlight(high);
            if (e == null || e.getXIndex() != high.getXIndex()) {
                mIndicesToHighlight = null;
                high = null;
            } else {
                // set the indices to highlight
                mIndicesToHighlight = new Highlight[]{
                        high
                };
            }
        }

        if (mSelectionListener != null) {

            if (!valuesToHighlight())
                mSelectionListener.onNothingSelected();
            else {
                // notify the listener
                mSelectionListener.onValueSelected(e, high.getDataSetIndex(), high);
            }
        }
        // redraw the chart
        invalidate();
    }

    /**
     * Returns the animator responsible for animating chart values.
     *
     * @return
     */
    public ChartAnimator getAnimator() {
        return mAnimator;
    }

    /**
     * If set to true, chart continues to scroll after touch up default: true
     */
    public boolean isDragDecelerationEnabled() {
        return mDragDecelerationEnabled;
    }

    /**
     * Returns drag deceleration friction coefficient
     *
     * @return
     */
    public float getDragDecelerationFrictionCoef() {
        return mDragDecelerationFrictionCoef;
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {
        mAnimator.animateX(durationMillis);
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified
     * animation time. If animate(...) is called, no further calling of
     * invalidate() is necessary to refresh the chart. ANIMATIONS ONLY WORK FOR
     * API LEVEL 11 (Android 3.0.x) AND HIGHER.
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {
        mAnimator.animateY(durationMillis);
    }

    /**
     * Returns the default ValueFormatter that has been determined by the chart
     * considering the provided minimum and maximum values.
     *
     * @return
     */
    public ValueFormatter getDefaultValueFormatter() {
        return mDefaultFormatter;
    }

    /**
     * set a selection listener for the chart
     *
     * @param l
     */
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }

    /**
     * Returns the custom gesture listener.
     *
     * @return
     */
    public OnChartGestureListener getOnChartGestureListener() {
        return mGestureListener;
    }

    @Override
    public float getXChartMax() {
        return mXChartMax;
    }

    @Override
    public float getXChartMin() {
        return mXChartMin;
    }

    @Override
    public int getXValCount() {
        return mData.getXValCount();
    }

    /**
     * Returns the center point of the chart (the whole View) in pixels.
     *
     * @return
     */
    public PointF getCenter() {
        return new PointF(getWidth() / 2f, getHeight() / 2f);
    }

    /**
     * Returns the center of the chart taking offsets under consideration.
     * (returns the center of the content rectangle)
     *
     * @return
     */
    @Override
    public PointF getCenterOffsets() {
        return mViewPortHandler.getContentCenter();
    }

    /**
     * set a description text that appears in the bottom right corner of the
     * chart, size = Y-legend text size
     *
     * @param desc
     */
    public void setDescription(String desc) {
        if (desc == null)
            desc = "";
        this.mDescription = desc;
    }

    /**
     * @return the extra offset to be appended to the viewport's top
     */
    public float getExtraTopOffset() {
        return mExtraTopOffset;
    }

    /**
     * @return the extra offset to be appended to the viewport's right
     */
    public float getExtraRightOffset() {
        return mExtraRightOffset;
    }

    /**
     * @return the extra offset to be appended to the viewport's bottom
     */
    public float getExtraBottomOffset() {
        return mExtraBottomOffset;
    }

    /**
     * @return the extra offset to be appended to the viewport's left
     */
    public float getExtraLeftOffset() {
        return mExtraLeftOffset;
    }

    /**
     * Returns true if log-output is enabled for the chart, fals if not.
     *
     * @return
     */
    public boolean isLogEnabled() {
        return mLogEnabled;
    }

    /**
     * Returns the Legend object of the chart. This method can be used to get an
     * instance of the legend in order to customize the automatically generated
     * Legend.
     *
     * @return
     */
    public Legend getLegend() {
        return mLegend;
    }

    /**
     * enables intercept touchevents
     */
    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(false);
    }

    /**
     * set a new paint object for the specified parameter in the chart e.g.
     * Chart.PAINT_VALUES
     *
     * @param p     the new paint object
     * @param which Chart.PAINT_VALUES, Chart.PAINT_GRID, Chart.PAINT_VALUES,
     *              ...
     */
    public void setPaint(Paint p, int which) {

        switch (which) {
            case PAINT_INFO:
                mInfoPaint = p;
                break;
            case PAINT_DESCRIPTION:
                mDescPaint = p;
                break;
        }
    }

    /**
     * Returns the paint object associated with the provided constant.
     *
     * @param which e.g. Chart.PAINT_LEGEND_LABEL
     * @return
     */
    public Paint getPaint(int which) {
        switch (which) {
            case PAINT_INFO:
                return mInfoPaint;
            case PAINT_DESCRIPTION:
                return mDescPaint;
        }

        return null;
    }


    /**
     * returns the x-value at the given index
     *
     * @param index
     * @return
     */
    public String getXValue(int index) {
        if (mData == null || mData.getXValCount() <= index)
            return null;
        else
            return mData.getXVals().get(index);
    }


    /**
     * Returns the ChartData object that has been set for the chart.
     *
     * @return
     */
    public T getData() {
        return mData;
    }

    /**
     * Sets a new data object for the chart. The data object contains all values and information needed for displaying.
     *
     * @param data
     */
    public void setData(T data) {

        if (data == null) {
            Log.e(LOG_TAG, "Cannot set data for chart. Provided data object is null.");
            return;
        }

        // LET THE CHART KNOW THERE IS DATA
        mDataNotSet = false;
        mOffsetsCalculated = false;
        mData = data;

        // calculate how many digits are needed
        calculateFormatter(data.getYMin(), data.getYMax());

        for (DataSet<?> set : mData.getDataSets()) {
            if (set.needsDefaultFormatter())
                set.setValueFormatter(mDefaultFormatter);
        }

        // let the chart know there is new data
        notifyDataSetChanged();

        if (mLogEnabled)
            Log.i(LOG_TAG, "Data is set.");
    }

    /**
     * Returns the ViewPortHandler of the chart that is responsible for the
     * content area of the chart and its offsets and dimensions.
     *
     * @return
     */
    public ViewPortHandler getViewPortHandler() {
        return mViewPortHandler;
    }

    /**
     * Returns the Renderer object the chart uses for drawing data.
     *
     * @return
     */
    public DataRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * Sets a new DataRenderer object for the chart.
     *
     * @param renderer
     */
    public void setRenderer(DataRenderer renderer) {

        if (renderer != null)
            mRenderer = renderer;
    }

//    @Override
//    public PointF getCenterOfView() {
//        return getCenter();
//    }

    /**
     * Returns the bitmap that represents the chart.
     *
     * @return
     */
    public Bitmap getChartBitmap() {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    /**
     * Saves the current chart state with the given name to the given path on
     * the sdcard leaving the path empty "" will put the saved file directly on
     * the SD card chart is saved as a PNG image, example:
     * saveToPath("myfilename", "foldername1/foldername2");
     *
     * @param title
     * @param pathOnSD e.g. "folder1/folder2/folder3"
     * @return returns true on success, false on error
     */
    public boolean saveToPath(String title, String pathOnSD) {

        Bitmap b = getChartBitmap();

        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()
                    + pathOnSD + "/" + title
                    + ".png");

            /*
             * Write bitmap to file using JPEG or PNG and 40% quality hint for
             * JPEG.
             */
            b.compress(CompressFormat.PNG, 40, stream);

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Saves the current state of the chart to the gallery as a JPEG image. The
     * filename and compression can be set. 0 == maximum compression, 100 = low
     * compression (high quality). NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName e.g. "my_image"
     * @param quality  e.g. 50, min = 0, max = 100
     * @return returns true if saving was successfull, false if not
     */
    public boolean saveToGallery(String fileName, int quality) {

        // restrain quality
        if (quality < 0 || quality > 100)
            quality = 50;

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            Bitmap b = getChartBitmap();

            b.compress(Bitmap.CompressFormat.JPEG, quality, out); // control
            // the jpeg
            // quality

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(8);

        // store the details
        values.put(Images.Media.TITLE, fileName);
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.DATE_ADDED, currentTime);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.DESCRIPTION, "MPAndroidChart-Library Save");
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values) == null
                ? false : true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) Utils.convertDpToPixel(50f);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(size,
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(size,
                                heightMeasureSpec)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mLogEnabled)
            Log.i(LOG_TAG, "OnSizeChanged()");

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {

            mViewPortHandler.setChartDimens(w, h);

            if (mLogEnabled)
                Log.i(LOG_TAG, "Setting chart dimens, width: " + w + ", height: " + h);

            for (Runnable r : mJobs) {
                post(r);
            }

            mJobs.clear();
        }

        notifyDataSetChanged();

        super.onSizeChanged(w, h, oldw, oldh);
    }
}
