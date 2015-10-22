package com.liuxuecanada.liuxuecanada.Utils.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import com.liuxuecanada.liuxuecanada.Utils.animation.ChartAnimator;
import com.liuxuecanada.liuxuecanada.Utils.data.LineScatterCandleRadarDataSet;
import com.liuxuecanada.liuxuecanada.Utils.utils.ViewPortHandler;


public abstract class LineScatterCandleRadarRenderer extends DataRenderer {

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param pts the transformed x- and y-position of the lines
     * @param set the currently drawn dataset
     */
    protected void drawHighlightLines(Canvas c, float[] pts, LineScatterCandleRadarDataSet set) {

        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(pts[0], mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(pts[0], mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {

            // create horizontal path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }
}
