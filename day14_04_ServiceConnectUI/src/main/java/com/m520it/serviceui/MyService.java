package com.m520it.serviceui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final String TAG = "tag";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "MyService.onBind():16:: ");
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "MyService.onCreate():24:: ");
//        doWork();
    }

    public void showToast() {
        Toast.makeText(this, "交互成功", 0).show();
        Log.v(TAG, "MyService.showToast():22::交互成功 " );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "MyService.onStartCommand():36:: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "MyService.onDestroy():36:: ");
        super.onDestroy();
    }

    private class MyBinder extends Binder implements IMyService {
        @Override
        public void callShowServiceToast() {
            showToast();
        }
    }
}
