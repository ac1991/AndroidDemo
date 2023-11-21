package com.wsq.httpdns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable(){

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.dns(new CustomDns());
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String url = request.url().toString();
                        String ip = request.url().host();
                        Log.e("CustomDns", "访问地址：" + url);
//                        Connection connection = chain.connection();
//                        Log.e("CustomDns", "1111访问IP：" + connection.socket().getInetAddress().getHostAddress());

                        return chain.proceed(request);
                    }
                });

                builder.addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Connection connection = chain.connection();
                        ;
                        Log.e("CustomDns", "访问IP：" + connection.socket().getInetAddress().getHostAddress());
                        return chain.proceed(request);
                    }
                });
                String aliUrl = "https://developer.aliyun.com/article/1179820";
                String bing = "https://cn.bing.com/";
                String xx1 = "https://www.learnfk.com/";
                Request request = new Request.Builder().url(xx1).build();
                client = builder.build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Log.e("CustomDns",  response.toString() + "  body：" + response.body().contentLength() );
                    }
                });

            }
        }).start();
    }
}