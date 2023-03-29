package com.zhaoxiao.zhiying.fragment.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

public class CommunityFragment extends BaseFragment {

    public CommunityFragment() {
    }

    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initData() {

    }
}