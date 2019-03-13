package com.wushengqi.commandutils;

import android.util.Log;


/**
 * Created by Wusq on 2018/8/23.
 */

public class Logger {
    public static boolean debug = true;

    public static final String TAG = "saas:";

    public static void  d(String tag, String msg) {
        if (debug){
            Log.d(TAG + tag, msg);
        }
    }

    public static void  i(String tag, String msg) {
        if (debug){
            Log.i(TAG + tag, msg);
        }
    }

    public static void  e(String tag, String msg) {
        if (debug){
            Log.e(TAG + tag, msg);
        }
    }

    public static void  w(String tag, String msg) {
        if (debug){
            Log.w(TAG + tag, msg);
        }
    }

    public static void  v(String tag, String msg) {
        if (debug){
            Log.v(TAG + tag, msg);
        }
    }


    private static String getString(Object... args) {
        StringBuilder content = new StringBuilder();

        for (Object arg : args) {
            if (arg != null) {
                content.append(arg.toString());
            }
        }

        return content.toString();
    }

    /**
     * Logger error
     */
    public static void error(Object... messages) {
        Log.e(TAG, getString(messages));
    }

    /**
     * Logger error
     */
    public static void e(String message, Throwable throwable) {
        Log.e(TAG, getString(message), throwable);
    }

    /**
     * Logger info
     */
    public static void i(Object... messages) {
        Log.i(TAG, getString(messages));
    }

    /**
     * Logger debug
     */
    public static void d(Object... messages) {
        Log.d(TAG, getString(messages));
    }
}
