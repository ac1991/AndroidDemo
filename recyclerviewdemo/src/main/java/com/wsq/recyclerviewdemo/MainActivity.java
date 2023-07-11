package com.wsq.recyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wsq.recyclerviewdemo.bean.Student;
import com.wsq.recyclerviewdemo.rv.MyAdapter;
import com.wsq.recyclerviewdemo.rv.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        List<Student> datas = new ArrayList<>();
        for (int i = 0; i <30; i++){
            Student student = new Student();
            student.name = "学生：" + i;
            datas.add(student);
        }

        MyAdapter myAdapter = new MyAdapter(datas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);


    }
}
