package com.wushengqi.ipccommunication.binder_and_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wushengqi.commandutils.Logger;

/**
 * Created by sqwu on 2019/3/12
 */
public class BindService extends Service {
    private static final String TAG = "BindService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder(this);
    }

    public void setName(){
        Logger.d(TAG, "setName");
        Logger.d(TAG, "myPid:" +Process.myPid() + "  myUid:" + Process.myUid() + "  myTid:" + Process.myTid());
    }

    public String getName(){

        return TAG;
    }
}
