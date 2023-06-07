package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.CodeResponse;
import com.zhaoxiao.zhiying.entity.mine.Login;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeLogin2Activity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.vcet)
    VerifyCodeEditText vcet;
    @BindView(R.id.btn_login)
    RoundButton btnLogin;
    @BindView(R.id.btn_resend)
    Button btnResend;
    private String phone;
    private CountDownButtonHelper mCountDownHelper;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_code_login2;
    }

    @Override
    protected void initData() {
        phone = (String) getIntent().getSerializableExtra("phone");
        userService = (UserService) getService(UserService.class);

        tvDesc.setText("短信验证码已发送至 +86 - " + StringUtils.hidePhone(phone));
        mCountDownHelper = new CountDownButtonHelper(btnResend, 60);
        mCountDownHelper.setOnCountDownListener(new CountDownButtonHelper.OnCountDownListener() {
            @Override
            public void onCountDown(int time) {
                btnResend.setText("重新发送 （ " + time + " ）");
            }

            @Override
            public void onFinished() {
                btnResend.setText("重新发送");
            }
        });
        mCountDownHelper.start();
    }

    @Override
    public void onDestroy() {
        mCountDownHelper.cancel();
        super.onDestroy();
    }

    @OnClick({R.id.btn_login, R.id.btn_resend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_resend:
//                XToastUtils.toast("重新发送");
                resendCode();
                break;
        }
    }

    private void resendCode() {
        Map<String, String> params = new HashMap<>();
        params.put(CodeLoginActivity.PARAM_API_KEY, ApiConfig.API_KEY);
        params.put(CodeLoginActivity.PARAM_MOBILE, phone);
        String code = StringUtils.randomCode();
        String content = null;
        try {
            content = URLEncoder.encode(CodeLoginActivity.CONTENT1+code+CodeLoginActivity.CONTENT2,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SpUtils.getInstance(this).setString("code",code,10*SpUtils.TIME_MINUTES);
        params.put(CodeLoginActivity.PARAM_CONTENT, content);

        Call<CodeResponse> codeCall = userService.sendCode(ApiConfig.SEND_CODE_URL, params);
        codeCall.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                System.out.println("获取验证码："+response.body().toString());
                mCountDownHelper.start();
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {

            }
        });
    }

    private void login(){
//        String edit = "";
//        edit+=vcet.getEditText();
//        edit+=vcet.getEtNumber();
//        edit+=vcet.getInputValue();
//        XToastUtils.toast(edit);

//        System.out.println("getEditText-->" + vcet.getEditText());
//        System.out.println("getEtNumber-->" + vcet.getEtNumber());
//        System.out.println("getInputValue-->" + vcet.getInputValue());

        String inputCode = vcet.getInputValue();
        String code = SpUtils.getInstance(this).getString("code","");
        System.out.println("code-->"+code);
        if(inputCode.length()<vcet.getEtNumber()){
            XToastUtils.error("请输入6位验证码");
        }else if(!inputCode.equals(code)){
            XToastUtils.error("验证码输入错误");
        }else{
            loginRequest();
        }
    }

    private void loginRequest() {
        Call<Data<Login>> loginCall = userService.loginByCode(phone);
        loginCall.enqueue(new Callback<Data<Login>>() {
            @Override
            public void onResponse(Call<Data<Login>> call, Response<Data<Login>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Login login = response.body().getData();
                    Login.LoginType loginType = login.getType();
                    if (loginType == Login.LoginType.LOGIN_BY_CODE){
                        XToastUtils.success("登陆成功");
//                        setResult(CodeLoginActivity.RESULT_CLOSE);
//                        finish();
                        SpUtils.getInstance(CodeLogin2Activity.this).setString("account",login.getAccount(),10*SpUtils.TIME_DAY);
                        navigateTo(HomeActivity.class);
                    } else if (loginType == Login.LoginType.REGISTER){
                        XToastUtils.success("注册成功");
                        SpUtils.getInstance(CodeLogin2Activity.this).setString("account",login.getAccount(),10*SpUtils.TIME_DAY);
                        new MaterialDialog.Builder(CodeLogin2Activity.this)
                                .title("注册成功")
                                .content("请尽快设置密码，设置密码后可使用密码进行登录")
                                .positiveText(R.string.lab_yes)
                                .negativeText(R.string.lab_no)
                                .positiveColor(getMyBgColor())
                                .negativeColor(getResources().getColor(R.color.gray))
                                .onPositive((dialog, which) -> navigateTo(SetPasswordActivity.class))
                                .onNegative((dialog, which) -> navigateTo(HomeActivity.class))
                                .show();
                    }
                } else {
                    XToastUtils.error("登陆失败");
                }
            }

            @Override
            public void onFailure(Call<Data<Login>> call, Throwable t) {
                XToastUtils.error("请求失败");
            }
        });
    }
}