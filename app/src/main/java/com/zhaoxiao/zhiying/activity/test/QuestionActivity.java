package com.zhaoxiao.zhiying.activity.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager.widget.ViewPager;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.community.ArticleSelectActivity;
import com.zhaoxiao.zhiying.activity.community.PublishTrendActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.activity.study.ChannelActivity;
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.adapter.test.MyTestAdapter;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.data.DataSource;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Data;
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
import com.zhaoxiao.zhiying.entity.test.QuestionAnswer;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.SubQuestionAnswer;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;
import com.zhaoxiao.zhiying.fragment.test.QuestionFragment;
import com.zhaoxiao.zhiying.fragment.test.SubQuestionFragment;
import com.zhaoxiao.zhiying.fragment.test.TestListeningFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.fl_test_listening)
    FragmentContainerView flTestListening;
    float currentProgress;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private int position;

    private List<QuestionM> questionList;
    private Map<String, Object> map;
    private String subType;

    private String account;

    private long startTime;
    private long endTime;
    private long duration;
    private UserService userService;

    private TestService testService;

    public final static int ANSWER_SHEET_RESULT = 10;
    private boolean toSheet = false;

    private boolean select = true;
    private Map<String,Object> map1;

    private TestListeningFragment testListeningFragment;
    private TestReceiver testReceiver;

    @Override
    protected int initLayout() {
        return R.layout.activity_question;
    }

    @Override
    protected void initData() {
        testListeningFragment = (TestListeningFragment) getSupportFragmentManager().findFragmentById(R.id.fl_test_listening);
        //注册广播
        testReceiver = new TestReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TestListeningFragment.ACTION_TEST_INIT);
        intentFilter.addAction(TestListeningFragment.ACTION_TEST_START);
        intentFilter.addAction(TestListeningFragment.ACTION_TEST_STOP);
        intentFilter.addAction(TestListeningFragment.ACTION_TEST_COMPLETE);
        registerReceiver(testReceiver, intentFilter);

        map = (Map<String, Object>) getIntent().getSerializableExtra("map");
        if (map.get("select") != null) {
            select = (boolean) map.get("select");
        }
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

        testService = (TestService) getService(TestService.class);

        preAndNext(position);
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new MyTestAdapter(getSupportFragmentManager(), mFragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((QuestionFragment) mFragments.get(QuestionActivity.this.position)).setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                toSheet = false;
                if (!account.equals("") && !account.equals("已过期")) {
                    saveAnswer(QuestionActivity.this.position);
                }

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

                testListeningFragment.pause();
                testListeningFragment.seekTo(0);
                testListeningFragment.release();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //添加测试记录
        account = SpUtils.getInstance(this).getString("account", "");
        if (!account.equals("") && !account.equals("已过期")) {
//            addTestRecord(account, questionId,table);
            userService = (UserService) getService(UserService.class);
        }
    }

    private void saveAnswer(int position) {
        QuestionM question = questionList.get(position);
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestionId(question.getId());
        questionAnswer.setAccount(account);
        List<SubQuestionAnswer> subQuestionAnswerList = new ArrayList<>();
        boolean fill = true;
        boolean right = true;
        if (question instanceof ListeningM) {
            questionAnswer.setTable(1);
            for (ListeningQuestion listeningQuestion : ((ListeningM) question).getListeningQuestionList()) {
                if (StringUtils.isEmpty(listeningQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!listeningQuestion.getAnswer().equals(listeningQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(listeningQuestion.getId(), listeningQuestion.getUserAnswer(),
                            listeningQuestion.getAnswer().equals(listeningQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof BankedM) {
            questionAnswer.setTable(2);
            for (BankedQuestion bankedQuestion : ((BankedM) question).getBankedQuestionList()) {
                if (StringUtils.isEmpty(bankedQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!bankedQuestion.getAnswer().equals(bankedQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(bankedQuestion.getId(), bankedQuestion.getUserAnswer(),
                            bankedQuestion.getAnswer().equals(bankedQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof MatchM) {
            questionAnswer.setTable(3);
            for (MatchQuestion matchQuestion : ((MatchM) question).getMatchQuestionList()) {
                if (StringUtils.isEmpty(matchQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!matchQuestion.getAnswer().equals(matchQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(matchQuestion.getId(), matchQuestion.getUserAnswer(),
                            matchQuestion.getAnswer().equals(matchQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof CarefulM) {
            questionAnswer.setTable(4);
            for (CarefulQuestion carefulQuestion : ((CarefulM) question).getCarefulQuestionList()) {
                if (StringUtils.isEmpty(carefulQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!carefulQuestion.getAnswer().equals(carefulQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(carefulQuestion.getId(), carefulQuestion.getUserAnswer(),
                            carefulQuestion.getAnswer().equals(carefulQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof ClozeM) {
            questionAnswer.setTable(7);
            for (ClozeQuestion clozeQuestion : ((ClozeM) question).getClozeQuestionList()) {
                if (StringUtils.isEmpty(clozeQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!clozeQuestion.getAnswer().equals(clozeQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(clozeQuestion.getId(), clozeQuestion.getUserAnswer(),
                            clozeQuestion.getAnswer().equals(clozeQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof NewM) {
            questionAnswer.setTable(8);
            for (NewQuestion newQuestion : ((NewM) question).getNewQuestionList()) {
                if (StringUtils.isEmpty(newQuestion.getUserAnswer())){
                    fill = false;
                    return;
                } else {
                    if (!newQuestion.getAnswer().equals(newQuestion.getUserAnswer())){
                        right = false;
                    }
                    subQuestionAnswerList.add(new SubQuestionAnswer(newQuestion.getId(), newQuestion.getUserAnswer(),
                            newQuestion.getAnswer().equals(newQuestion.getUserAnswer())));
                }
            }
        } else if (question instanceof TranslationM) {
            questionAnswer.setTable(5);
            if (!((TranslationM) question).getAnswer().equals(((TranslationM) question).getUserAnswer())){
                right = false;
            }
            if (StringUtils.isEmpty(((TranslationM) question).getUserAnswer())){
                return;
            }
            questionAnswer.setAnswer(((TranslationM) question).getUserAnswer());
        } else if (question instanceof WritingM) {
            questionAnswer.setTable(6);
            if (!((WritingM) question).getAnswer().equals(((WritingM) question).getUserAnswer())){
                right = false;
            }
            if (StringUtils.isEmpty(((WritingM) question).getUserAnswer())){
                return;
            }
            questionAnswer.setAnswer(((WritingM) question).getUserAnswer());
        }
        questionAnswer.setRight(right);
        questionAnswer.setSubQuestionAnswerList(subQuestionAnswerList);

        Call<Data<Boolean>> saveAnswerCall = testService.saveAnswer(questionAnswer);
        saveAnswerCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        System.out.println("保存答案成功");
                        if (toSheet) {
//                            map1 = new HashMap<>();
//                            map1.put("questionList", questionList);
//                            map1.put("select", select);
//                            navigateToForResult(AnswerSheetActivity.class, "map", (Serializable) map1, ANSWER_SHEET_RESULT);
                        }
                    } else {
                        System.out.println("保存答案失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                System.out.println("请求失败");
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
//                new BottomSheet.BottomListSheetBuilder(this)
//                        .setTitle("更多")
//                        .addItem("收藏")
//                        .addItem("笔记")
//                        .addItem("分享")
//                        .setIsCenter(true)
//                        .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
//                            dialog.dismiss();
////                            XToastUtils.toast("Item " + (position + 1));
//                            switch (position){
//                                case 0:
//                                    XToastUtils.toast("1");
//                                    break;
//                                case 1:
//                                    XToastUtils.toast("2");
//                                    break;
//                                case 2:
//                                    XToastUtils.toast("3");
//                                    break;
//                            }
//                        })
//                        .build()
//                        .show();
                showSimpleBottomSheetList();
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
//                    XToastUtils.toast("交卷");
                    toSheet = true;
                    map1 = new HashMap<>();
                    map1.put("questionList", questionList);
                    map1.put("select", select);
                    navigateToForResult(AnswerSheetActivity.class, "map", (Serializable) map1, ANSWER_SHEET_RESULT);
                    saveAnswer(position);
//                    navigateToForResult(AnswerSheetActivity.class,"questionList", (Serializable) questionList, ANSWER_SHEET_RESULT);
//                    navigateTo(AnswerSheetActivity.class,"questionList", (Serializable) questionList);
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
                toSheet = true;
                if (!account.equals("") && !account.equals("已过期")) {
                    saveAnswer(position);
                    map1 = new HashMap<>();
                    map1.put("questionList", questionList);
                    map1.put("select", select);
                    navigateToForResult(AnswerSheetActivity.class, "map", (Serializable) map1, ANSWER_SHEET_RESULT);
                } else {
                    map1 = new HashMap<>();
                    map1.put("questionList", questionList);
                    map1.put("select", select);
                    navigateToForResult(AnswerSheetActivity.class, "map", (Serializable) map1, ANSWER_SHEET_RESULT);
                }
//                navigateToForResult(AnswerSheetActivity.class,"questionList", (Serializable) questionList, ANSWER_SHEET_RESULT);
//                navigateTo(AnswerSheetActivity.class,"questionList", (Serializable) questionList);
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
        try {
            this.unregisterReceiver(testReceiver);
        } catch (IllegalArgumentException e) {
            // 忽略“接收器未注册”的异常
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
//        setUnselect();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setUnselect();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            if (requestCode == ANSWER_SHEET_RESULT){
                int i = data.getIntExtra("i",0);
                int j = data.getIntExtra("j",0);
                viewPager.setCurrentItem(i);
                ((QuestionFragment) mFragments.get(i)).setCurrentItem(j);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 处理传递过来的参数
        select = intent.getBooleanExtra("select",false);
//        setUnselect();

        viewPager.setCurrentItem(0);
    }

    private void setUnselect() {
        Intent intent;
        if (select){
            intent = new Intent(SubQuestionFragment.ACTION_QUESTION_SELECT);
        } else {
            intent = new Intent(SubQuestionFragment.ACTION_QUESTION_UNSELECT);
        }
        System.out.println("发送广播");
        sendBroadcast(intent);
    }

    public boolean getSelect(){
        return select;
    }

    //自定义广播
    public class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ((QuestionFragment)mFragments.get(position)).UIControl(intent.getAction());
            if (intent.getAction().equals(TestListeningFragment.ACTION_TEST_COMPLETE)){
                if(position-1>=0)((QuestionFragment)mFragments.get(position-1)).UIControl(intent.getAction());
            }
        }
    }

    public TestListeningFragment getTestListeningFragment(){
        return testListeningFragment;
    }

    //更多弹窗
    private void showSimpleBottomSheetList() {
        if (questionList.size()==0){
            return;
        }
        QuestionM question = questionList.get(position);
        boolean isCollect = question.getCollectStatus();
        Map<String, Object> map = new HashMap<>();
        map.put("questionInfo", question.getInfo());
        map.put("subType", question.getSubType());
        map.put("questionId", question.getId());
        int table = 0;
        if (question instanceof ListeningM) {
            table = 1;
        } else if (question instanceof BankedM) {
            table = 2;
        } else if (question instanceof MatchM) {
            table = 3;
        } else if (question instanceof CarefulM) {
            table = 4;
        } else if (question instanceof ClozeM) {
            table = 5;
        } else if (question instanceof NewM) {
            table = 6;
        } else if (question instanceof TranslationM) {
            table = 7;
        } else if (question instanceof WritingM) {
            table = 8;
        }
        map.put("table", table);

        BottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new BottomSheet.BottomListSheetBuilder(this);
        bottomListSheetBuilder
                .setTitle("更多")
                .addItem("笔记");
        if (isCollect){
            bottomListSheetBuilder
                    .addItem("取消收藏");
        } else {
            bottomListSheetBuilder
                    .addItem("收藏");
        }
        int finalTable = table;
        bottomListSheetBuilder
                .addItem("分享")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position){
                        case 0:
                            if (!account.equals("") && !account.equals("已过期")) {
                                map.put("link",false);
                                map.put("edit",true);
                                navigateTo(TestNoteActivity.class, "map", (Serializable) map);
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                        case 1:
                            if (!account.equals("") && !account.equals("已过期")) {
                                collect(!isCollect,question, finalTable);
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                        case 2:
                            if (!account.equals("") && !account.equals("已过期")) {
//                                XToastUtils.toast("分享");
                                Map<String, Object> linkMap = new HashMap<>();
                                linkMap.put("linkId", question);
                                linkMap.put("info", question.getInfo());
                                linkMap.put("type", "题目");
                                linkMap.put("linkType", 2);
                                navigateTo(PublishTrendActivity.class, "linkMap", (Serializable) linkMap);
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                    }
                })
                .build()
                .show();
    }

    private void collect(boolean collect,QuestionM question,int table) {
        Call<Data<Boolean>> collectCall = testService.collect(account, question.getId(), table, collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        question.setCollectStatus(collect);
                        if (collect){
                            XToastUtils.toast("收藏成功");
                        } else {
                            XToastUtils.toast("取消收藏成功");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (collect) {
                    XToastUtils.toast("收藏失败");
                } else {
                    XToastUtils.toast("取消收藏失败");
                }
            }
        });
    }
}