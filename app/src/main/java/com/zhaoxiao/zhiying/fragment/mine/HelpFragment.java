package com.zhaoxiao.zhiying.fragment.mine;

import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import butterknife.OnClick;

public class HelpFragment extends BaseFragment {

    public HelpFragment() {
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_help;
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.card_document)
    public void onClick() {
        XToastUtils.toast("帮助文档");
    }
}