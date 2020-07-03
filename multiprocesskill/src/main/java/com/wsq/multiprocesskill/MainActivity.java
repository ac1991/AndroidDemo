package com.wsq.multiprocesskill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "onStart");
        Intent intent = new Intent(this, ProcessService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
        Intent intent = new Intent(this, ProcessService.class);
        stopService(intent);

        Intent intent1 = new Intent(this, ProcessService.class);
        startService(intent1);
    }
}
