package com.haitong.dnsparse.dns;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DNSParse {
    public static List<String> dnsParse(String domain){
        List<String> ipList = new ArrayList<>();
        try {

            Log.d("DNSParse","开始dns解析");
            domain = handleDomain(domain);
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            for (InetAddress address : addresses) {
                Log.d("DNSParse","IP Address: " + address.getHostAddress());
                ipList.add(address.getHostAddress());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipList;
    }

    public  static String handleDomain(String domain){
        if (domain == null){
            return "";
        }

        /**  1.去除协议头  **/
        if (domain.startsWith("http://")){
            domain = domain.replace("http://", "");
        }

        if (domain.startsWith("https://")){
            domain = domain.replace("https://", "");
        }

        /*** 2.如果有端口号，则去除端口号及之后的数据 ***/
        String[] portDatas = domain.split(":");

        domain = portDatas[0];

        /***  3.如果没有端口号，可能有path，则去除path及之后的数据  ***/
        String[] pathDatas = domain.split("/");
        domain = pathDatas[0];

        /**  4.如果没有端口号、path 可能域名后面直接带query参数，则去除query数据 ***/
        String[] queryDatas = domain.split("\\?");
        domain = queryDatas[0];
        return domain;
    }
}
