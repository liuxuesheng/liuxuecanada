package com.liuxuecanada.liuxuecanada.Utils.listener;

import com.liuxuecanada.liuxuecanada.Utils.data.Entry;
import com.liuxuecanada.liuxuecanada.Utils.highlight.Highlight;

public interface OnChartValueSelectedListener {

    /**
     * Called when a value has been selected inside the chart.
     * 
     * @param e The selected Entry.
     * @param dataSetIndex The index in the datasets array of the data object
     *            the Entrys DataSet is in.
     * @param h the corresponding highlight object that contains information
     *            about the highlighted position
     */
    void onValueSelected(Entry e, int dataSetIndex, Highlight h);

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    void onNothingSelected();
}
