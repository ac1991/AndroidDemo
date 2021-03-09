package com.wsq.recyclerviewdemo.rv;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wsq.recyclerviewdemo.R;

/**
 * Date:2021/3/8
 * Time:17:43
 * author:wushengqi
 */
public class MyHolder extends RecyclerView.ViewHolder {
    TextView tv_name;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.name);
    }
}
