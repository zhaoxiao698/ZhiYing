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
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.EditTextUtil;
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
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.ll_old_password)
    LinearLayout llOldPassword;
    private UserService userService;

    private boolean update = false;
    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(this).getString("account", "");
        getPassword();

        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                llOldPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
                llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
                llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
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
                llOldPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
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
                llOldPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
                llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
                llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateTo(HomeActivity.class);
    }

    @OnClick({R.id.btn_yes, R.id.btn_no, R.id.ll_password, R.id.ll_password_confirm, R.id.ll_old_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                setPassword();
                break;
            case R.id.btn_no:
                navigateTo(HomeActivity.class);
                break;
            case R.id.ll_password:
                EditTextUtil.showSoftInputFromWindow(this, etPassword);
                break;
            case R.id.ll_password_confirm:
                EditTextUtil.showSoftInputFromWindow(this, etPasswordConfirm);
                break;
            case R.id.ll_old_password:
                EditTextUtil.showSoftInputFromWindow(this, etOldPassword);
                break;
        }
    }

    private void setPassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();
        if (update){
            if (StringUtils.isEmpty(oldPassword)){
                XToastUtils.error("请输入原密码");
                llOldPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
            }
        }
        if (StringUtils.isEmpty(password)) {
            XToastUtils.error("请输入密码");
            llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
        } else if (StringUtils.isEmpty(passwordConfirm)) {
            XToastUtils.error("请再次输入密码");
            llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
        } else if (!password.equals(passwordConfirm)) {
            llPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
            llPasswordConfirm.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
            XToastUtils.error("两次输入密码不一致");
        } else {
            String account = SpUtils.getInstance(this).getString("account", "");
            setPassword(account,oldPassword, password);
//            XToastUtils.success("设置成功");
//            navigateTo(HomeActivity.class);
        }
    }

    private void setPassword(String account, String oldPassword, String newPassword) {
        Call<Data<Boolean>> setPasswordCall = userService.setPassword(account,oldPassword, newPassword);
        setPasswordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    boolean success = response.body().getData();
                    if (success) {
                        XToastUtils.success("设置成功");
                        navigateTo(HomeActivity.class);
                    } else {
                        llOldPassword.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
                        XToastUtils.error("原密码错误");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    private void getPassword() {
        Call<Data<Boolean>> getPasswordCall = userService.getPassword(account);
        getPasswordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    boolean hasPassword = response.body().getData();
                    if (hasPassword) {
                        llOldPassword.setVisibility(View.VISIBLE);
                        update = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}