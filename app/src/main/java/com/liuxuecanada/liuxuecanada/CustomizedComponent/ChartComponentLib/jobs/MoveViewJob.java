package com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.jobs;

import android.view.View;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.Transformer;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ChartComponentLib.utils.ViewPortHandler;


public class MoveViewJob implements Runnable {

    protected ViewPortHandler mViewPortHandler;
    protected float xIndex = 0f;
    protected float yValue = 0f;
    protected Transformer mTrans;
    protected View view;

    public MoveViewJob(ViewPortHandler viewPortHandler, float xIndex, float yValue,
            Transformer trans, View v) {

        this.mViewPortHandler = viewPortHandler;
        this.xIndex = xIndex;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;
    }

    @Override
    public void run() {

        float[] pts = new float[] {
                xIndex, yValue
        };

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);
    }
}
