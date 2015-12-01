package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data;

import android.graphics.DashPathEffect;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Utils;

import java.util.List;


public abstract class LineScatterCandleRadarDataSet<T extends Entry> extends BarLineScatterCandleBubbleDataSet<T> {

    protected boolean mDrawVerticalHighlightIndicator = true;
    protected boolean mDrawHorizontalHighlightIndicator = true;

    /**
     * the width of the highlight indicator lines
     */
    protected float mHighlightLineWidth = 0.5f;

    /**
     * the path effect for dashed highlight-lines
     */
    protected DashPathEffect mHighlightDashPathEffect = null;


    public LineScatterCandleRadarDataSet(List<T> yVals, String label) {
        super(yVals, label);
        mHighlightLineWidth = Utils.convertDpToPixel(0.5f);
    }


    public boolean isVerticalHighlightIndicatorEnabled() {
        return mDrawVerticalHighlightIndicator;
    }

    public boolean isHorizontalHighlightIndicatorEnabled() {
        return mDrawHorizontalHighlightIndicator;
    }

    /**
     * Returns the line-width in which highlight lines are to be drawn.
     *
     * @return
     */
    public float getHighlightLineWidth() {
        return mHighlightLineWidth;
    }


    public DashPathEffect getDashPathEffectHighlight() {
        return mHighlightDashPathEffect;
    }
}
