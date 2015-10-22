package com.liuxuecanada.liuxuecanada.Utils.interfaces;

import android.graphics.PointF;
import android.graphics.RectF;

import com.liuxuecanada.liuxuecanada.Utils.data.ChartData;
import com.liuxuecanada.liuxuecanada.Utils.formatter.ValueFormatter;


public interface ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getYChartMin();

    float getYChartMax();
    
    int getXValCount();

    int getWidth();

    int getHeight();

    PointF getCenterOfView();

    PointF getCenterOffsets();

    RectF getContentRect();
    
    ValueFormatter getDefaultValueFormatter();

    ChartData getData();
}
