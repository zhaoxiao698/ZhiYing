package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.Login;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.et_password_confirm)
    EditText etPasswordConfirm;
    @BindView(R.id.ll_password_confirm)
    LinearLayout llPasswordConfirm;
    @BindView(R.id.btn_yes)
    RoundButton btnYes;
    @BindView(R.id.btn_no)
    Button btnNo;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);

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
                llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });
        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
                llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateTo(HomeActivity.class);
    }

    @OnClick({R.id.btn_yes, R.id.btn_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                setPassword();
                break;
            case R.id.btn_no:
                navigateTo(HomeActivity.class);
                break;
        }
    }

    private void setPassword() {
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();
        if (StringUtils.isEmpty(password)) {
            XToastUtils.error("请输入密码");
            llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
        } else if (StringUtils.isEmpty(passwordConfirm)) {
            XToastUtils.error("请再次输入密码");
            llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
        } else if (!password.equals(passwordConfirm)){
            llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
            llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
            XToastUtils.error("两次输入密码不一致");
        } else {
            String account = SpUtils.getInstance(this).getString("account","");
            setPassword(account,password);
//            XToastUtils.success("设置成功");
//            navigateTo(HomeActivity.class);
        }
    }

    private void setPassword(String account, String password) {
        Call<Data<Boolean>> setPasswordCall = userService.setPassword(account, password);
        setPasswordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    boolean success = response.body().getData();
                    if (success){
                        XToastUtils.success("设置成功");
                        navigateTo(HomeActivity.class);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}