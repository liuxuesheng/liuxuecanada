package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter;


import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.ViewPortHandler;

public class DefaultXAxisValueFormatter implements XAxisValueFormatter {

    @Override
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return original; // just return original, no adjustments
    }
}
