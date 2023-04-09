package com.zhaoxiao.zhiying.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;
import com.xuexiang.xui.widget.toast.XToast;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.Login;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zhaoxiao.zhiying.activity.mine.ServiceProtocolActivity.KEY_PROTOCOL_TITLE;

public class PasswordLoginActivity extends BaseActivity {
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.tv_user_protocol)
    XUIAlphaTextView tvUserProtocol;
    @BindView(R.id.tv_privacy_protocol)
    XUIAlphaTextView tvPrivacyProtocol;
    @BindView(R.id.btn_login)
    RoundButton btnLogin;
    @BindView(R.id.btn_code_login)
    RoundButton btnCodeLogin;

    ViewTooltip viewTooltip;
    UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_password_login;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);

        viewTooltip = ViewTooltip
                .on(cbProtocol)
                .color(Color.BLACK)
                .position(ViewTooltip.Position.BOTTOM)
                .text("前先勾选协议")
                .clickToHide(true)
                .autoHide(false, 0)
                .animation(new ViewTooltip.FadeTooltipAnimation(500));

//        XToast.Config.get()
//                //位置设置为居中
//                .setGravity(Gravity.CENTER);

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llAccount.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });

        cbProtocol.setOnCheckedChangeListener((compoundButton, b) -> {
//            viewTooltip.autoHide(b,0);
            viewTooltip.close();
        });
    }

    @OnClick({R.id.tv_user_protocol, R.id.tv_privacy_protocol, R.id.btn_login, R.id.btn_code_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_user_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "用户协议");
                break;
            case R.id.tv_privacy_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "隐私政策");
                break;
            case R.id.btn_login:
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                boolean accountNull=false,passwordNull=false;
                String hint = null;
                if (StringUtils.isEmpty(account)) {
                    llAccount.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
                    hint = "请输入账号/手机号";
                    accountNull = true;
                }
                if (StringUtils.isEmpty(password)){
                    llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
                    if(accountNull){
                        hint += "和密码";
                    } else {
                        hint = "请输入密码";
                    }
                    passwordNull = true;
                }
                if (!accountNull && !passwordNull) {
                    if (!cbProtocol.isChecked()) {
                        viewTooltip.show();
                    } else {
//                        XToastUtils.success("登录");
                        login(account, password);
                    }
                } else {
                    XToastUtils.error(hint);
                }
                break;
            case R.id.btn_code_login:
                finish();
                navigateTo(CodeLoginActivity.class);
                break;
        }
    }

    private void login(String account, String password) {
        Call<Data<Login>> loginCall = userService.loginByPassword(account, password);
        loginCall.enqueue(new Callback<Data<Login>>() {
            @Override
            public void onResponse(Call<Data<Login>> call, Response<Data<Login>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Login login = response.body().getData();
                    Login.LoginType loginType = login.getType();
                    if (loginType == Login.LoginType.LOGIN_BY_PASSWORD||loginType == Login.LoginType.LOGIN_BY_ACCOUNT||loginType == Login.LoginType.LOGIN_BY_PHONE){
                        XToastUtils.success("登陆成功");
                        SpUtils.getInstance(PasswordLoginActivity.this).setString("account",login.getAccount(),10*SpUtils.TIME_DAY);
                        navigateTo(HomeActivity.class);
                    }
                } else {
                    XToastUtils.error("登陆失败");
                }
            }

            @Override
            public void onFailure(Call<Data<Login>> call, Throwable t) {

            }
        });
    }
}