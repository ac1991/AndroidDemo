package com.xzl.websocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.xzl.client.WebSocketManager;
import com.xzl.client.XZLWebSocketListener;

import org.java_websocket.handshake.ServerHandshake;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWebSocket();
//        LayoutInflater.from(this).inflate()
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

            }
        });

    }

    private void initWebSocket(){
        final WebSocketManager webSocketManager = WebSocketManager.getInstance();
        webSocketManager.setUrl("ws://121.40.165.18:8800");
        webSocketManager.setWebSocketListener(new XZLWebSocketListener() {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                webSocketManager.sendMsg("大吉大利，晚上吃鸡！");
            }

            @Override
            public void onMessage(String message) {
                Log.d("打印输出", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        });

        webSocketManager.connect();

    }
}
