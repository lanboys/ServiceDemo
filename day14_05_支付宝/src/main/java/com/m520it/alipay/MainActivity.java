package com.m520it.alipay;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection mConnection;

    private static final String TAG = "AlipayService";

    private IAlipay mAgent;
    private Intent mIntent;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.editText);
        mEditText.setText("10");
    }

    @SuppressLint("NewApi")
    public void binderAlipayService(View view) {

        //http://blog.csdn.net/vrix/article/details/45289207
        mIntent = new Intent("com.m520it.alipay.action.ALIPAY");
        mIntent.setPackage("com.m520it.alipay");

        //mIntent = new Intent(this,AlipayService.class);

        //在oppo手机中，如果支付宝 app 被杀死，服务也会被杀死，导致支付失败，需要重新绑定

        //启动服务
        startService(mIntent);
        //绑定支付服务
        bindAlipayService(mIntent);
    }

    //绑定支付服务
    private void bindAlipayService(Intent intent) {
        //只需要在第一次进行绑定,否则导致服务无法关闭
        if (mConnection != null) {
            return;
        }

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //拿到代理
                mAgent = IAlipay.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Toast.makeText(MainActivity.this, "断开支付宝服务",
                        Toast.LENGTH_SHORT).show();
                
                Log.e(TAG, "断开支付宝服务");
            }
        };
        //绑定支付服务
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {

        // oppo手机 解绑不了服务 其他未知

        //销毁前进行解绑操作,并关闭服务,如果是手动解绑，还需要将mConnection,mAgent置为null
        unbindService(mConnection);
        stopService(mIntent);

        super.onDestroy();
    }

    public void buyCar(View view) {

        //从编辑框中获取价格
        final String price = mEditText.getText().toString();

        try {
            int payResult = mAgent.callSafePay("lisi", "123",
                    Integer.valueOf(price), System.currentTimeMillis());

            handlerPayResult(payResult);
        } catch (Exception e) {

        }
    }

    //  对支付返回的code 进行处理
    //支付成功200    余额不足404   网络异常505    密码出错600
    public void handlerPayResult(int payResult) {
        switch (payResult) {
            case 200:
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
                break;
            case 600:
                Toast.makeText(this, "密码出错", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
