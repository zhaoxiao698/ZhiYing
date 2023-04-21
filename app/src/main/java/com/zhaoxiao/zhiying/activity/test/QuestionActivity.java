package com.zhaoxiao.zhiying.activity.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.test.MyTestAdapter;
import com.zhaoxiao.zhiying.data.DataSource;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.fragment.test.QuestionFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;
    @BindView(R.id.btn_pre)
    RoundButton btnPre;
    @BindView(R.id.btn_next)
    RoundButton btnNext;
    @BindView(R.id.btn_sheet)
    LinearLayout btnSheet;
    @BindView(R.id.hpv)
    HorizontalProgressView hpv;
    float currentProgress;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private int position;

    private List<QuestionM> questionList;
    private Map<String, Object> map;
    private String subType;

    @Override
    protected int initLayout() {
        return R.layout.activity_question;
    }

    @Override
    protected void initData() {
        map = (Map<String, Object>) getIntent().getSerializableExtra("map");
        questionList = (List<QuestionM>) map.get("questionList");
        subType = (String) map.get("subType");
        tvTitle.setText(subType);
        for (QuestionM question : questionList /*DataSource.getQuestionList()*/) {
            mFragments.add(QuestionFragment.newInstance(question));
        }

        //进度条
//        hpv.setStartProgress(currentProgress);
//        float end = (float) (position+1)/mFragments.size()*100;
//        hpv.setEndProgress(end);
//        hpv.startProgressAnimation();
//        currentProgress = end;
        currentProgress = (float) (position+1)/mFragments.size()*100;
        hpv.setProgress(currentProgress);

        preAndNext(position);
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new MyTestAdapter(getSupportFragmentManager(), mFragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((QuestionFragment) mFragments.get(position)).setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                QuestionActivity.this.position = position;
                preAndNext(position);

                //进度条
//                hpv.setStartProgress(currentProgress);
//                float end = (float) (position+1)/mFragments.size()*100;
//                hpv.setEndProgress(end);
//                hpv.startProgressAnimation();
//                currentProgress = end;
                currentProgress = (float) (position+1)/mFragments.size()*100;
                hpv.setProgress(currentProgress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void preAndNext(int position) {
        if (position == 0) {
            btnPre.setEnabled(false);
            btnPre.setText("无");
        } else {
            btnPre.setEnabled(true);
            btnPre.setText("上一题");
        }
        if (position == mFragments.size() - 1) {
            btnNext.setText("交卷");
        } else {
            btnNext.setText("下一题");
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_pre, R.id.btn_next, R.id.btn_sheet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
//                XToastUtils.toast("更多");
                new BottomSheet.BottomListSheetBuilder(this)
                        .setTitle("更多")
                        .addItem("收藏")
                        .addItem("笔记")
                        .addItem("分享")
                        .setIsCenter(true)
                        .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                            dialog.dismiss();
//                            XToastUtils.toast("Item " + (position + 1));
                            switch (position){
                                case 0:
                                    XToastUtils.toast("1");
                                    break;
                                case 1:
                                    XToastUtils.toast("2");
                                    break;
                                case 2:
                                    XToastUtils.toast("3");
                                    break;
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.btn_pre:
                viewPager.setCurrentItem(--position);
//                if (position == 0) {
//                    btnPre.setEnabled(false);
//                    btnPre.setText("无");
//                }
//                if (position < mFragments.size() - 1) {
//                    btnNext.setText("下一题");
//                }
                break;
            case R.id.btn_next:
                if (position == mFragments.size() - 1) {
                    XToastUtils.toast("交卷");
                } else {
                    viewPager.setCurrentItem(++position);
//                    if (position > 0) {
//                        btnPre.setEnabled(true);
//                        btnPre.setText("上一题");
//                    }
//                    if (position == mFragments.size() - 1) {
//                        btnNext.setText("交卷");
//                    }
                }
                break;
            case R.id.btn_sheet:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (((QuestionFragment) mFragments.get(position)).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        hpv.stopProgressAnimation();
        super.onDestroy();
    }
}