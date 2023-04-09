package com.zhaoxiao.zhiying.fragment.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.google.android.flexbox.FlexboxLayout;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.test.BankedQuestion;
import com.zhaoxiao.zhiying.entity.test.CarefulQuestion;
import com.zhaoxiao.zhiying.entity.test.ClozeQuestion;
import com.zhaoxiao.zhiying.entity.test.Label;
import com.zhaoxiao.zhiying.entity.test.ListeningQuestion;
import com.zhaoxiao.zhiying.entity.test.MatchQuestion;
import com.zhaoxiao.zhiying.entity.test.NewQuestion;
import com.zhaoxiao.zhiying.entity.test.SubQuestion;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.UnitConversion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SubQuestionFragment extends BaseFragment {

    @BindView(R.id.tv_stem)
    TextView tvStem;
    @BindView(R.id.tv_a)
    TextView tvA;
    @BindView(R.id.tv_b)
    TextView tvB;
    @BindView(R.id.tv_c)
    TextView tvC;
    @BindView(R.id.tv_d)
    TextView tvD;
    //    @BindView(R.id.fl_option)
//    FlexboxLayout flOption;
    @BindView(R.id.ll_a)
    LinearLayout llA;
    @BindView(R.id.ll_b)
    LinearLayout llB;
    @BindView(R.id.ll_c)
    LinearLayout llC;
    @BindView(R.id.ll_d)
    LinearLayout llD;
    @BindView(R.id.ll_subQuestion)
    LinearLayout llSubQuestion;
    @BindView(R.id.iv_a)
    TextView ivA;
    @BindView(R.id.iv_b)
    TextView ivB;
    @BindView(R.id.iv_c)
    TextView ivC;
    @BindView(R.id.iv_d)
    TextView ivD;
    @BindView(R.id.lv_position)
    LabelsView lvPosition;
    private SubQuestion subQuestion;
    private int optionNum;
    private int selectedPosition = -1;

    public SubQuestionFragment() {
    }

    public static SubQuestionFragment newInstance(SubQuestion subQuestion, int optionNum) {
        SubQuestionFragment fragment = new SubQuestionFragment();
        fragment.subQuestion = subQuestion;
        fragment.optionNum = optionNum;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_sub_question;
    }

    @Override
    protected void initData() {
        tvStem.setText(subQuestion.getId() + ". " + subQuestion.getStem());
        if (subQuestion instanceof ListeningQuestion || subQuestion instanceof CarefulQuestion || subQuestion instanceof ClozeQuestion) {
//            flOption.setVisibility(View.GONE);
            tvA.setText(subQuestion.getA());
            tvB.setText(subQuestion.getB());
            tvC.setText(subQuestion.getC());
            tvD.setText(subQuestion.getD());
        } else if (subQuestion instanceof BankedQuestion || subQuestion instanceof MatchQuestion || subQuestion instanceof NewQuestion) {
            llA.setVisibility(View.GONE);
            llB.setVisibility(View.GONE);
            llC.setVisibility(View.GONE);
            llD.setVisibility(View.GONE);
//            flOption.removeAllViews();
//            for (int i = 0; i < optionNum; i++) {
//                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_option, null);
//                TextView child = viewGroup.findViewById(R.id.tv_option);
//                viewGroup.removeView(child);
//                char text = (char) ('A' + i);
//                child.setText(String.valueOf(text));
//                int finalI = i;
//                child.setOnClickListener(view -> select(finalI,false));
//                flOption.addView(child);
//            }

            List<String> labels = new ArrayList<>();
            for (int i = 0; i < optionNum; i++) {
                char text = (char) ('A' + i);
                labels.add(String.valueOf(text));
            }
            lvPosition.setLabels(labels);
            lvPosition.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                @Override
                public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
//                    System.out.println(data);
//                    System.out.println(isSelect);
                    if (isSelect) {
                        subQuestion.setUserAnswer(data.toString());
                    } else {
                        subQuestion.setUserAnswer("");
                    }
                }
            });
        }
    }

    @OnClick({R.id.ll_a, R.id.ll_b, R.id.ll_c, R.id.ll_d})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_a:
                select(1);
                break;
            case R.id.ll_b:
                select(2);
                break;
            case R.id.ll_c:
                select(3);
                break;
            case R.id.ll_d:
                select(4);
                break;
        }
    }

    public float getHeight() {
        float viewHeight = llSubQuestion.getHeight();
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        System.out.println("view-->" + viewHeight);
        System.out.println("屏幕-->" + screenHeight);
        float slidingHeight = viewHeight + UnitConversion.dp2px(getContext(), 50 + 80 - 30);
        return slidingHeight / screenHeight;
    }

