package com.zhaoxiao.zhiying.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhaoxiao.zhiying.fragment.study.ListenFragment;

import org.jetbrains.annotations.NotNull;

import xyz.doikki.videoplayer.player.VideoView;

public class MyVideoView extends VideoView {

    private OnPreparedListener onPreparedListener;

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public MyVideoView(@NonNull @NotNull Context context) {
        super(context);
    }

    public MyVideoView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        onPreparedListener.onPrepared();
    }

    public interface OnPreparedListener {
        void onPrepared();
    }
}
