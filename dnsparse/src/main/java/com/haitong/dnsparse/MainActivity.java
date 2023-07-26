package com.haitong.dnsparse;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.haitong.dnsparse.dns.DNSParse;
import com.haitong.dnsparse.utils.ExcelReader;
import com.haitong.dnsparse.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PermissionUtils.checkPermission(this, Manifest.permission.INTERNET)){
            new Thread(new Runnable(){

                @Override
                public void run() {
                    List<String>  domainList = ExcelReader.readerUrlFromExcel(getApplicationContext(), "app域名.xls");

                    ArrayMap<String, List<String>>  datas = new ArrayMap<>();

                    for (String domain: domainList){
                        List<String> ipList = DNSParse.dnsParse(domain);
                        datas.put(domain, ipList);
                    }

                    for (int i = 0; i < datas.size(); i++){
                        String domain = datas.keyAt(i);
                        List<String> ipList = datas.get(domain);
                        Log.e("域名解析", domain);
                        for (String ip: ipList){
                            Log.d("域名解析", "ip:" + ip);
                        }

                    }

                    ExcelReader.writeExcel(getApplicationContext(), datas);

                }
            }).start();

        }else{
            Toast.makeText(this, "没有网络权限", Toast.LENGTH_SHORT).show();
        }
    }


}