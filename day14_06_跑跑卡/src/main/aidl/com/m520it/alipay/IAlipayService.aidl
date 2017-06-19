package com.m520it.alipay;

/**
 * Created by 520 on 2016/11/28.
 */

 interface IAlipayService {

    int callSafePay(String account, String pwd, double money, long currTimeMills);
}