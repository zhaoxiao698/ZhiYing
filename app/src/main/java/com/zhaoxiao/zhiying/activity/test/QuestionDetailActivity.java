package com.zhaoxiao.zhiying.activity.test;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.test.BankedM;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.ClozeM;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.NewM;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.fragment.test.QuestionFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.fl_question)
    FrameLayout flQuestion;
    private Map<String,Integer> map;
    private int questionId;
    private int table;
    private QuestionM question;
    private TestService testService;
    private FragmentManager fragmentManager;

    private String account;

    private long startTime;
    private long endTime;
    private long duration;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void initData() {
        map = (Map<String, Integer>) getIntent().getSerializableExtra("map");
        questionId = map.get("questionId");
        table = map.get("table");
        switch (table){
            case 1:tvTitle.setText("听力");break;
            case 2:tvTitle.setText("选词填空");break;
            case 3:tvTitle.setText("匹配");break;
            case 4:tvTitle.setText("仔细阅读");break;
            case 5:tvTitle.setText("翻译");break;
            case 6:tvTitle.setText("写作");break;
            case 7:tvTitle.setText("完形填空");break;
            case 8:tvTitle.setText("新题型");break;
        }
        testService = (TestService) getService(TestService.class);
        getQuestion();

        //添加测试记录
        account = SpUtils.getInstance(this).getString("account", "");
        if (!account.equals("") && !account.equals("已过期")) {
            addTestRecord(account, questionId,table);
            userService = (UserService) getService(UserService.class);
        }
    }

    private void addTestRecord(String account, int questionId, int table) {
        Call<Data<Boolean>> addTestRecordCall = testService.addTestRecord(account, questionId, table);
        addTestRecordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        System.out.println("添加测试记录成功");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    private void getQuestion() {
        switch (table){
            case 1:
                Call<Data<ListeningM>> questionByIdCall1 = testService.getQuestionById1(questionId, table);
                questionByIdCall1.enqueue(new Callback<Data<ListeningM>>() {
                    @Override
                    public void onResponse(Call<Data<ListeningM>> call, Response<Data<ListeningM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<ListeningM>> call, Throwable t) {

                    }
                });
                break;
            case 2:
                Call<Data<BankedM>> questionByIdCall2 = testService.getQuestionById2(questionId, table);
                questionByIdCall2.enqueue(new Callback<Data<BankedM>>() {
                    @Override
                    public void onResponse(Call<Data<BankedM>> call, Response<Data<BankedM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<BankedM>> call, Throwable t) {

                    }
                });
                break;
            case 3:
                Call<Data<MatchM>> questionByIdCall3 = testService.getQuestionById3(questionId, table);
                questionByIdCall3.enqueue(new Callback<Data<MatchM>>() {
                    @Override
                    public void onResponse(Call<Data<MatchM>> call, Response<Data<MatchM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<MatchM>> call, Throwable t) {

                    }
                });
                break;
            case 4:
                Call<Data<CarefulM>> questionByIdCall4 = testService.getQuestionById4(questionId, table);
                questionByIdCall4.enqueue(new Callback<Data<CarefulM>>() {
                    @Override
                    public void onResponse(Call<Data<CarefulM>> call, Response<Data<CarefulM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<CarefulM>> call, Throwable t) {

                    }
                });
                break;
            case 5:
                Call<Data<TranslationM>> questionByIdCall5 = testService.getQuestionById5(questionId, table);
                questionByIdCall5.enqueue(new Callback<Data<TranslationM>>() {
                    @Override
                    public void onResponse(Call<Data<TranslationM>> call, Response<Data<TranslationM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<TranslationM>> call, Throwable t) {

                    }
                });
                break;
            case 6:
                Call<Data<WritingM>> questionByIdCall6 = testService.getQuestionById6(questionId, table);
                questionByIdCall6.enqueue(new Callback<Data<WritingM>>() {
                    @Override
                    public void onResponse(Call<Data<WritingM>> call, Response<Data<WritingM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<WritingM>> call, Throwable t) {

                    }
                });
                break;
            case 7:
                Call<Data<ClozeM>> questionByIdCall7 = testService.getQuestionById7(questionId, table);
                questionByIdCall7.enqueue(new Callback<Data<ClozeM>>() {
                    @Override
                    public void onResponse(Call<Data<ClozeM>> call, Response<Data<ClozeM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<ClozeM>> call, Throwable t) {

                    }
                });
                break;
            case 8:
                Call<Data<NewM>> questionByIdCall8 = testService.getQuestionById8(questionId, table);
                questionByIdCall8.enqueue(new Callback<Data<NewM>>() {
                    @Override
                    public void onResponse(Call<Data<NewM>> call, Response<Data<NewM>> response) {
                        if (response.body() != null && response.body().getCode() == 10000) {
                            question = response.body().getData();
                            tvTitle.setText(question.getSubType());
                            QuestionFragment questionFragment = QuestionFragment.newInstance(question);
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fl_question, questionFragment).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Data<NewM>> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                new BottomSheet.BottomListSheetBuilder(this)
                        .setTitle("更多")
                        .addItem("收藏")
                        .addItem("笔记")
                        .addItem("分享")
                        .setIsCenter(true)
                        .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                            dialog.dismiss();
//                            XToastUtils.toast("Item " + (position + 1));
                            switch (position) {
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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        if (!account.equals("") && !account.equals("已过期")) {
            addPlanDo();
        }
    }

    private void addPlanDo(){
        Call<Data<Boolean>> addPlanDoCall = userService.addPlanDo(account, duration);
        addPlanDoCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        System.out.println("添加学习记录："+duration+"ms");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}