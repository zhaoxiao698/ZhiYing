package com.zhaoxiao.zhiying.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.entity.test.BankedM;
import com.zhaoxiao.zhiying.entity.test.BankedQuestion;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.CarefulQuestion;
import com.zhaoxiao.zhiying.entity.test.ClozeM;
import com.zhaoxiao.zhiying.entity.test.ClozeQuestion;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.ListeningQuestion;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.MatchQuestion;
import com.zhaoxiao.zhiying.entity.test.NewM;
import com.zhaoxiao.zhiying.entity.test.NewQuestion;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerSheetActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.btn_commit)
    RoundButton btnCommit;

    private List<QuestionM> questionList;
    private boolean complete = true;
    private Map<String,Object> map;
    private boolean select;

    @Override
    protected int initLayout() {
        return R.layout.activity_answer_sheet;
    }

    @Override
    protected void initData() {
        map = (Map<String, Object>) getIntent().getSerializableExtra("map");
        questionList = (List<QuestionM>) map.get("questionList");
        select = (boolean) map.get("select");
        if (!select){
            btnCommit.setVisibility(View.GONE);
        }
        int i = 0;
        for (QuestionM question : questionList) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_answer_sheet, null);
            TextView tvType = viewGroup.findViewById(R.id.tv_type);
            FlexboxLayout flOption = viewGroup.findViewById(R.id.fl_option);
            viewGroup.removeView(tvType);
            viewGroup.removeView(flOption);
            tvType.setText(question.getSubType());

            if (question instanceof ListeningM) {
//                System.out.println("");

                flOption.removeAllViews();
                if (((ListeningM) question).getListeningQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (ListeningQuestion listeningQuestion : ((ListeningM) question).getListeningQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(listeningQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                        if (!select&&!listeningQuestion.getAnswer().equals(listeningQuestion.getUserAnswer())){
                            child.setBackground(getResources().getDrawable(R.drawable.shape_wrong_answer));
                        }
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(listeningQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            } else if (question instanceof BankedM){
                flOption.removeAllViews();
                if (((BankedM) question).getBankedQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (BankedQuestion bankedQuestion : ((BankedM) question).getBankedQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(bankedQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(bankedQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof MatchM){
                flOption.removeAllViews();
                if (((MatchM) question).getMatchQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (MatchQuestion matchQuestion : ((MatchM) question).getMatchQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(matchQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(matchQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof CarefulM){
                flOption.removeAllViews();
                if (((CarefulM) question).getCarefulQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (CarefulQuestion carefulQuestion : ((CarefulM) question).getCarefulQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(carefulQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(carefulQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof ClozeM){
                flOption.removeAllViews();
                if (((ClozeM) question).getClozeQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (ClozeQuestion clozeQuestion : ((ClozeM) question).getClozeQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(clozeQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(clozeQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof NewM){
                flOption.removeAllViews();
                if (((NewM) question).getNewQuestionList().size() == 0) {
                    tvType.setVisibility(View.GONE);
                }
                int j = 0;
                for (NewQuestion newQuestion : ((NewM) question).getNewQuestionList()) {
                    ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                    TextView child = viewGroup1.findViewById(R.id.tv_option);
                    if (StringUtils.isEmpty(newQuestion.getUserAnswer())) {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                        child.setTextColor(getResources().getColor(R.color.gray));
                        complete = false;
                    } else {
                        child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                        child.setTextColor(getResources().getColor(R.color.white));
                    }
                    viewGroup1.removeView(child);
                    child.setText(String.valueOf(newQuestion.getOrder()));
                    int finalI = i;
                    int finalJ = j;
                    child.setOnClickListener(view -> {
                        Intent intent = new Intent();
                        intent.putExtra("i", finalI);
                        intent.putExtra("j", finalJ);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    flOption.addView(child);
                    j++;
                }

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof TranslationM){
                flOption.removeAllViews();

                ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                TextView child = viewGroup1.findViewById(R.id.tv_option);
                if (StringUtils.isEmpty(((TranslationM) question).getUserAnswer())) {
                    child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                    child.setTextColor(getResources().getColor(R.color.gray));
                    complete = false;
                } else {
                    child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                    child.setTextColor(getResources().getColor(R.color.white));
                }
                viewGroup1.removeView(child);
                child.setText(String.valueOf(question.getOrder()));
                int finalI = i;
                child.setOnClickListener(view -> {
                    Intent intent = new Intent();
                    intent.putExtra("i", finalI);
                    intent.putExtra("j", -1);
                    setResult(RESULT_OK, intent);
                    finish();
                });
                flOption.addView(child);

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }  else if (question instanceof WritingM){
                flOption.removeAllViews();

                ViewGroup viewGroup1 = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_option, null);
                TextView child = viewGroup1.findViewById(R.id.tv_option);
                if (StringUtils.isEmpty(((WritingM) question).getUserAnswer())) {
                    child.setBackground(getResources().getDrawable(R.drawable.shape_no_answer));
                    child.setTextColor(getResources().getColor(R.color.gray));
                    complete = false;
                } else {
                    child.setBackground(getResources().getDrawable(R.drawable.shape_right_answer));
                    child.setTextColor(getResources().getColor(R.color.white));
                }
                viewGroup1.removeView(child);
                child.setText(String.valueOf(question.getOrder()));
                int finalI = i;
                child.setOnClickListener(view -> {
                    Intent intent = new Intent();
                    intent.putExtra("i", finalI);
                    intent.putExtra("j", -1);
                    setResult(RESULT_OK, intent);
                    finish();
                });
                flOption.addView(child);

                llContainer.addView(tvType);
                llContainer.addView(flOption);
                i++;
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
//                XToastUtils.toast("交卷");
                if (complete){
                    new MaterialDialog.Builder(this)
                            .iconRes(R.drawable.icon_tip)
                            .title("交卷")
                            .content("您确定要交卷吗？")
                            .positiveText(R.string.lab_yes)
                            .negativeText(R.string.lab_no)
                            .positiveColor(getMyBgColor())
                            .negativeColor(getResources().getColor(R.color.gray))
                            .onPositive((dialog, which) -> navigateTo(TestCompleteActivity.class,"questionList", (Serializable) questionList))
                            .show();
                } else {
                    new MaterialDialog.Builder(this)
                            .iconRes(R.drawable.icon_tip)
                            .title("交卷")
                            .content("您还有问完成的题目，是否交卷？")
                            .positiveText(R.string.lab_yes)
                            .negativeText(R.string.lab_no)
                            .positiveColor(getMyBgColor())
                            .negativeColor(getResources().getColor(R.color.gray))
                            .onPositive((dialog, which) -> navigateTo(TestCompleteActivity.class,"questionList", (Serializable) questionList))
                            .show();
                }
                break;
        }
    }
}