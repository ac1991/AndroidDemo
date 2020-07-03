package com.wushengqi.ipccommunication.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wushengqi.commandutils.Logger;
import com.wushengqi.ipccommunication.R;

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener {

    private IAidlInterface iAidlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iAidlInterface = IAidlInterface.Stub.asInterface(service);
            Logger.d("绑定成功:" + name.getPackageName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("绑定失败:" + name.getPackageName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        findViewById(R.id.bindServiceBtn).setOnClickListener(this);
        findViewById(R.id.addBook).setOnClickListener(this);
        findViewById(R.id.addCallback).setOnClickListener(this);
        findViewById(R.id.removeCallback).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bindServiceBtn:
                Intent intent = new Intent(this, AIDLService.class);
                intent.setAction("com.android.aidl");
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.addBook:
                Logger.d("AIDLActivity", "myPid:" + Process.myPid() + "  myUid:" + Process.myUid() + "  myTid:" + Process.myTid());
                Book book = new Book();
                book.name = "小胖子植树";
                try {
                    iAidlInterface.addBookInOut(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addCallback:
                try {
                    iAidlInterface.registerAIDLCallback(callbackStub);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.removeCallback:
                try {
                    iAidlInterface.unregisterAIDLCallback(callbackStub);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private AIDLCallback.Stub callbackStub = new AIDLCallback.Stub() {
        @Override
        public void addBookResult(int bookIndex) throws RemoteException {
            Toast.makeText(AIDLActivity.this, bookIndex+ "", Toast.LENGTH_LONG).show();
        }
    };
}
