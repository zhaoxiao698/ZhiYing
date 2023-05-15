package com.zhaoxiao.zhiying.activity.community;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTopicActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.sv_content)
    ScrollView svContent;

    private CommunityService communityService;
    private String account;
    
    @Override
    protected int initLayout() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initData() {
        communityService = (CommunityService) getService(CommunityService.class);
        account = SpUtils.getInstance(this).getString("account","");

        btnCreate.setEnabled(!etInfo.getText().toString().trim().equals(""));
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnCreate.setEnabled(!editable.toString().trim().equals(""));
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_create:
                create();
                break;
        }
    }

    private void create() {
        Topic topic = new Topic();
        topic.setName(etName.getText().toString());
        topic.setInfo(etInfo.getText().toString());
        topic.setUserAccount(account);
        Call<Data<Boolean>> createTopicCall = communityService.createTopic(topic);
        createTopicCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        XToastUtils.toast("创建成功");
                        finish();
                    } else {
                        XToastUtils.toast("已存在该话题");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}