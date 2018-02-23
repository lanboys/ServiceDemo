package com.m520it.alipay;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlipayService extends BaseService {

    private boolean flag = true;

    CopyOnWriteArrayList<OnAlipayCallback> observer = new CopyOnWriteArrayList<>();

    public AlipayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        AliPayAgent aliPayAgent = new AliPayAgent();
        Log.e(TAG, "AlipayService:aliPayAgent：" + aliPayAgent);

        return aliPayAgent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                while (flag) {
                    Log.e(TAG, Thread.currentThread().getName() + " is running");
                    for (OnAlipayCallback onAlipayCallback : observer) {
                        Log.e(TAG, "观察者：" + onAlipayCallback.toString());
                        try {
                            onAlipayCallback.onAlipayInfo(true,
                                    new PayUser(12, "bing", new Messenger(new Handler())));
                        } catch (Exception e) {
                            Log.e(TAG, "onAlipayInfo: " + e);
                        }
                    }

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        flag = false;
        super.onDestroy();
    }

    //支付成功200    余额不足404   网络异常505    密码出错600
    public int safePay(String account, String pwd, double money, long currTimeMills) {
        if (!account.equals("lisi") || !pwd.equals("123")) {
            return 600;
        }
        if (money > 1000) {
            return 404;
        }
        return 200;
    }

    //    利用   AIDL 进行进程间通讯
    private class AliPayAgent extends IAlipay.Stub {

        ArrayList<String> companyId = new ArrayList<String>();

        private AliPayAgent() {
            companyId.add("com.bing.lan.lanbing");
            companyId.add("com.m520it.paopaocar");
        }

        /**
         * 远程调用的时候  所有接口方法 都将经过此方法(类似动态代理 ) 借此特性 可以做权限验证
         * 可以根据返回值做权限验证
         */
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            // 权限验证
            //if (companyId.contains(_arg0)) {
            //    return super.onTransact(code, data, reply, flags);
            //}
            //Log.e(TAG, "请先在支付宝注册");

            //return false;

            Log.e(TAG, "Proxy正在调用onTransact");

            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public int callSafePay(String companyId, String account, String pwd,
                double money, long currTimeMills) throws RemoteException {

            Log.e(TAG, companyId + "---> 正在调起支付");

            return safePay(account, pwd, money, currTimeMills);
        }

        @Override
        public void registerPayListener(OnAlipayCallback callback) throws RemoteException {
            Log.e(TAG, "registerPayListener" + callback);

            if (callback != null) {
                observer.add(callback);
            }
        }

        @Override
        public void unRegisterPayListener(OnAlipayCallback callback) throws RemoteException {
            Log.e(TAG, "unRegisterPayListener" + callback);

            observer.remove(callback);
        }
    }
}
