package com.zhaoxiao.zhiying.fragment.test;

import android.widget.TextView;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.activity.test.SelectActivity;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class UnselectedFragment extends BaseFragment {

    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.btn_select)
    RoundButton btnSelect;

    private boolean isLogin;

    public UnselectedFragment() {
    }

    public static UnselectedFragment newInstance(boolean isLogin) {
        UnselectedFragment fragment = new UnselectedFragment();
        fragment.isLogin = isLogin;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_unselected;
    }

    @Override
    protected void initData() {
        if (isLogin) {
            tvSelect.setText("请登录后查看");
            btnSelect.setText("点击登录");
        }
    }

    @OnClick(R.id.btn_select)
    public void onClick() {
        if (isLogin) {
            navigateTo(CodeLoginActivity.class);
        } else {
            navigateTo(SelectActivity.class);
//        XToastUtils.toast("选择题库");
        }
    }
}