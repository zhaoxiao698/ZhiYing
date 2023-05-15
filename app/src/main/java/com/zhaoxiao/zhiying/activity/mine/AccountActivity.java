package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends BaseActivity {
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    private UserService userService;
    private String account;
    private User user;

    @Override
    protected int initLayout() {
        return R.layout.activity_account;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(this).getString("account","");
        getUserInfo();
    }

    @OnClick({R.id.iv_back, R.id.rl_account, R.id.rl_phone, R.id.rl_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_account:
                break;
            case R.id.rl_phone:
                break;
            case R.id.rl_password:
                navigateTo(SetPasswordActivity.class);
                break;
        }
    }

    private void getUserInfo(){
        Call<Data<User>> getUserInfoCall = userService.getByAccount(account);
        getUserInfoCall.enqueue(new Callback<Data<User>>() {
            @Override
            public void onResponse(Call<Data<User>> call, Response<Data<User>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    user = response.body().getData();
                    tvAccount.setText(user.getAccount());
                    tvPhone.setText(StringUtils.hidePhone(user.getPhone()));
                }
            }

            @Override
            public void onFailure(Call<Data<User>> call, Throwable t) {

            }
        });
    }
}