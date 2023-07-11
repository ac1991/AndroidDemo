package com.wushengqi.androiddemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hotfixandroid.BugDemo;
import com.example.hotfixandroid.FixDexUtil;
import com.wushengqi.androiddemo.hotfix.FileUtils;
import com.wushengqi.androiddemo.hotfix.TestHotfix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("子分支提交", "");
        Log.d("主分支提交", "");
        fix();
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

//                PathClassLoader pathClassLoader = (PathClassLoader) getClassLoader();
////                BugDemo.test(getApplicationContext());
////                getClassDemo();
////                BugDemo.test(getApplicationContext());
                TestHotfix testHotfix = new TestHotfix();
                testHotfix.loadFix(getApplicationContext());
                break;
            case R.id.fixBtn:
                fix();

//                FixDexUtil.loadFixedDex(getApplicationContext(), new File());
                break;
        }
    }

    public void sss(){
        BugDemo bugDemo = new BugDemo();
        bugDemo.bug(getApplicationContext());
    }

    public void fix(){
        InputStream abpath = getClass().getResourceAsStream("/assets/classes.dex");
        final String path = getCacheDir().getAbsolutePath() + "/classes.dex";

        FileUtils fileUtils = new FileUtils();
        fileUtils.setFileWriteListener(new FileUtils.FileWriteListener() {
            @Override
            public void onFinish() {
                FixDexUtil.loadFixedDex(getApplicationContext(), new File(path));
                sss();
//                        FixDexUtil.hotFix2(getApplicationContext(), new File(path));
            }
        });
        fileUtils.writeFile(abpath, path);
    }

    public void getClassDemo(){
//        getCacheDir().getAbsolutePath()
        final String path = getCacheDir().getAbsolutePath() + "/classes.dex";
        String optimizeDir = getFilesDir().getAbsolutePath() +
                File.separator + "optimize_dex";
        DexClassLoader dexClassLoader = new DexClassLoader(path,
                optimizeDir,null,getClassLoader());


        try{
            Class <?> clazz = getClassLoader().loadClass("com.example.hotfixandroid"+".BugDemo");
            Object obj = clazz.newInstance() ;
            Class [] params = new Class[2];
            params[0]= Integer.TYPE;
            params[1] = Integer.TYPE ;
            Method action = clazz.getMethod("bug", Context.class) ;
            action.invoke(obj, getApplicationContext());
//            Log.e("", "return value is :"+ ret);

        }catch ( ClassNotFoundException e){

        }catch (InstantiationException e2 ){
            e2.printStackTrace();
        }catch (IllegalAccessException e3){
            e3.printStackTrace();
        }catch (NoSuchMethodException e4){
            e4.printStackTrace();
        }catch (InvocationTargetException e5){
            e5.printStackTrace();

        }
    }
}
