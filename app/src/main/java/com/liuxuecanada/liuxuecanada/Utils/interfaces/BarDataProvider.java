package com.liuxuecanada.liuxuecanada.Utils.interfaces;

import com.liuxuecanada.liuxuecanada.Utils.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isDrawHighlightArrowEnabled();
}
