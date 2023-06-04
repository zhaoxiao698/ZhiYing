package com.zhaoxiao.zhiying.fragment.test;

import android.content.Intent;
import android.os.Build;

import com.danikula.videocache.HttpProxyCacheServer;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.cache.ProxyVideoCacheManager;
import com.zhaoxiao.zhiying.view.MyVideoView;

import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.player.BaseVideoView;

public class TestListeningFragment extends BaseFragment {

    @BindView(R.id.player)
    MyVideoView player;

    private Intent broadcastIntent;

    private float mSpeed = 1.0f;

    public static final String ACTION_TEST_INIT = "ACTION_TEST_INIT";
    public static final String ACTION_TEST_START = "ACTION_TEST_START";
    public static final String ACTION_TEST_STOP = "ACTION_TEST_STOP";
    public static final String ACTION_TEST_COMPLETE = "ACTION_TEST_COMPLETE";

    private OnSpeechFragmentCreatedListener onSpeechFragmentCreatedListener;

    public void setOnSpeechFragmentCreatedListener(OnSpeechFragmentCreatedListener onSpeechFragmentCreatedListener) {
        this.onSpeechFragmentCreatedListener = onSpeechFragmentCreatedListener;
    }


    public TestListeningFragment() {
    }

    public static TestListeningFragment newInstance() {
        return new TestListeningFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initData() {
//        player = mRootView.findViewById(R.id.player);
        if (onSpeechFragmentCreatedListener!=null) {
            onSpeechFragmentCreatedListener.onSpeechFragmentCreated();
        }
    }

    public void play(String url){
        //缓存
        HttpProxyCacheServer cacheServer = ProxyVideoCacheManager.getProxy(getContext());
        String proxyUrl = cacheServer.getProxyUrl(ApiConfig.BASE_URl+url);
        player.setUrl(proxyUrl);
        StandardVideoController controller = new StandardVideoController(getContext());
        controller.addDefaultControlComponent("缓存", false);
        player.setVideoController(controller);
        player.start();

//        player.setUrl("https://media.w3.org/2010/05/sintel/trailer.mp4"); //设置视频地址
//        StandardVideoController controller = new StandardVideoController(getContext());
//        controller.addDefaultControlComponent("标题", false);
//        player.setVideoController(controller); //设置控制器
//        player.start(); //开始播放，不调用则不自动播放

        player.setOnPreparedListener(new MyVideoView.OnPreparedListener() {
            @Override
            public void onPrepared() {
                broadcastIntent = new Intent(ACTION_TEST_INIT);
                getContext().sendBroadcast(broadcastIntent);
            }
        });
        player.addOnStateChangeListener(new BaseVideoView.OnStateChangeListener() {
            @Override
            public void onPlayerStateChanged(int playerState) {

            }

            @Override
            public void onPlayStateChanged(int playState) {
                switch (playState){
                    case MyVideoView.STATE_PLAYING:
                        broadcastIntent = new Intent(ACTION_TEST_START);
                        getContext().sendBroadcast(broadcastIntent);
                        break;
                    case MyVideoView.STATE_PAUSED:
                        broadcastIntent = new Intent(ACTION_TEST_STOP);
                        getContext().sendBroadcast(broadcastIntent);
                        break;
                    case MyVideoView.STATE_PLAYBACK_COMPLETED:
                    case MyVideoView.STATE_IDLE:
                        broadcastIntent = new Intent(ACTION_TEST_COMPLETE);
                        getContext().sendBroadcast(broadcastIntent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void replay(){
        player.replay(true);
    }

    public void pause(){
        player.pause();
    }

    public void resume(){
        player.resume();
    }

    public void release(){
        player.release();
    }

    public boolean isPlaying(){
        System.out.println(this);
        return player.isPlaying();
    }

    public long getDuration(){
        return player.getDuration();
    }

    public void seekTo(long pos){
        player.seekTo(pos);
    }

    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public int getCurrentPlayState(){
        return player.getCurrentPlayState();
    }

    public boolean onBackPressed(){
        return player.onBackPressed();
    }

    public void changePlayerSpeed(float speed) {
        if (player!=null) {
            // this checks on API 23 and up
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (player.isPlaying()) {
                    player.setSpeed(speed);
                    mSpeed = speed;
                } else {
                    player.setSpeed(speed);
                    player.pause();
                    mSpeed = speed;
                }
            }
        }
    }
    
    public float getSpeed(){
        return mSpeed;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        player.release();
        System.out.println("onDestroy ---> "+player);
    }

    @Override
    public void onDestroyView() {
        player.release();
        super.onDestroyView();
//        player.release();
        System.out.println("onDestroyView ---> "+player);
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
        System.out.println("onPause ---> "+player);
    }

    @Override
    public void onResume() {
        super.onResume();
        player.resume();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop ---> "+player);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("onDetach ---> "+player);
    }

    public interface OnSpeechFragmentCreatedListener {
        void onSpeechFragmentCreated();
    }
}