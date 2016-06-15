package com.shanbay.shanbaytest.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public final class DimensionUtils {
    private DimensionUtils(){}

    public static int dp2px(int dp,Context context){
        DisplayMetrics metrics =  context.getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }
    public static DisplayMetrics getDisplay(Context context){
        return context.getResources().getDisplayMetrics();
    }

}
