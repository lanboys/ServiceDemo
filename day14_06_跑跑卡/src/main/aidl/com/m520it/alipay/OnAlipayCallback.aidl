package com.m520it.alipay;

import com.m520it.alipay.PayUser;

interface OnAlipayCallback {

    void onAlipayInfo(boolean isPaySuccess, in PayUser payUser);
}

//    int callSafePay1(PayUser payUser, long currTimeMills);
