package com.example.https;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.https.utils.HttpRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setConnectType(HttpRequest.CONNECT_POST);
        httpRequest.setRequest("https://www.baidu.com/", null, new byte[]{});
        httpRequest.startRequest(new HttpRequest.HttpRequestListener() {
            @Override
            public void onResult(byte[] data) {

            }

            @Override
            public void onBufferReceive(byte[] data) {

            }

            @Override
            public void onError(Exception e, int errorCode) {

            }
        });
    }


}
