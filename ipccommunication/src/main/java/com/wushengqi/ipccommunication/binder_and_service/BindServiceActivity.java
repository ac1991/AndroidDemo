package com.wushengqi.ipccommunication.binder_and_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wushengqi.ipccommunication.R;

public class BindServiceActivity extends AppCompatActivity {
    private String TAG = "BindServiceActivity";

    private MsgBinder binder;
    private Intent intent;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MsgBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);

        intent = new Intent(this, BindService.class);

        findViewById(R.id.bindServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.invokeServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setText();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(connection);
    }
}
