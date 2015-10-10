package com.liuxuecanada.liuxuecanada.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.WheelSelectorComponent.WheelSelector;

import java.util.LinkedList;

public class PaintService {

    private static Bitmap bm = null;
    private static boolean backgroundPainted = false;
    private static boolean textPainted = false;

    public static void paintBackground(Context context, View view) {
        Log.d("asdasdas1", " back");
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Log.d("asdasdas", " " + width + " " + height);

        bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        //LinearGradient linearGradient = new LinearGradient(0, 0, width, height, new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.33f, 0.66f, 1}, Shader.TileMode.REPEAT);
        RadialGradient radicalGradient = new RadialGradient(width, (int) (height * 1.3), (int) (Math.sqrt(height * height + width * width) * 1.5), new int[]{0xFFce8905, 0xFF0f6748, 0xFF01095a}, new float[]{0.3f, 0.6f, 0.9f}, Shader.TileMode.REPEAT);
        Paint paint = new Paint();
        paint.setShader(radicalGradient);
        paint.setDither(true);
        canvas.drawRect(0, 0, width, height, paint);

        view.setBackground(new BitmapDrawable(context.getResources(), bm));

    }

    public static boolean getBackgroundPainted() {
        return backgroundPainted;
    }

    public static void setBackgroundPainted(boolean painted) {
        backgroundPainted = painted;
    }

    public static void paintText(Activity activity, ViewGroup viewGroup) {
        LinkedList<TextView> tvlist = findAllTextView(viewGroup);

        Log.d("asdasdas1", " @ " + "G" + tvlist.size());

        for (TextView tv : tvlist) {
            Log.d("asdasdas1", " @ " + "U");
            int[] k = getViewCoordinates(activity, tv, viewGroup);
            Log.d("asdasdas1", " @ " + "S " + tv.getText());
            Log.d("asdasdas1", " @ " + "S " + k[0] + " " + k[1]);
            int convertedColor = convertColor(bm.getPixel(k[0], k[1]));
            ((TextView) tv).setTextColor(convertedColor);
        }

    }

    public static boolean getTextPainted() {
        return textPainted;
    }

    public static void setTextPainted(boolean painted) {
        textPainted = painted;
    }

    private static LinkedList<TextView> findAllTextView(ViewGroup viewgroup) {
        int count = viewgroup.getChildCount();
        Log.d("asdasdas1", " @ " + "K" + count);

        LinkedList<TextView> tvlist = new LinkedList<TextView>();
        for (int i = 0; i < count; i++) {
            View view = viewgroup.getChildAt(i);
            if (view instanceof WheelSelector)
                continue;
            else if (view instanceof ViewGroup)
                tvlist.addAll(findAllTextView((ViewGroup) view));
            else if (view instanceof TextView)
                tvlist.addLast((TextView) view);
        }
        return tvlist;
    }

    private static int[] getViewCoordinates(Activity activity, View view, ViewGroup viewGroup) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int topOffset = dm.heightPixels - viewGroup.getMeasuredHeight();

        // the view to locate
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        int centreX = (int) (view.getWidth() / 2);
        int centreY = (int) (view.getHeight() / 2);

        int x = loc[0] + centreX;
        int y = loc[1] - topOffset + centreY;
        Log.d("asdasdas", " loc0 " + loc[0] + " loc1 " + loc[1]);
        Log.d("asdasdas", " x " + x + " y " + y + " offset " + topOffset);
        Log.d("asdasdas", " centreX " + centreX + " centreY " + centreY + " " + view.getHeight() + " " + view.getY());
        return new int[]{x, y};

    }

    private static int convertColor(int colorCode) {
        int red = Color.red(colorCode);
        int green = Color.green(colorCode);
        int blue = Color.blue(colorCode);
        float[] hsv = new float[3];

        Color.RGBToHSV(red, green, blue, hsv);
        Log.d("asdasdas", "Before H " + hsv[0] + " S " + hsv[1] + " V " + hsv[2]);

        if (hsv[2] < 0.6f) {
            hsv[2] = 1.0f;
        } else {
            hsv[0] = (hsv[0] - 10.0f) < 0 ? hsv[0] + 350.0f : hsv[0] - 10.0f;
            hsv[1] = hsv[1] > 0.5 ? hsv[1] - 0.3f : hsv[1] + 0.3f;
            hsv[2] = 1.0f;
        }

        Log.d("asdasdas", "After H " + hsv[0] + " S " + hsv[1] + " V " + hsv[2]);

        return Color.HSVToColor(hsv);
    }
}
