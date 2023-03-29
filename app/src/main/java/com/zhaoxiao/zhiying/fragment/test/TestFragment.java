package com.zhaoxiao.zhiying.fragment.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

public class TestFragment extends BaseFragment {

    public TestFragment() {
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {

    }
}