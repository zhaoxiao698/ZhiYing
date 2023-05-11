package com.zhaoxiao.zhiying.activity.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.word.WordActivity;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetQuestionActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.lv_num)
    LabelsView lvNum;
    @BindView(R.id.lv_source)
    LabelsView lvSource;
    @BindView(R.id.btn_save)
    RoundButton btnSave;

    private int num = 10;
    private int source = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_question;
    }

    @Override
    protected void initData() {
        List<Integer> numLabels = new ArrayList<>();
        numLabels.add(5);
        numLabels.add(10);
        numLabels.add(20);
        numLabels.add(30);
        lvNum.setLabels(numLabels, new LabelsView.LabelTextProvider<Integer>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, Integer data) {
                return data.toString();
            }
        });
        lvNum.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                num = (int) data;
            }
        });
        lvNum.setSelects(1);
        int question_num = getStringFromSp("question_num", false);
        switch (question_num){
            case 5: lvNum.setSelects(0);num=numLabels.get(0);break;
            case 10: lvNum.setSelects(1);num=numLabels.get(1);break;
            case 20: lvNum.setSelects(2);num=numLabels.get(2);break;
            case 30: lvNum.setSelects(3);num=numLabels.get(3);break;
        }

        List<String> sourceLabels = new ArrayList<>();
        sourceLabels.add("新题+错题");
        sourceLabels.add("只出新题");
        sourceLabels.add("只出错题");
        sourceLabels.add("无限制");
        lvSource.setLabels(sourceLabels);
        lvSource.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                source = position;
            }
        });
        lvSource.setSelects(1);
        int question_source = getStringFromSp("question_source", false);
        if (question_source==-1){
            question_source = 1;
        }
        lvSource.setSelects(question_source);
        source=question_source;
    }

    @OnClick({R.id.iv_back, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                saveSetting();
                break;
        }
    }

    private void saveSetting() {
        saveStringToSp("question_num",num);
        saveStringToSp("question_source",source);
        finish();
    }
}