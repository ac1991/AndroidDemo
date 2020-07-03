package com.wsq.multiprocesskill;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class ProcessService extends Service {
    public ProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ProcessService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("ProcessService", "onDestroy");
        super.onDestroy();

        Process.killProcess(Process.myPid());
    }
}
