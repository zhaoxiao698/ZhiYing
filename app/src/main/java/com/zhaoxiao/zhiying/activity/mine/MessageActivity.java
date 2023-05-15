package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.rl_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_notice:
                navigateTo(NoticeActivity.class);
                break;
        }
    }
}