package com.liuxuecanada.liuxuecanada.Utils.interfaces;

import com.liuxuecanada.liuxuecanada.Utils.components.YAxis;
import com.liuxuecanada.liuxuecanada.Utils.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
