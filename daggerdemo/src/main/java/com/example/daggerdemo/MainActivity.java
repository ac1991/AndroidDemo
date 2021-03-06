package com.example.daggerdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

//https://www.jianshu.com/p/2cd491f0da01
public class MainActivity extends AppCompatActivity {

//    @Inject
//    UserBean userBean;

    @Inject
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerMainActivityComponent.create().inject(this);
        System.out.println(student.name);
    }
}
