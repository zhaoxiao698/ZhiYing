package com.zhaoxiao.zhiying.activity;

import android.widget.Button;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.PwdSwitch;
import com.zhaoxiao.zhiying.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private PwdSwitch pwd1;
    private PwdSwitch pwd2;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        pwd1 = new PwdSwitch(findViewById(R.id.et_pwd), findViewById(R.id.iv_pwd_switch1));
        pwd2 = new PwdSwitch(findViewById(R.id.et_confirm_pwd), findViewById(R.id.iv_pwd_switch2));
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    //注册
    private void register(String account, String pwd, String confirmPwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号!");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码!");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            showToast("两次输入密码不一致!");
            return;
        }

        navigateTo(LoginActivity.class);
        showToast("注册成功！");

        //网络请求

    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        String account = etAccount.getText().toString().trim();
        String pwd = pwd1.etPwd.getText().toString().trim();
        String confirmPwd = pwd2.etPwd.getText().toString().trim();
        register(account, pwd, confirmPwd);
    }
}