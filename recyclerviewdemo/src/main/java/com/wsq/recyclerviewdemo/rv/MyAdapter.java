package com.wsq.recyclerviewdemo.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wsq.recyclerviewdemo.R;
import com.wsq.recyclerviewdemo.bean.Student;

import java.util.List;

/**
 * Date:2021/3/8
 * Time:17:45
 * author:wushengqi
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    private List<Student> mDatas;

    public MyAdapter(List<Student> datas){

        if (datas == null && datas.size() > 0){
            throw new NullPointerException("datas 不能为空");
        }

        this.mDatas = datas;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv_name.setText(this.mDatas.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
