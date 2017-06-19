package com.m520it.serviceui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IMyService mMyBinder ;
    private ServiceConnection mServiceConnection;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(MainActivity.this, MyService.class);
    }


    public void startService(View view) {
//        Log.v(TAG, "MainActivity.startService():28:: ");
        startService(mIntent);
    }

    public void bindService(View view) {
//        Log.v(TAG, "MainActivity.bindService():43:: ");
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mMyBinder = (IMyService) iBinder;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }
            };
            bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    public void connectService(View view) {
        if (mMyBinder != null) {
            mMyBinder.callShowServiceToast();
            return;
        }
        Toast.makeText(this,"交互失败",Toast.LENGTH_SHORT).show();
    }

    public void unbindServiceOnClick(View view) {
//        Log.v(TAG, "MainActivity.unbindService():55:: ");
        if (mServiceConnection != null) {
            //只能解绑一次,否则报错 java.lang.IllegalArgumentException: Service not registered
            unbindService(mServiceConnection);
            mServiceConnection = null;
            mMyBinder = null;
        }

    }

    public void closeService(View view) {
//        Log.v(TAG, "MainActivity.closeService():60:: ");
        stopService(mIntent);
    }
}
