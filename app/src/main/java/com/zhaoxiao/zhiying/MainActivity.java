package com.zhaoxiao.zhiying;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.LoginActivity;
import com.zhaoxiao.zhiying.activity.RegisterActivity;
import com.zhaoxiao.zhiying.activity.TestActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.setting)
    ImageView setting;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
//        StatusBarUtil.setTransparentForImageView(this,null);
//        StatusBarUtil.setTransparent(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
//                navigateTo(NormalActivity.class);
                navigateTo(LoginActivity.class);
//                navigateTo(TestActivity.class);
                break;
            case R.id.btn_register:
                navigateTo(RegisterActivity.class);
                break;
            case R.id.setting:
                navigateTo(TestActivity.class);
        }
    }
}