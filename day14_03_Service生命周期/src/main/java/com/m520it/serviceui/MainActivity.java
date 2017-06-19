package com.m520it.serviceui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";

    public static ServiceConnection sTestServiceConnection;
    private MyService.MyBinder mMyBinder;
    private ServiceConnection mServiceConnection;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(this, MyService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不会内存泄露
        App.sWatcher.watch(this);
    }

    public void startService(View view) {
        Log.v(TAG, "MainActivity.startService():28:: ");
        getApplicationContext().startService(mIntent);
    }

    public void bindService(View view) {
        Log.v(TAG, "MainActivity.bindService():43:: ");
        //保证绑定服务后未解除绑定将不再进行绑定，否则容易造成服务无法解绑或者关闭
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mMyBinder = (MyService.MyBinder) iBinder;
                    Log.i(TAG, "onServiceConnected");
                }

                //在正常情况下是不被调用的，它的调用时机是当Service服务被异外销毁时，例如内存的资源不足时
                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.i(TAG, "onServiceDisconnected");
                }
            };

            bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);

            //测试界面2解绑本界面的服务
            sTestServiceConnection = mServiceConnection;
            //测试界面2解绑本界面的服务

        }
    }

    public void connectService(View view) {
        if (mMyBinder != null) {
            mMyBinder.showServiceToast();
        }
    }

    public void unbindServiceOnClick(View view) {
        Log.v(TAG, "MainActivity.unbindService():55:: ");
        if (mServiceConnection != null) {
            //一个mServiceConnection只能解绑一次,否则报错 java.lang.IllegalArgumentException: Service not registered
            unbindService(mServiceConnection);
            mServiceConnection = null;
            mMyBinder = null;
        }
    }

    public void closeService(View view) {
        Log.v(TAG, "MainActivity.closeService():60:: ");
        getApplicationContext().stopService(mIntent);
    }

    public void startNewActivity(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public void unbindActivity1ServiceOnClick(View view) {
        Toast.makeText(this, "界面2测试用的", Toast.LENGTH_SHORT).show();
    }
}
