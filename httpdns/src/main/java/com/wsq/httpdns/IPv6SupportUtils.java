package com.wsq.httpdns;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

public class IPv6SupportUtils {
//    public static boolean isIPv6Supported(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            Network network = connectivityManager.getActiveNetwork();
//            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
//            if (networkCapabilities != null) {
//                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_IPV6);
//            }
//        }
//        return false;
//    }
}
