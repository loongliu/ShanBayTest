package com.shanbay.shanbaytest.util;

import android.util.Log;

/**
 * Created by Liujilong on 2016/6/15.
 * liujilong.me@gmail.com
 */
public final class LogUtils {
    private LogUtils(){}

    public static final boolean DEBUG = true;

    public static void v(String tag, String message){
        if(DEBUG){
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message){
        if(DEBUG){
            Log.d(tag, message);
        }
    }
    public static void i(String tag, String message){
        if(DEBUG){
            Log.i(tag, message);
        }
    }
    public static void e(String tag, String message){
        if(DEBUG){
            Log.e(tag, message);
        }
    }
    public static void w(String tag, String message){
        if(DEBUG){
            Log.w(tag, message);
        }
    }
}
