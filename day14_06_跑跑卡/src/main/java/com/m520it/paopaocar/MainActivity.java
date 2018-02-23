package com.m520it.paopaocar;

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

import com.m520it.alipay.IAlipay;
import com.m520it.alipay.OnAlipayCallback;
import com.m520it.alipay.PayUser;

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

        //在oppo手机中，如果支付宝 app 被杀死，服务也会被杀死，导致支付失败，需要重新绑定

        //启动服务
        //startService(mIntent);
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
            public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
                //远程 拿到代理
                //应用内 拿到本身
                Log.e(TAG, "onServiceConnected:mAgent：" + iBinder);
                mAgent = IAlipay.Stub.asInterface(iBinder);
                Log.e(TAG, "onServiceConnected:mAgent：" + mAgent);

                //Binder 死亡代理
                try {
                    iBinder.linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            Log.e(TAG, "iBinder.binderDied：死亡后回调" );

                            // 死亡后回调
                            iBinder.unlinkToDeath(this, 0);

                            // 重新绑定服务
                            mConnection = null;
                            mAgent = null;
                            bindAlipayService(mIntent);
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                try {
                    // 远程注册回调  必须 继承自 Stub(因为Stub帮我们重写了onTransact 代理方法) ,不然 无法收到回调
                    OnAlipayCallback callback = new OnAlipayCallback.Stub() {

                        @Override
                        public void onAlipayInfo(boolean isPaySuccess, final PayUser payUser) throws RemoteException {
                            Log.e(TAG, "PayUser=======：" + payUser);
                        }
                    };
                    Log.e(TAG, "callback---------------------: " + callback);

                    mAgent.registerPayListener(callback);
                } catch (Exception e) {
                    Log.e(TAG, "registerPayListener: " + e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Toast.makeText(MainActivity.this, "断开支付宝服务", Toast.LENGTH_SHORT).show();
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
        //stopService(mIntent);

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
