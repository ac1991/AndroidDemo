package com.wushengqi.ipccommunication.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.wushengqi.commandutils.Logger;


/**
 * Created by sqwu on 2019/3/13
 */
public class AIDLService extends Service {
    private String TAG = "AIDLService";
    private Book book;
    private int bookIndex;
    private AIDLCallback aidlCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private IAidlInterface.Stub stub = new IAidlInterface.Stub() {
        @Override
        public void addBookInOut(Book book) throws RemoteException {
            Logger.d(TAG, "myPid:" + Process.myPid() + "  myUid:" + Process.myUid() + "  myTid:" + Process.myTid());
            Logger.d(book.name);
            AIDLService.this.book = book;
            bookIndex++;
            if (aidlCallback != null){
                aidlCallback.addBookResult(bookIndex);
            }

        }

        @Override
        public void registerAIDLCallback(AIDLCallback aidlCallback) throws RemoteException {
            Logger.d(TAG, "remote add AIDLCallback:"+ aidlCallback.toString());
            if (aidlCallback != null) {
//                AIDLService.this.aidlCallback = aidlCallback;
                mCallbacks.register(aidlCallback);
            }
        }

        @Override
        public void unregisterAIDLCallback(AIDLCallback aidlCallback) throws RemoteException {
            Logger.d(TAG, "remote AIDLCallback:"+ aidlCallback.toString());
            Logger.d(TAG, "service AIDLCallback:"+ AIDLService.this.aidlCallback.toString());
//            if ( AIDLService.this.aidlCallback == aidlCallback){
                mCallbacks.unregister(aidlCallback);
//                AIDLService.this.aidlCallback = null;
//            }
        }
    };

    final RemoteCallbackList<AIDLCallback> mCallbacks = new RemoteCallbackList<>();
}
