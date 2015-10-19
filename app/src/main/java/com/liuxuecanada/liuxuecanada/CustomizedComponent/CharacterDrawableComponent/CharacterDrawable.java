package com.liuxuecanada.liuxuecanada.CustomizedComponent.CharacterDrawableComponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class CharacterDrawable extends ColorDrawable {

    private static final int STROKE_WIDTH = 10;
    private static final float SHADE_FACTOR = 0.9f;
    private final char character;
    private final Paint textPaint;
    private final Paint borderPaint;
    private Context context = null;

    public CharacterDrawable(char character, int color, Context context) {
        super(color);
        this.character = character;
        this.textPaint = new Paint();
        this.borderPaint = new Paint();
        this.context = context;

        // text paint settings
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // border paint settings
        borderPaint.setColor(getDarkerShade(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int) (SHADE_FACTOR * Color.red(color)),
                (int) (SHADE_FACTOR * Color.green(color)),
                (int) (SHADE_FACTOR * Color.blue(color)));
    }




    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        // draw border
        canvas.drawRect(new Rect(0, 0, 40, 40), borderPaint);
        Paint paint = new Paint();

        paint.setDither(true);
        paint.setColor(0xFFEEEEEE);

        canvas.drawOval(0, 0, 40, 40, paint);

        // draw text
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textPaint.setTextSize(height / 2);
        canvas.drawText(String.valueOf(character), width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}