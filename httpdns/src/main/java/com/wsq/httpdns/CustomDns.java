package com.wsq.httpdns;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Dns;

public class CustomDns implements Dns {
    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String domain) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(domain);
        List<InetAddress>  dnsList = new ArrayList<>();
        for (InetAddress address : addresses) {
            Log.d("CustomDns", address.getHostAddress());
            if (address instanceof Inet6Address){
//                Log.e("CustomDns", "IP V6: " + address.getHostAddress());
//                dnsList.add(address);
            }
        }

        //如果没有IPv6则还是用原来的dns
        if (dnsList.isEmpty()){
            dnsList.addAll(Arrays.asList(addresses));
        }
        return dnsList;
    }
}
