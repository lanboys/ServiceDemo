package com.m520it.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BadeService extends Service {

    /**
     * 1.一次或多次启动服务,只需要一次关闭服务就能关闭,界面1启动服务,界面2也可以关闭服务
     * <p>
     * 2.一次bindService,必须带着对应的ServiceConnection进行一次解绑,所以,最好能将conn存在集合里面,防止服务解绑不了
     * 一个mServiceConnection只能解绑一次,否则报错 java.lang.IllegalArgumentException: Service not registered
     * <p>
     * 不能在界面2解绑界面1的,虽然同一个conn,但是是不同的activity调用的解绑方法
     * java.lang.IllegalArgumentException: Service not registered: com.m520it.serviceui.MainActivity$1@fb1cf1f
     */

    public static final String TAG = "BadeService";

    public BadeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "BadeService.onBind():16:: ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "BadeService.onCreate():24:: ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "BadeService.onStartCommand():36:: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "BadeService.onDestroy():36:: ");

        super.onDestroy();
    }
}
