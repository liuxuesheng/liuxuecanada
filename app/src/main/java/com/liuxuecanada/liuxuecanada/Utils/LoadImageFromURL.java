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
        // TODO Auto-generated method stub
        Bitmap bitMap = null;
        URL url = null;
        InputStream is = null;

        try {
            scaleWidth = (int) params[3];
            scaleLength = (int) params[4];
        } catch (Exception ex) {

        }
        try {
            Log.d("sdd9s9d ", "X");
            url = new URL((String) params[0]);
            localIv = (ImageView) params[1];
            scale = (Boolean) params[2];
            is = url.openConnection().getInputStream();
            bitMap = BitmapFactory.decodeStream(is);
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
        return bitMap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (scale) {
            scaleImage(result, localIv, scaleWidth, scaleLength);
        } else {
            localIv.setImageBitmap(result);
            localIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            localIv.setAdjustViewBounds(true);
        }
    }

    private void scaleImage(Bitmap bm, ImageView iv, int width, int height) {
        iv.setImageBitmap(Bitmap.createScaledBitmap(bm, width, height, false));
    }

}