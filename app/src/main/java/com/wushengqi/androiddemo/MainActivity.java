package com.wushengqi.androiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hotfixandroid.BugDemo;
import com.example.hotfixandroid.FixDexUtil;
import com.wushengqi.androiddemo.hotfix.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        findViewById(R.id.bugBtn).setOnClickListener(this);
        findViewById(R.id.fixBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bugBtn:
                BugDemo.test(getApplicationContext());
                break;
            case R.id.fixBtn:
                InputStream abpath = getClass().getResourceAsStream("/assets/classes.dex");
                final String path = getCacheDir().getPath() + "/classes.dex";

                FileUtils fileUtils = new FileUtils();
                fileUtils.setFileWriteListener(new FileUtils.FileWriteListener() {
                    @Override
                    public void onFinish() {
                        FixDexUtil.loadFixedDex(getApplicationContext(), new File(path));
                    }
                });
                fileUtils.writeFile(abpath, path);

//                FixDexUtil.loadFixedDex(getApplicationContext(), new File());
                break;
        }
    }
}
