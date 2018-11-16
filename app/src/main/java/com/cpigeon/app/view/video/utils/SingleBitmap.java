package com.cpigeon.app.view.video.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.cpigeon.app.utils.BitmapUtils;

/**
 * Created by Zhu TingYu on 2018/1/27.
 */

public class SingleBitmap {
    private static SingleBitmap singleBitmap;
    private Bitmap bitmap;
    public synchronized static SingleBitmap getSingleBitmap(){
        if(singleBitmap == null){
            singleBitmap = new SingleBitmap();
        }
        return singleBitmap;
    }

    public void setBitmap(View view){
        bitmap = BitmapUtils.convertViewToBitmap(view);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
