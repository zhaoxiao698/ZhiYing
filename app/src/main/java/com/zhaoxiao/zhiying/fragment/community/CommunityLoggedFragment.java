package com.zhaoxiao.zhiying.fragment.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

public class CommunityLoggedFragment extends BaseFragment {

    public CommunityLoggedFragment() {
    }

    public static CommunityLoggedFragment newInstance() {
        return new CommunityLoggedFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_community_logged;
    }

    @Override
    protected void initData() {

    }
}