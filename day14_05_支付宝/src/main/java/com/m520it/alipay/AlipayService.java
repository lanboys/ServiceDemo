package com.m520it.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlipayService extends Service {

    private boolean flag = true;
    private static final String TAG = "AlipayService";

    public AlipayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AliPayAgent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                while (flag) {
                    Log.e(TAG, Thread.currentThread().getName() + " is running");
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

    //    利用   AIDl 进行进程间通讯
    private class AliPayAgent extends IAlipayService.Stub {

        @Override
        public int callSafePay(String account, String pwd, double money, long currTimeMills) {
            return safePay(account, pwd, money, currTimeMills);
        }
    }
}
