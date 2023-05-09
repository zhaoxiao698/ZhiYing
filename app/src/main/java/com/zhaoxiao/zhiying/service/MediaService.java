package com.zhaoxiao.zhiying.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.MainActivity;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;

import java.io.IOException;

public class MediaService extends Service {
    private static final String TAG = "MediaService";
    private MyBinder mBinder = new MyBinder();
    private String audio;

    private static final int ONGOING_NOTIFICATION_ID = 1001;
    private static final String CHANNEL_ID = "Music channel";
    NotificationManager mNotificationManager;

    //初始化MediaPlayer
    public MediaPlayer mMediaPlayer = new MediaPlayer();

    private int id;

    float mSpeed = 1.0f;

    public MediaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        audio = intent.getStringExtra("audio");
        if(id==intent.getIntExtra("id",0)){
            Intent broadcastIntent = new Intent(ListenFragment.ACTION_MUSIC_INIT);
            sendBroadcast(broadcastIntent);
            return super.onStartCommand(intent,flags,startId);
        }
        id = intent.getIntExtra("id",0);
        iniMediaPlayerFile();

        //前台服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Music Channel", NotificationManager.IMPORTANCE_LOW);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Intent notificationIntent =
                new Intent(getApplicationContext(),
                        MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0, notificationIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(),
                        CHANNEL_ID);

        Notification notification = builder
                .setContentTitle("标题")
                .setContentText("内容")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent).build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class MyBinder extends Binder {

//        /**
//         *  获取MediaService.this（方便在ServiceConnection中）
//         *
//         * *//*
//        public MediaService getInstance() {
//            return MediaService.this;
//        }*/

        /**
         * 播放音乐
         */
        public void playMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                Intent intent = new Intent(ListenFragment.ACTION_MUSIC_START);
                sendBroadcast(intent);
            }
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }

        /**
         * reset
         */
        public void resetMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                iniMediaPlayerFile();
            }
        }

        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        /**
         * 获取歌曲长度
         **/
        public int getDuration() {
            return mMediaPlayer.getDuration();
        }

        /**
         * 获取播放位置
         */
        public int getPlayPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        /**
         * 播放指定位置
         */
        public void seekToPositon(int msec) {
            mMediaPlayer.seekTo(msec);
        }

        /**
         * 判断是否正在播放
         */
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        /**
         * 设置倍速
         */
        public void changePlayerSpeed(float speed) {
            if (mMediaPlayer!=null) {
                // this checks on API 23 and up
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
                        mSpeed = speed;
                    } else {
                        mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
                        mMediaPlayer.pause();
                        mSpeed = speed;
                    }
                }
            }
        }

        /**
         * 获取倍速
         */
        public float getSpeed(){
            return mSpeed;
        }
    }

    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    private void iniMediaPlayerFile() {
//        mMediaPlayer.reset();
//        //获取文件路径
//        try {
//            //此处的两个方法需要捕获IO异常
//            //设置音频文件到MediaPlayer对象中
//            mMediaPlayer.setDataSource(audio);
//            //让MediaPlayer对象准备
//            mMediaPlayer.prepare();
//            System.out.println("准备成功");
//            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Intent intent = new Intent(ListenFragment.ACTION_MUSIC_COMPLETE);
//                    sendBroadcast(intent);
//                }
//            });
//
//            Intent intent = new Intent(ListenFragment.ACTION_MUSIC_START);
//            sendBroadcast(intent);
//        } catch (IOException e) {
//            Log.d(TAG, "设置资源，准备阶段出错");
//            e.printStackTrace();
//        }

        //网络音频-缓存
        mMediaPlayer.reset();
        if (audio != null && !audio.equals("")) {
            try {
                mMediaPlayer.setDataSource(audio);//设置播放来源

                mMediaPlayer.prepareAsync();//异步准备
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    //异步准备监听
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.d(TAG, "Voice异步文件准备完成");
                        Log.d("Voice异步文件时长", mediaPlayer.getDuration() / 1000 + "");
                        mediaPlayer.start();//播放
                        Intent intent = new Intent(ListenFragment.ACTION_MUSIC_INIT);
                        sendBroadcast(intent);
                    }
                });
                mMediaPlayer.setScreenOnWhilePlaying(true);// 设置播放的时候一直让屏幕变亮
                mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    //文件缓冲监听
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        Log.d("Voice进度", i + "%");
                        Log.d("Voice文件长度", mediaPlayer.getDuration() / 1000 + "");
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Intent intent = new Intent(ListenFragment.ACTION_MUSIC_COMPLETE);
                        sendBroadcast(intent);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();

                XToastUtils.toast("音频出错");
            }
        }
    }

    //销毁多媒体播放器
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();//停止播放音乐
        mMediaPlayer.release();//释放占用的资源
        mMediaPlayer = null;//将player置为空
    }
}