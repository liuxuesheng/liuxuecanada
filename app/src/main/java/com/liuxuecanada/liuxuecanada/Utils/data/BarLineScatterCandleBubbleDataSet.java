
package com.liuxuecanada.liuxuecanada.Utils.data;

import android.graphics.Color;

import java.util.List;


public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry> extends DataSet<T> {

    /** default highlight color */
    protected int mHighLightColor = Color.rgb(255, 187, 115);

    public BarLineScatterCandleBubbleDataSet(List<T> yVals, String label) {
        super(yVals, label);
    }

    /**
     * Sets the color that is used for drawing the highlight indicators. Dont
     * forget to resolve the color using getResources().getColor(...) or
     * Color.rgb(...).
     * 
     * @param color
     */
    public void setHighLightColor(int color) {
        mHighLightColor = color;
    }

    /**
     * Returns the color that is used for drawing the highlight indicators.
     * 
     * @return
     */
    public int getHighLightColor() {
        return mHighLightColor;
    }
}
