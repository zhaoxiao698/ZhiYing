package com.zhaoxiao.zhiying.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.progress.CircleProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.HomeActivity;
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
import com.zhaoxiao.zhiying.entity.test.SubQuestionAnswer;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestCompleteActivity extends BaseActivity implements CircleProgressView.CircleProgressUpdateListener  {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.progressView_circle_small2)
    CircleProgressView progressViewCircleSmall2;
    @BindView(R.id.progress_text_main)
    TextView progressTextMain;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_wrong)
    TextView tvWrong;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.btn_commit)
    RoundButton btnCommit;
    private List<QuestionM> questionList;
    private int total = 0;
    private int right = 0;
    private int wrong = 0;
    private int no = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_test_complete;
    }

    @Override
    protected void initData() {
        questionList = (List<QuestionM>) getIntent().getSerializableExtra("questionList");

        progressViewCircleSmall2.setProgressViewUpdateListener(this);

        for (QuestionM question : questionList) {
            if (question instanceof ListeningM) {
                for (ListeningQuestion listeningQuestion : ((ListeningM) question).getListeningQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(listeningQuestion.getUserAnswer())){
                        no++;
                    } else if (listeningQuestion.getAnswer().equals(listeningQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof BankedM){
                for (BankedQuestion bankedQuestion : ((BankedM) question).getBankedQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(bankedQuestion.getUserAnswer())){
                        no++;
                    } else if (bankedQuestion.getAnswer().equals(bankedQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof MatchM){
                for (MatchQuestion matchQuestion : ((MatchM) question).getMatchQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(matchQuestion.getUserAnswer())){
                        no++;
                    } else if (matchQuestion.getAnswer().equals(matchQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof CarefulM){
                for (CarefulQuestion carefulQuestion : ((CarefulM) question).getCarefulQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(carefulQuestion.getUserAnswer())){
                        no++;
                    } else if (carefulQuestion.getAnswer().equals(carefulQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof ClozeM){
                for (ClozeQuestion clozeQuestion : ((ClozeM) question).getClozeQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(clozeQuestion.getUserAnswer())){
                        no++;
                    } else if (clozeQuestion.getAnswer().equals(clozeQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof NewM){
                for (NewQuestion newQuestion : ((NewM) question).getNewQuestionList()) {
                    total++;
                    if (StringUtils.isEmpty(newQuestion.getUserAnswer())){
                        no++;
                    } else if (newQuestion.getAnswer().equals(newQuestion.getUserAnswer())) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else if (question instanceof TranslationM){
                total++;
                if (StringUtils.isEmpty(((TranslationM) question).getUserAnswer())){
                    no++;
                }/* else if (((TranslationM) question).getAnswer().equals(((TranslationM) question).getUserAnswer())) {
                    right++;
                } else {
                    wrong++;
                }*/
            } else if (question instanceof WritingM){
                total++;
                if (StringUtils.isEmpty(((WritingM) question).getUserAnswer())){
                    no++;
                }/* else if (((WritingM) question).getAnswer().equals(((WritingM) question).getUserAnswer())) {
                    right++;
                } else {
                    wrong++;
                }*/
            }
        }

        tvTotal.setText(String.valueOf(total));
        tvRight.setText(String.valueOf(right));
        tvWrong.setText(String.valueOf(wrong));
        tvNo.setText(String.valueOf(no));

        if (right+wrong>0) {
            progressViewCircleSmall2.setEndProgress((float) right / (right + wrong) * 100);
            progressViewCircleSmall2.startProgressAnimation();
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                navigateTo(HomeActivity.class);
                break;
            case R.id.btn_commit:
//                navigateTo(QuestionActivity.class);
                Intent intent = new Intent(this, QuestionActivity.class);
                intent.putExtra("select", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        navigateTo(HomeActivity.class);
    }

    @Override
    public void onCircleProgressStart(View view) {

    }

    @Override
    public void onCircleProgressUpdate(View view, float progress) {
        int progressInt = (int) progress;
        if (view.getId() == R.id.progressView_circle_small2){
            progressTextMain.setText(progressInt + "");
        }
    }

    @Override
    public void onCircleProgressFinished(View view) {

    }

    @Override
    public void onDestroy() {
        progressViewCircleSmall2.stopProgressAnimation();

        progressViewCircleSmall2.setProgressViewUpdateListener(null);
        super.onDestroy();
    }
}