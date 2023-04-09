package com.zhaoxiao.zhiying.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.PwdSwitch;
import com.zhaoxiao.zhiying.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.cb_remember_pwd)
    CheckBox cbRememberPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private PwdSwitch pwd1;

    String spFileName = "sp_login";
    String accountKey = "login_account";
    String pwdKey = "login_pwd";
    String rememberPwdKey = "login_remember_pwd";

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        pwd1 = new PwdSwitch(findViewById(R.id.et_pwd), findViewById(R.id.iv_pwd_switch));
        init();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(LoginActivity.this,null);
    }

    //初始化
    private void init() {
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        String account = spFile.getString(accountKey, null);
        String pwd = spFile.getString(pwdKey, null);
        boolean rememberPwd = spFile.getBoolean(rememberPwdKey, false);

        if (!StringUtils.isEmpty(account)) {
            etAccount.setText(account);
        }
        if (!StringUtils.isEmpty(pwd)) {
            pwd1.etPwd.setText(pwd);
        }

        cbRememberPwd.setChecked(rememberPwd);
    }

    //登录
    private void login(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号!");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码!");
            return;
        }

        //记住密码复选框
        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spFile.edit();

        if (cbRememberPwd.isChecked()) {
            editor.putString(accountKey, account);
            editor.putString(pwdKey, pwd);
            editor.putBoolean(rememberPwdKey, true);
            editor.apply();
        } else {
//            editor.remove(accountKey);
            editor.putString(accountKey, account);
            editor.remove(pwdKey);
            editor.remove(rememberPwdKey);
            editor.apply();
        }

        //没接口直接登录
        saveStringToSp("account", account);
//        navigateTo(HomeActivity.class, "tab",0,Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        showToast("登录成功");

        //网络请求

    }

    @OnClick({R.id.tv_register, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                navigateTo(RegisterActivity.class);
                break;
            case R.id.btn_login:
                String account = etAccount.getText().toString().trim();
                String pwd = pwd1.etPwd.getText().toString().trim();
                login(account, pwd);
                break;
        }
    }
}