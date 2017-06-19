package com.m520it.musicmidea.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m520it.musicmidea.R;
import com.m520it.musicmidea.adpter.MusicAdapter;
import com.m520it.musicmidea.fileUtils.FileUtil;
import com.m520it.musicmidea.fileUtils.SharedUtil;
import com.m520it.musicmidea.svc.IMusicPlayService;
import com.m520it.musicmidea.svc.MusicPlayService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MusicAdapter mMusicAdapter;
    private IMusicPlayService mMusicAgent;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "MainActivity.onCreate():36:: ");

        initUI();
        initDatas();

        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMusicAgent = (IMusicPlayService) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);


    }

    private void initUI() {
        ListView musicLv = (ListView) findViewById(R.id.music_lv);
        mMusicAdapter = new MusicAdapter();
        musicLv.setAdapter(mMusicAdapter);
        musicLv.setOnItemClickListener(this);
    }

    private void initDatas() {
        ArrayList<String> mDatas = FileUtil.loadMusicFilePath(this);
        mMusicAdapter.setDatas(mDatas);
        mMusicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mMusicAgent.callPlayMusic(mMusicAdapter.getmDatas(), i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //菜单按钮选择播放模式
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.single_loop:
                SharedUtil.saveData(this, SharedUtil.SINGLE_LOOP);
                Log.v(TAG, "MainActivity.onOptionsItemSelected():86:: ");
                break;
            case R.id.all_loop:
                SharedUtil.saveData(this, SharedUtil.ALL_LOOP);
                break;
            case R.id.stop_when_over:
                SharedUtil.saveData(this, SharedUtil.STOP_WHEN_OVER);
                break;
            case R.id.logout_app:
                //1.停止播放
                mMusicAgent.callStopMusic();
                //2.解绑服务
                if (mServiceConnection != null) {
                    unbindService(mServiceConnection);
                    mServiceConnection = null;
                }
                //3.停止服务
                Intent intent = new Intent(this, MusicPlayService.class);
                stopService(intent);

                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
//        //2.解绑服务
//        if (mServiceConnection != null) {
//            unbindService(mServiceConnection);
//            mServiceConnection = null;
//        }
//        //3.停止服务
//        Intent intent = new Intent(this, MusicPlayService.class);
//        stopService(intent);

        Log.v(TAG, "MainActivity.onDestroy():36:: ");
        super.onDestroy();
    }
}
