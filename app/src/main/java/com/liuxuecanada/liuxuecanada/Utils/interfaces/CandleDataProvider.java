package com.liuxuecanada.liuxuecanada.Utils.interfaces;

import com.liuxuecanada.liuxuecanada.Utils.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
