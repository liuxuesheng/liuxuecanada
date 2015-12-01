package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.interfaces;

import android.graphics.PointF;
import android.graphics.RectF;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.data.ChartData;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.formatter.ValueFormatter;


public interface
        ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getYChartMin();


    int getXValCount();

    int getWidth();

    int getHeight();


    PointF getCenterOffsets();


    ChartData getData();
}
