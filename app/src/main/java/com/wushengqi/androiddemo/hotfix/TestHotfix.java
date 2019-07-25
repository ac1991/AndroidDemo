package com.wushengqi.androiddemo.hotfix;

import android.content.Context;

import com.example.hotfixandroid.BugDemo;

//增加一个中间层，不然无法调用修复类
public class TestHotfix {
    public void loadFix(Context context){
        BugDemo bugDemo = new BugDemo();
        bugDemo.bug(context);
    }
}
