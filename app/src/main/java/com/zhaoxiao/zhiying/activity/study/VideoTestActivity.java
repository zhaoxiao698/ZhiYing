package com.zhaoxiao.zhiying.activity.study;

import android.os.Bundle;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.player.VideoView;

public class VideoTestActivity extends BaseActivity {
    @BindView(R.id.player)
    VideoView videoView;

    @Override
    protected int initLayout() {
        return R.layout.activity_video_test;
    }

    @Override
    protected void initData() {
        videoView.setUrl("https://media.w3.org/2010/05/sintel/trailer.mp4"); //设置视频地址
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent("标题", false);
        videoView.setVideoController(controller); //设置控制器
        videoView.start(); //开始播放，不调用则不自动播放
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }

    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }
}