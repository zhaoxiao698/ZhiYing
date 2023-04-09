package com.zhaoxiao.zhiying.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.User;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends BaseActivity {

    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.get)
    Button get;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.url)
    Button url;

    private Retrofit retrofit;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initData() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    @OnClick({R.id.test, R.id.get, R.id.post, R.id.url})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test:
                showToast("点击");
                new MaterialDialog.Builder(this)
                        .iconRes(R.drawable.icon_tip)
                        .title(R.string.tip_infos)
                        .content(R.string.content_simple_confirm_dialog)
                        .positiveText(R.string.lab_submit)
                        .show();
                break;
            case R.id.get:
                Call<Data<List<User>>> dataCall = userService.getList();
                dataCall.enqueue(new Callback<Data<List<User>>>() {
                    @Override
                    public void onResponse(Call<Data<List<User>>> call, Response<Data<List<User>>> response) {
                        showToast("请求成功");
                        System.out.println("code:" + response.body().getCode());
                        System.out.println("message:" + response.body().getMsg());
                    }

                    @Override
                    public void onFailure(Call<Data<List<User>>> call, Throwable t) {
                        Log.e("error", "回调失败：" + t.getMessage() + "," + t.toString());
                    }
                });
                break;
            case R.id.post:
                User user = new User("3","345","嘿嘿",30,"男");
                Call<Data<Boolean>> postCall = userService.addUser(user);
                postCall.enqueue(new Callback<Data<Boolean>>() {
                    @Override
                    public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                        showToast(response.body().toString());
                        System.out.println(response);
                    }

                    @Override
                    public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                        Log.e("error", "回调失败：" + t.getMessage() + "," + t.toString());
                    }
                });
                break;
            case R.id.url:
                Call<ResponseBody> urlCall = userService.getUrl(etUrl.getText().toString());
                urlCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        showToast("请求成功");
                        try {
                            System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("error", "回调失败：" + t.getMessage() + "," + t.toString());
                    }
                });
                break;
        }
    }
}