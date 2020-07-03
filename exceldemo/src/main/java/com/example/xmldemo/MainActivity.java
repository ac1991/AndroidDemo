package com.example.xmldemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xmldemo.excel.ExcelManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readExcel();
    }


    public void readExcel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExcelManager excelManager = new ExcelManager();
                excelManager.readExl(getApplication());
            }
        }).start();

    }
}
