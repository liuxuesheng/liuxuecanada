
package com.liuxuecanada.liuxuecanada.Utils.formatter;

import com.liuxuecanada.liuxuecanada.Utils.components.YAxis;
import com.liuxuecanada.liuxuecanada.Utils.data.Entry;
import com.liuxuecanada.liuxuecanada.Utils.utils.ViewPortHandler;

import java.text.DecimalFormat;


public class PercentFormatter implements ValueFormatter, YAxisValueFormatter {

    protected DecimalFormat mFormat;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public PercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " %";
    }

    // YAxisValueFormatter
    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value) + " %";
    }
}
