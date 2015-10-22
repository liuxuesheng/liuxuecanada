package com.liuxuecanada.liuxuecanada.Utils.formatter;

import com.liuxuecanada.liuxuecanada.Utils.components.YAxis;


public interface YAxisValueFormatter {

    /**
     * Called when a value from the YAxis is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the YAxis value to be formatted
     * @param yAxis the YAxis object the value belongs to
     * @return
     */
    String getFormattedValue(float value, YAxis yAxis);
}
