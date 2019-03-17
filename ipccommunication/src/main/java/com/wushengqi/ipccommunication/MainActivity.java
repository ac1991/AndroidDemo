package com.wushengqi.ipccommunication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wushengqi.ipccommunication.aidl.AIDLActivity;
import com.wushengqi.ipccommunication.aidl.AIDLService;
import com.wushengqi.ipccommunication.binder_and_service.BindServiceActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.skipBindService).setOnClickListener(this);
        findViewById(R.id.skipAIDLService).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.skipBindService:
                Intent intent = new Intent(MainActivity.this, BindServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.skipAIDLService:
                Intent intent1 = new Intent(MainActivity.this, AIDLActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
