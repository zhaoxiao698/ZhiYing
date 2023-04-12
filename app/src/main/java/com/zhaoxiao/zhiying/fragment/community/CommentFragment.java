package com.zhaoxiao.zhiying.fragment.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

public class CommentFragment extends BaseFragment {
    private int trendId;
    private int type;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(int trendId, int type) {
        CommentFragment fragment = new CommentFragment();
        fragment.trendId = trendId;
        fragment.type = type;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initData() {

    }
}