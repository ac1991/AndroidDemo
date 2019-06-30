package com.example.hotfixandroid;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

public class BugDemo {
    public static void test(Context context){
        String xx = "";
        xx = xx.replace("","");
        Toast.makeText(context, "bug 没有修复" + xx, Toast.LENGTH_LONG).show();
    }

    public void bug(Context context){
        String xx = "";
        xx = xx.replace("","");
        Toast.makeText(context, "bug 修复了" + xx, Toast.LENGTH_LONG).show();
    }
}
