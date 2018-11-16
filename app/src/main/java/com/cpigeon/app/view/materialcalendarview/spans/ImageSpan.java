package com.cpigeon.app.view.materialcalendarview.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.style.LineBackgroundSpan;

/**
 * Created by Zhu TingYu on 2017/12/25.
 */

public class ImageSpan implements LineBackgroundSpan {

    Bitmap bitmap;
    //        private static final int size = 60;
    private static final int size = 42;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ImageSpan(Context context, @DrawableRes int id) {
        bitmap = getBitmapFromDrawable(context, id);
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
//        setScale();
        p.setAntiAlias(true);
        c.drawBitmap(bitmap, right - bitmap.getWidth() - 6, -16, p);
    }

    private void setScale() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        // 计算缩放比例
        float scaleWidth = ((float) size) / width;
        float scaleHeight = ((float) size) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
//        Bitmap.createBitmap(this.getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    }


    public  Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }
}
