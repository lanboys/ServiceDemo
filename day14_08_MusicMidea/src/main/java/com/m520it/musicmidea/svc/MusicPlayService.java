package com.m520it.musicmidea.svc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.m520it.musicmidea.R;
import com.m520it.musicmidea.activity.MainActivity;
import com.m520it.musicmidea.fileUtils.SharedUtil;

import java.util.ArrayList;

public class MusicPlayService extends Service {

    private static final int NOTIFICATION_FLAG = 1;
    private MediaPlayer mMediaPlayer;
    private int mCurrentPosition;
    public static final String TAG = "musicPlay";

    public MusicPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicAgent();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "MyService.onDestroy():36:: ");
        super.onDestroy();
    }

    public void playMusic(final ArrayList<String> musicDatas, final int position) {
        mCurrentPosition = position;

        Log.v(TAG, "MusicPlayService.playMusic():20:: " + "开始播放" + musicDatas.get(mCurrentPosition));
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.v(TAG, "MusicPlayService.onCompletion():31:: " + musicDatas.get(mCurrentPosition) + "播放完成了");

                        int playMode = SharedUtil.getPlayMode(MusicPlayService.this);
                        if (playMode == SharedUtil.SINGLE_LOOP) {
                            playMusic(musicDatas, mCurrentPosition);
                        } else if (playMode == SharedUtil.ALL_LOOP) {
                            mCurrentPosition++;
                            if (mCurrentPosition > musicDatas.size() - 1) {
                                mCurrentPosition = 0;
                            }
                            playMusic(musicDatas, mCurrentPosition);
                        }
                    }
                });
            }
            mMediaPlayer.reset();//重置播放器,清空已经存在的音乐
            mMediaPlayer.setDataSource(musicDatas.get(mCurrentPosition));
            mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            mMediaPlayer.start();
            showNotification(musicDatas.get(mCurrentPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void StopMusic() {
        //清空通知
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        //清空播放器
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void showNotification(String musicName) {
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("开始播放:" + musicName)// 设置在status
                // bar上显示的提示文字
                .setContentTitle("正在播放:" + musicName)// 设置在下拉status
                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText("正在播放:" + musicName)// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFICATION_FLAG, notify2);
    }

    private class MusicAgent extends Binder implements IMusicPlayService {

        @Override
        public void callPlayMusic(ArrayList<String> musicDatas, int position) {
            playMusic(musicDatas, position);
        }

        @Override
        public void callStopMusic() {
            StopMusic();
        }
    }
}
