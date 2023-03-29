package com.zhaoxiao.zhiying.fragment.study;

import android.widget.TextView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import butterknife.BindView;

public class ChannelDetailFragment extends BaseFragment {

    @BindView(R.id.tv_info)
    TextView tvInfo;
    private String info;

    public ChannelDetailFragment() {
    }

    public static ChannelDetailFragment newInstance(String info) {
        ChannelDetailFragment fragment = new ChannelDetailFragment();
        fragment.info = info;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_channel_detail;
    }

    @Override
    protected void initData() {
        tvInfo.setText(info);
    }
}