//    //选择效果
//    private void select(int select, boolean hasOption) {
//        if (hasOption) {
//            if (selectedPosition == -1) {
//                LinearLayout linearLayoutOld = (LinearLayout) llSubQuestion.getChildAt(selectedPosition);
//                TextView textViewOld = (TextView) linearLayoutOld.getChildAt(0);
//                textViewOld.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
//                textViewOld.setTextColor(getResources().getColor(R.color.g_yellow));
//            }
//            LinearLayout linearLayoutNew = (LinearLayout) llSubQuestion.getChildAt(selectedPosition);
//            TextView textViewNew = (TextView) linearLayoutNew.getChildAt(0);
//            textViewNew.setBackground(getResources().getDrawable(R.drawable.shape_selected));
//            textViewNew.setTextColor(getResources().getColor(R.color.white));
//
//        } else {
//            if (selectedPosition == -1) {
//                TextView textViewOld = (TextView) flOption.getChildAt(selectedPosition);
//                textViewOld.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
//                textViewOld.setTextColor(getResources().getColor(R.color.g_yellow));
//            }
//            TextView textViewNew = (TextView) flOption.getChildAt(selectedPosition);
//            textViewNew.setBackground(getResources().getDrawable(R.drawable.shape_selected));
//            textViewNew.setTextColor(getResources().getColor(R.color.white));
//
//        }
//        selectedPosition = select;
//    }

    //选择效果
    private void select(int select) {
        setInActive(selectedPosition);
        if (selectedPosition != select) {
            setActive(select);
            char userAnswer = (char) ('A'-1+select);
            subQuestion.setUserAnswer(String.valueOf(userAnswer));
        } else {
            subQuestion.setUserAnswer("");
        }
        selectedPosition = select;
    }

    private void setActive(int select) {
        switch (select) {
            case 1:
                ivA.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                ivA.setTextColor(getResources().getColor(R.color.white));
                tvA.setTextColor(getResources().getColor(R.color.g_yellow));
                break;
            case 2:
                ivB.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                ivB.setTextColor(getResources().getColor(R.color.white));
                tvB.setTextColor(getResources().getColor(R.color.g_yellow));
                break;
            case 3:
                ivC.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                ivC.setTextColor(getResources().getColor(R.color.white));
                tvC.setTextColor(getResources().getColor(R.color.g_yellow));
                break;
            case 4:
                ivD.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                ivD.setTextColor(getResources().getColor(R.color.white));
                tvD.setTextColor(getResources().getColor(R.color.g_yellow));
                break;
        }
    }

    private void setInActive(int select) {
        switch (select) {
            case 1:
                ivA.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
                ivA.setTextColor(getResources().getColor(R.color.g_yellow));
                tvA.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 2:
                ivB.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
                ivB.setTextColor(getResources().getColor(R.color.g_yellow));
                tvB.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 3:
                ivC.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
                ivC.setTextColor(getResources().getColor(R.color.g_yellow));
                tvC.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 4:
                ivD.setBackground(getResources().getDrawable(R.drawable.shape_unselected));
                ivD.setTextColor(getResources().getColor(R.color.g_yellow));
                tvD.setTextColor(getResources().getColor(R.color.gray));
                break;
        }
    }
}