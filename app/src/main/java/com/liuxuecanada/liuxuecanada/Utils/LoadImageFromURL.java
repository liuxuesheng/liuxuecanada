package com.liuxuecanada.liuxuecanada.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadImageFromURL extends AsyncTask<Object, ImageView, Bitmap> {

    ImageView localIv = null;
    Boolean scale = null;
    int scaleWidth = 0;
    int scaleLength = 0;

    @Override
    protected Bitmap doInBackground(Object... params) {
        Log.d("system time7: ", "" + System.currentTimeMillis());
        Bitmap bitMap = null;
        URL url = null;
        InputStream is = null;

        try {
            scaleWidth = (int) params[3];
            scaleLength = (int) params[4];
            Log.d("image debugger1: ", "" + scaleWidth + " " + scaleLength);
        } catch (Exception ex) {

        }
        try {
            url = new URL((String) params[0]);
            localIv = (ImageView) params[1];
            scale = (Boolean) params[2];
            Log.d("image debugger2: ", "" + url + " " + scale);

            Log.d("system timeXXX: ", "" + System.currentTimeMillis());
            is = url.openConnection().getInputStream();
            Log.d("system timeYYY: ", "" + System.currentTimeMillis());
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = true;

            final int REQUIRED_SIZE = 70;
            int width_tmp = ops.outWidth, height_tmp = ops.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            ops.inJustDecodeBounds = false;
            ops.inSampleSize = scale;
            bitMap = BitmapFactory.decodeStream(is, null, ops);
            Log.d("system timeZZZ: ", "" + System.currentTimeMillis());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Log.d("system time8: ", "" + System.currentTimeMillis());
        return bitMap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d("system time9: ", "" + System.currentTimeMillis());
        super.onPostExecute(result);
        if (scale) {
            scaleImage(result, localIv, scaleWidth, scaleLength);
        } else {
            localIv.setImageBitmap(result);
            localIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            localIv.setAdjustViewBounds(true);
        }
        Log.d("system time10: ", "" + System.currentTimeMillis());
    }

    private void scaleImage(Bitmap bm, ImageView iv, int width, int height) {
        Log.d("image debugger3: ", "" + width + " " + height);
        iv.setImageBitmap(Bitmap.createScaledBitmap(bm, width, height, false));
    }

}