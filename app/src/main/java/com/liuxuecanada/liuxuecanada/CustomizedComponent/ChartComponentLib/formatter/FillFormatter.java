package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter;


import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.LineDataSet;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces.LineDataProvider;

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
