package com.wushengqi.ipccommunication.binder_and_service;

import android.os.Binder;
import android.os.Process;

import com.wushengqi.commandutils.Logger;

/**
 * Created by sqwu on 2019/3/12
 *
 * 实际业务逻辑发生在bind中
 */
public class MsgBinder extends Binder {
    private String TAG = "MsgBinder";
    private BindService bindService;

    public MsgBinder(BindService bindService){
        this.bindService = bindService;
    }


    public void setText(){
        bindService.setName();
        Logger.d(TAG, "myPid:" + Process.myPid() + "  myUid:" + Process.myUid() + "  myTid:" + Process.myTid());
    }


}
