package com.zhaoxiao.zhiying.activity.word;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPlanActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
//    @BindView(R.id.iv_more)
//    ImageView ivMore;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_plan)
    EditText etPlan;
    @BindView(R.id.ll_plan)
    LinearLayout llPlan;
    @BindView(R.id.lv_group)
    LabelsView lvGroup;
    @BindView(R.id.btn_save)
    RoundButton btnSave;

    private int group = 10;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_plan;
    }

    @Override
    protected void initData() {
//        etPlan.setText("0");

        List<Integer> labels = new ArrayList<>();
        labels.add(5);
        labels.add(10);
        labels.add(20);
        labels.add(30);
        lvGroup.setLabels(labels, new LabelsView.LabelTextProvider<Integer>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, Integer data) {
                return data.toString();
            }
        });
        lvGroup.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                group = (int) data;
            }
        });
        lvGroup.setSelects(1);
        int word_group = getStringFromSp("word_group", false);
        switch (word_group){
            case 5: lvGroup.setSelects(0);group=labels.get(0);break;
            case 10: lvGroup.setSelects(1);group=labels.get(1);;break;
            case 20: lvGroup.setSelects(2);group=labels.get(2);;break;
            case 30: lvGroup.setSelects(3);group=labels.get(3);;break;
        }
        int word_plan = getStringFromSp("word_plan", false);
        etPlan.setText(String.valueOf(word_plan));
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
        saveStringToSp("word_group",group);
        String plan = etPlan.getText().toString();
        if (StringUtils.isEmpty(plan)){
            saveStringToSp("word_plan",0);
        }
        else {
            saveStringToSp("word_plan",Integer.parseInt(plan));
        }
        navigateTo(WordActivity.class);
    }

//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//        StatusBarUtils.setStatusBarLightMode(this);
//    }
}