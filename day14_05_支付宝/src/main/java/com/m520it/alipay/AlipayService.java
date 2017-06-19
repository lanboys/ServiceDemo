package com.m520it.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlipayService extends Service {
    public AlipayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AliPayAgent();
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
    private class AliPayAgent extends  IAlipayService.Stub {
        @Override
        public int callSafePay(String account, String pwd, double money, long currTimeMills) {
            return safePay( account,  pwd,  money,  currTimeMills);
        }
    }
}
