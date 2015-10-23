package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter;


import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.components.YAxis;

import java.text.DecimalFormat;


public class DefaultYAxisValueFormatter implements YAxisValueFormatter {

    /** decimalformat for formatting */
    private DecimalFormat mFormat;

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DefaultYAxisValueFormatter(int digits) {

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        // avoid memory allocations here (for performance)
        return mFormat.format(value);
    }
}
