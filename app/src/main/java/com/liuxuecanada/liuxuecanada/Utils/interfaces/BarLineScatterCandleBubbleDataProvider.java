package com.liuxuecanada.liuxuecanada.Utils.interfaces;

import com.liuxuecanada.liuxuecanada.Utils.components.YAxis.AxisDependency;
import com.liuxuecanada.liuxuecanada.Utils.data.BarLineScatterCandleBubbleData;
import com.liuxuecanada.liuxuecanada.Utils.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
