package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces;


import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
