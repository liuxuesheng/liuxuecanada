package com.liuxuecanada.liuxuecanada.Utils.formatter;

import com.liuxuecanada.liuxuecanada.Utils.utils.ViewPortHandler;


public class DefaultXAxisValueFormatter implements XAxisValueFormatter {

    @Override
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return original; // just return original, no adjustments
    }
}
