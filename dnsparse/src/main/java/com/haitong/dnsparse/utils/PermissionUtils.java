package com.haitong.dnsparse.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static boolean checkPermission(Context context, String permission){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
