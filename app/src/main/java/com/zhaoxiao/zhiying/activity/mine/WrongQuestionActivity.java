package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.fragment.test.QuestionListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WrongQuestionActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.fl_question)
    FrameLayout flQuestion;

    FragmentManager fragmentManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_wrong_question;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_question, QuestionListFragment.newInstance("错题")).commit();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.g_yellow),0);
    }
}