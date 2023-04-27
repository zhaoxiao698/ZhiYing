package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.word.WordActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.Plan;
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

public class SetTimePlanActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_plan)
    EditText etPlan;
    @BindView(R.id.ll_plan)
    LinearLayout llPlan;
    @BindView(R.id.btn_save)
    RoundButton btnSave;

    private long timePlan;
    private UserService userService;
    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_time_plan;
    }

    @Override
    protected void initData() {
//        int word_plan = getStringFromSp("word_plan", false);
//        etPlan.setText(String.valueOf(word_plan));
        timePlan = (long) getIntent().getSerializableExtra("timePlan");
        etPlan.setText(String.valueOf(timePlan/(1000 * 60)));

        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(this).getString("account","");
    }

    @OnClick({R.id.iv_back, R.id.ll_plan, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_plan:
                EditTextUtil.showSoftInputFromWindow(this,etPlan);
                break;
            case R.id.btn_save:
                saveSetting();
                break;
        }
    }

    private void saveSetting() {
        String plan = etPlan.getText().toString();
        if (StringUtils.isEmpty(plan)){
//            saveStringToSp("time_plan",0);
            setPlan(0);
        }
        else {
//            saveStringToSp("time_plan",Integer.parseInt(plan));
            long planI = Integer.parseInt(plan)*(1000 * 60);
            setPlan(planI);
        }
    }

    private void setPlan(long plan){
        Call<Data<Boolean>> setPlanCall = userService.setPlan(account, plan);
        setPlanCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}