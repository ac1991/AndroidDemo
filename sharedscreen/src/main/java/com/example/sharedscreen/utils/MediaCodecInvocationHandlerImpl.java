package com.example.sharedscreen.utils;

import android.os.Bundle;
import android.util.Log;

import com.example.sharedscreen.encoder2.MediaCodecCreater;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//动态代理类，用来打日志
public class MediaCodecInvocationHandlerImpl implements InvocationHandler {
    private Object targetObject;



    public Object newProxyInstance(Object object){
        this.targetObject = object;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object ret = null;

        try {
            ret = method.invoke(targetObject, args);
            Log.d("mediaCodecCreater", method.getName());
        }catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }
}
