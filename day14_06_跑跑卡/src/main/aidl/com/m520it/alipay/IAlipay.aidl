package com.m520it.alipay;

/**
 * Created by 520 on 2016/11/28.
 */

// 必须显示 导入包 即使同一个包下
import com.m520it.alipay.onAlipayCallback;


// AIDL 不是必须的。。。只是为了生成代码
interface IAlipay {

    int callSafePay(String companyId,String account, String pwd, double money, long currTimeMills);

//    int callSafePay1(PayUser payUser, long currTimeMills);

    void registerPayListener(OnAlipayCallback callback);

    void unRegisterPayListener(OnAlipayCallback callback);
}

//    int callSafePay1(PayUser payUser, long currTimeMills);
