package com.zhaoxiao.zhiying.activity.test;

import android.view.View;

import com.donkingliang.labels.LabelsView;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.entity.test.Label;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity {

    @BindView(R.id.lv1)
    LabelsView lv1;
    @BindView(R.id.lv2)
    LabelsView lv2;

    int questionBankId;

    @Override
    protected int initLayout() {
        return R.layout.activity_select;
    }

    @Override
    protected void initData() {
        List<Label> labels1 = new ArrayList<>();
        labels1.add(new Label(1,"四级"));
        labels1.add(new Label(2,"六级"));
        lv1.setLabels(labels1, (label, position, data) -> data.getQuestionBank());
        lv1.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if(isSelect)lv2.clearAllSelect();
        });
        List<Label> labels2 = new ArrayList<>();
        labels2.add(new Label(3,"英一"));
        labels2.add(new Label(4,"英二"));
        lv2.setLabels(labels2, (label, position, data) -> data.getQuestionBank());
        lv2.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if(isSelect)lv1.clearAllSelect();
        });

        Integer questionBankId = getStringFromSp("questionBankId",false);
        if (questionBankId!=null&&questionBankId!=-1) {
            switch (questionBankId){
                case 1:lv1.setSelects(0);break;
                case 2:lv1.setSelects(1);break;
                case 3:lv2.setSelects(0);break;
                case 4:lv2.setSelects(1);break;
                default:break;
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
//                XToastUtils.toast("保存");
//                XToastUtils.toast(lv1.getSelectLabels().toString());
//                XToastUtils.toast(lv1.getSelectLabelDatas().toString());
                List<Label> datas;
                if((datas=lv1.getSelectLabelDatas())!=null&&datas.size()!=0) {
//                    XToastUtils.toast(datas.get(0).getQuestionBank());
                    saveStringToSp("questionBankId",datas.get(0).getQuestionBankId());
                    finish();
                } else if((datas=lv2.getSelectLabelDatas())!=null&&datas.size()!=0){
//                    XToastUtils.toast(datas.get(0).getQuestionBank());
                    saveStringToSp("questionBankId",datas.get(0).getQuestionBankId());
                    finish();
                }
                else {
                    XToastUtils.toast("请选择一个题库");
                }
                break;
        }
    }
}