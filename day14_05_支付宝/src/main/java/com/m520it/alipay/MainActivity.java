package com.m520it.alipay;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection mConnection;

    public static final String TAG = "AlipayService";

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


                //Messenger messenger = new Messenger(iBinder);
                //messenger.send(Message.obtain());

                //MessengerImpl messenger1 = new MessengerImpl();

                //远程 拿到iBinder代理----->调用代理的同名方法------>调用onTransact()------>调用iBinder方法
                //应用 拿到iBinder本身--------------------------------------------------->直接调用iBinder方法
                Log.e(TAG, "onServiceConnected:iBinder：" + iBinder);
                mAgent = IAlipay.Stub.asInterface(iBinder);
                Log.e(TAG, "onServiceConnected:mAgent：" + mAgent);

                try {

                    // 远程注册回调，必须继承自 系统生成的 Stub(因为Stub帮我们重写了onTransact 代理方法)，
                    // 不然 无法收到回调，也可以自己写一个 继承Binder,并重写onTransact方法，
                    // 因为onTransact默认实现不帮助分发各个方法的调用

                    // 应用内 注册 不需要，因为Client端的 mAgent就是Service端的binder对象，见方法 IAlipay.Stub.asInterface
                    mAgent.registerPayListener(new OnAlipayCallback() {
                        @Override
                        public void onAlipayInfo(boolean isPaySuccess, final PayUser payUser) throws RemoteException {

                            Log.e(TAG, "PayUser：" + payUser);
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    });
                } catch (RemoteException e) {
                    Log.e(TAG, "registerPayListener: " + e);
                }
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
            int payResult = mAgent.callSafePay(getPackageName(), "lisi", "123",
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
