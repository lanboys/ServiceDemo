package com.m520it.serviceui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    /**
     * 1.一次或多次启动服务,只需要一次关闭服务就能关闭,界面1启动服务,界面2也可以关闭服务
     * <p>
     * 2.一次bindService,必须带着对应的ServiceConnection进行一次解绑,所以,最好能将conn存在集合里面,防止服务解绑不了
     * 一个mServiceConnection只能解绑一次,否则报错 java.lang.IllegalArgumentException: Service not registered
     * <p>
     * 不能在界面2解绑界面1的,虽然同一个conn,但是是不同的activity调用的解绑方法
     * java.lang.IllegalArgumentException: Service not registered: com.m520it.serviceui.MainActivity$1@fb1cf1f
     */

    private boolean flag = true;
    private static final String TAG = "tag";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "MyService.onBind():16:: ");
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "MyService.onCreate():24:: ");
    }

    public void showToast() {
        Toast.makeText(this, "交互成功", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MyService.showToast():22::交互成功 ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                while (flag) {
                    Log.i(TAG, "run()");
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Log.e(TAG, "MyService.onStartCommand():36:: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "MyService.onDestroy():36:: ");
        flag = false;
        super.onDestroy();
    }

    public class MyBinder extends Binder {

        public void showServiceToast() {
            showToast();
        }
    }
}
