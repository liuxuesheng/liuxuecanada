package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces;


import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.BarData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces.BarLineScatterCandleBubbleDataProvider;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isDrawHighlightArrowEnabled();
}
