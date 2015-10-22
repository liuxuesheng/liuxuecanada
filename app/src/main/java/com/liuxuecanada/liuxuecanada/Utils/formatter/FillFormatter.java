package com.liuxuecanada.liuxuecanada.Utils.formatter;

import com.liuxuecanada.liuxuecanada.Utils.data.LineData;
import com.liuxuecanada.liuxuecanada.Utils.data.LineDataSet;
import com.liuxuecanada.liuxuecanada.Utils.interfaces.LineDataProvider;


public interface FillFormatter {

    /**
     * Returns the vertical (y-axis) position where the filled-line of the
     * LineDataSet should end.
     * 
     * @param dataSet the LineDataSet that is currently drawn
     * @param dataProvider
     * @return
     */
    float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider);
}
