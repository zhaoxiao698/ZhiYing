package com.zhaoxiao.zhiying.fragment.test;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.xui.widget.progress.CircleProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.mine.CollectionActivity;
import com.zhaoxiao.zhiying.activity.mine.HistoryActivity;
import com.zhaoxiao.zhiying.activity.mine.NoteListActivity;
import com.zhaoxiao.zhiying.activity.mine.WrongQuestionActivity;
import com.zhaoxiao.zhiying.activity.test.QuestionActivity;
import com.zhaoxiao.zhiying.activity.test.SetQuestionActivity;
import com.zhaoxiao.zhiying.activity.test.TruePaperListActivity;
import com.zhaoxiao.zhiying.activity.test.WordTestActivity;
import com.zhaoxiao.zhiying.activity.word.WordActivity;
import com.zhaoxiao.zhiying.adapter.test.TestFtypeAdapter;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.test.BankedM;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.ClozeM;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.NewM;
import com.zhaoxiao.zhiying.entity.test.Paper;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TestFtype;
import com.zhaoxiao.zhiying.entity.test.TranslationM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CustomDialog;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedFragment extends BaseFragment implements CircleProgressView.CircleProgressUpdateListener {
    //    @BindView(R.id.tv_questionBank)
//    TextView tvQuestionBank;
    @BindView(R.id.progressView_circle_small1)
    CircleProgressView progressViewCircleSmall1;
    @BindView(R.id.progressView_circle_small2)
    CircleProgressView progressViewCircleSmall2;
    @BindView(R.id.progress_text_main)
    TextView progressTextMain;
    //    @BindView(R.id.recycler_view)
//    RecyclerView recyclerView;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    private int questionBankId;

    private List<TestFtype> ftypeList;
    private TestFtypeAdapter ftypeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TestService testService;
    private int num = 0, finish = 0, right = 0;

    private CustomDialog customDialog;

    private boolean random = true;
    private int limitNum = 10;

//    private List<TreeNode<QuestionType>> dataToBind = new ArrayList<>();
//
//    MySingleLayoutTreeAdapter adapter;

    private String account;

    private int source;

    public SelectedFragment() {
    }

    public static SelectedFragment newInstance(int questionBankId) {
        SelectedFragment fragment = new SelectedFragment();
        fragment.questionBankId = questionBankId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initData() {
        customDialog = new CustomDialog(getContext());

        TestFragment testFragment = (TestFragment) getParentFragment();
        switch (questionBankId) {
            case 1:
                tvBank.setText("四级");
                testFragment.changeQuestionBank("四级");
                break;
            case 2:
                tvBank.setText("六级");
                testFragment.changeQuestionBank("六级");
                break;
            case 3:
                tvBank.setText("英一");
                testFragment.changeQuestionBank("英一");
                break;
            case 4:
                tvBank.setText("英二");
                testFragment.changeQuestionBank("英二");
                break;
            default:
                tvBank.setText("题库" + questionBankId);
                testFragment.changeQuestionBank("题库" + questionBankId);
                break;
        }
        progressViewCircleSmall1.setEndProgress((float) (549.0 / 1500 * 100));
        progressViewCircleSmall1.setProgressViewUpdateListener(this);
        progressViewCircleSmall2.setProgressViewUpdateListener(this);

        //树形控件
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        dataToBind.clear();
//        dataToBind.addAll(TreeDataUtils.convertDataToTreeNode(DataSource.getQuestionType()));
//        adapter = new MySingleLayoutTreeAdapter(R.layout.view_tree_level_0, dataToBind);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnTreeClickedListener(new SingleLayoutTreeAdapter.OnTreeClickedListener() {
//            @Override
//            public void onNodeClicked(View view, TreeNode treeNode, int i) {
//                ImageView icon = view.findViewById(R.id.level_icon);
//                if(treeNode.isExpand()){
//                    icon.setImageResource(R.drawable.tree_icon_collapse);
//                }else{
//                    icon.setImageResource(R.drawable.tree_icon_expand);
//                }
//            }
//
//            @Override
//            public void onLeafClicked(View view, TreeNode treeNode, int i) {
//
//            }
//        });


        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getTestFtypeList();
            }
        });

        account = SpUtils.getInstance(getContext()).getString("account", "");

        source = getStringFromSp("question_source", false);
        if (source==-1){
            source = 1;
        }
        limitNum = getStringFromSp("question_num", false);
        if (limitNum==-1){
            limitNum = 10;
        }

        testService = (TestService) getService(TestService.class);
//        ftypeList = DataSource.getTestFtype();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ftypeAdapter = new TestFtypeAdapter(getContext());
        rv.setAdapter(ftypeAdapter);
        ftypeAdapter.setOnItemClickListener(new TestFtypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int ftypeId) {

            }

            @Override
            public void onStypeClick(int ftypeId, int stypeId) {
//                navigateTo(QuestionActivity.class);
                special(ftypeId, stypeId);
            }
        });
        getTestFtypeList();

        rv.setNestedScrollingEnabled(false);
        rv.setFocusable(false);
    }

    private void getTestFtypeList() {
        Call<Data<List<TestFtype>>> ftypeCall = testService.getTestFtypeList(account, questionBankId);
        ftypeCall.enqueue(new Callback<Data<List<TestFtype>>>() {
            @Override
            public void onResponse(Call<Data<List<TestFtype>>> call, Response<Data<List<TestFtype>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<TestFtype> list = response.body().getData();
                    System.out.println(list);
                    ftypeAdapter.setList(list);
                    ftypeAdapter.notifyDataSetChanged();
                    sum(list);
                    srl.finishRefresh();
//                    srl.finishLoadMore();
//                    showToast("请求类型成功");
                } /*else showToast("请求类型失败");*/
                System.out.println("responseBody:" + response.body());
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Data<List<TestFtype>>> call, Throwable t) {
                showToast("请求未完成");
            }
        });
    }

    private void sum(List<TestFtype> list) {
        num = 0;
        finish = 0;
        right = 0;
        for (TestFtype ftype : list) {
            num += ftype.getNum();
            finish += ftype.getFinish();
            right += ftype.getRight();
        }
//        num = 1000;
//        finish = 500;
//        right = 400;
        if (finish >= num) {
            finish = num;
        }
        if (right >= finish) {
            right = finish;
        }
        tvNum.setText(finish + "/" + num);
        if (num>0) {
            progressViewCircleSmall1.setEndProgress((float) finish / num * 100);
        } else {
            progressViewCircleSmall1.setEndProgress(0);
        }
        if (finish>0){
            progressViewCircleSmall2.setEndProgress((float) right / finish * 100);
        } else {
            progressViewCircleSmall2.setEndProgress(0);
        }
        startProgress();
    }

    @Override
    public void onStart() {
        super.onStart();
        startProgress();
    }

    public void startProgress() {
        progressViewCircleSmall1.startProgressAnimation();
        progressViewCircleSmall2.startProgressAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCircleProgressStart(View view) {

    }

    @Override
    public void onCircleProgressUpdate(View view, float progress) {
        int progressInt = (int) progress;
        if (view.getId() == R.id.progressView_circle_small1) {
            int finish = Math.round(progress * num / 100);
            tvNum.setText(finish + "/" + num);
        } else if (view.getId() == R.id.progressView_circle_small2) {
            progressTextMain.setText(progressInt + "");
        }
    }

    @Override
    public void onCircleProgressFinished(View view) {

    }

    @Override
    public void onDestroyView() {
        progressViewCircleSmall1.stopProgressAnimation();
        progressViewCircleSmall2.stopProgressAnimation();

        progressViewCircleSmall1.setProgressViewUpdateListener(null);
        progressViewCircleSmall2.setProgressViewUpdateListener(null);
        super.onDestroyView();
    }

    //专项训练
    private void special(int ftypeId, int stypeId) {
        customDialog.show();
        switch (ftypeId) {
            case 1:
                Call<Data<List<ListeningM>>> listeningCall = testService.getListeningList(random, limitNum, questionBankId, 0, stypeId,source,account);
                listeningCall.enqueue(new Callback<Data<List<ListeningM>>>() {
                    @Override
                    public void onResponse(Call<Data<List<ListeningM>>> call, Response<Data<List<ListeningM>>> response) {
                        List<ListeningM> list = response.body().getData();
                        Map<String, Object> map = new HashMap<>();
                        map.put("questionList", list);
                        String subType = "听力";
                        switch (stypeId) {
                            case 1:
                                subType = "短篇新闻";
                                break;
                            case 2:
                                subType = "长对话";
                                break;
                            case 3:
                                subType = "听力篇章";
                                break;
                            case 4:
                                subType = "讲话/报道/讲座";
                                break;
                        }
                        map.put("subType", subType);
                        navigateTo(QuestionActivity.class, "map", (Serializable) map);
                        RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                    }

                    @Override
                    public void onFailure(Call<Data<List<ListeningM>>> call, Throwable t) {

                    }
                });
                break;
            case 2:
                switch (stypeId) {
                    case 5:
                        Call<Data<List<BankedM>>> bankedCall = testService.getBankedList(random, limitNum, questionBankId,source,account);
                        bankedCall.enqueue(new Callback<Data<List<BankedM>>>() {
                            @Override
                            public void onResponse(Call<Data<List<BankedM>>> call, Response<Data<List<BankedM>>> response) {
                                List<BankedM> list = response.body().getData();
                                Map<String, Object> map = new HashMap<>();
                                map.put("questionList", list);
                                String subType = "选词填空";
                                map.put("subType", subType);
                                navigateTo(QuestionActivity.class, "map", (Serializable) map);
                                RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                            }

                            @Override
                            public void onFailure(Call<Data<List<BankedM>>> call, Throwable t) {

                            }
                        });
                        break;
                    case 6:
                        Call<Data<List<MatchM>>> matchCall = testService.getMatchList(random, limitNum, questionBankId,source,account);
                        matchCall.enqueue(new Callback<Data<List<MatchM>>>() {
                            @Override
                            public void onResponse(Call<Data<List<MatchM>>> call, Response<Data<List<MatchM>>> response) {
                                List<MatchM> list = response.body().getData();
                                Map<String, Object> map = new HashMap<>();
                                map.put("questionList", list);
                                String subType = "匹配";
                                map.put("subType", subType);
                                navigateTo(QuestionActivity.class, "map", (Serializable) map);
                                RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                            }

                            @Override
                            public void onFailure(Call<Data<List<MatchM>>> call, Throwable t) {

                            }
                        });
                        break;
                    case 7:
                        Call<Data<List<CarefulM>>> carefulCall = testService.getCarefulList(random, limitNum, questionBankId,source,account);
                        carefulCall.enqueue(new Callback<Data<List<CarefulM>>>() {
                            @Override
                            public void onResponse(Call<Data<List<CarefulM>>> call, Response<Data<List<CarefulM>>> response) {
                                List<CarefulM> list = response.body().getData();
                                Map<String, Object> map = new HashMap<>();
                                map.put("questionList", list);
                                String subType = "仔细阅读";
                                map.put("subType", subType);
                                navigateTo(QuestionActivity.class, "map", (Serializable) map);
                                RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                            }

                            @Override
                            public void onFailure(Call<Data<List<CarefulM>>> call, Throwable t) {

                            }
                        });
                        break;
                    case 8:
                    case 9:
                    case 10:
                        Call<Data<List<NewM>>> newCall = testService.getNewList(random, limitNum, questionBankId, stypeId,source,account);
                        newCall.enqueue(new Callback<Data<List<NewM>>>() {
                            @Override
                            public void onResponse(Call<Data<List<NewM>>> call, Response<Data<List<NewM>>> response) {
                                List<NewM> list = response.body().getData();
                                Map<String, Object> map = new HashMap<>();
                                map.put("questionList", list);
                                String subType = "新题型";
                                switch (stypeId) {
                                    case 8:
                                        subType = "新题型-七选五";
                                        break;
                                    case 9:
                                        subType = "新题型-排序";
                                        break;
                                    case 10:
                                        subType = "新题型-小标题";
                                        break;
                                }
                                map.put("subType", subType);
                                navigateTo(QuestionActivity.class, "map", (Serializable) map);
                                RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                            }

                            @Override
                            public void onFailure(Call<Data<List<NewM>>> call, Throwable t) {

                            }
                        });
                        break;
                }
                break;
            case 3:
                Call<Data<List<TranslationM>>> translationCall = testService.getTranslationList(random, limitNum, questionBankId,source,account);
                translationCall.enqueue(new Callback<Data<List<TranslationM>>>() {
                    @Override
                    public void onResponse(Call<Data<List<TranslationM>>> call, Response<Data<List<TranslationM>>> response) {
                        List<TranslationM> list = response.body().getData();
                        Map<String, Object> map = new HashMap<>();
                        map.put("questionList", list);
                        String subType = "翻译";
                        switch (stypeId) {
                            case 11:
                                subType = "汉译英";
                                break;
                            case 12:
                                subType = "英译汉";
                                break;
                        }
                        map.put("subType", subType);
                        navigateTo(QuestionActivity.class, "map", (Serializable) map);
                        RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                    }

                    @Override
                    public void onFailure(Call<Data<List<TranslationM>>> call, Throwable t) {

                    }
                });
                break;
            case 4:
                Call<Data<List<WritingM>>> writingCall = testService.getWritingList(random, limitNum, questionBankId, stypeId,source,account);
                writingCall.enqueue(new Callback<Data<List<WritingM>>>() {
                    @Override
                    public void onResponse(Call<Data<List<WritingM>>> call, Response<Data<List<WritingM>>> response) {
                        List<WritingM> list = response.body().getData();
                        Map<String, Object> map = new HashMap<>();
                        map.put("questionList", list);
                        String subType = "写作";
                        switch (stypeId) {
                            case 13:
                                subType = "短文写作";
                                break;
                            case 14:
                                subType = "应用文";
                                break;
                        }
                        map.put("subType", subType);
                        navigateTo(QuestionActivity.class, "map", (Serializable) map);
                        RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                    }

                    @Override
                    public void onFailure(Call<Data<List<WritingM>>> call, Throwable t) {

                    }
                });
                break;
            case 5:
                Call<Data<List<ClozeM>>> clozeCall = testService.getClozeList(random, limitNum, questionBankId,source,account);
                clozeCall.enqueue(new Callback<Data<List<ClozeM>>>() {
                    @Override
                    public void onResponse(Call<Data<List<ClozeM>>> call, Response<Data<List<ClozeM>>> response) {
                        List<ClozeM> list = response.body().getData();
                        Map<String, Object> map = new HashMap<>();
                        map.put("questionList", list);
                        String subType = "完形填空";
                        map.put("subType", subType);
                        navigateTo(QuestionActivity.class, "map", (Serializable) map);
                        RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                    }

                    @Override
                    public void onFailure(Call<Data<List<ClozeM>>> call, Throwable t) {

                    }
                });
        }
    }

    //模拟考试
    private void exam(){
        customDialog.show();
        Call<Data<Paper>> examCall = testService.getExam(questionBankId,account);
        examCall.enqueue(new Callback<Data<Paper>>() {
            @Override
            public void onResponse(Call<Data<Paper>> call, Response<Data<Paper>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Paper paper = response.body().getData();
                    List<QuestionM> list = new ArrayList<>();
                    if (questionBankId==1||questionBankId==2){
                        list.addAll(paper.getListeningList());
                        list.addAll(paper.getBankedList());
                        list.addAll(paper.getMatchList());
                        list.addAll(paper.getCarefulList());
                        list.addAll(paper.getTranslationList());
                        list.addAll(paper.getWritingList());
                    } else if (questionBankId==3||questionBankId==4){
                        list.addAll(paper.getClozeList());
                        list.addAll(paper.getCarefulList());
                        list.addAll(paper.getNewList());
                        list.addAll(paper.getTranslationList());
                        list.addAll(paper.getWritingList());
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("questionList",list);
                    String subType = "模拟考试";
                    map.put("subType",subType);
                    navigateTo(QuestionActivity.class, "map", (Serializable) map);
                    RxJavaUtils.delay(500, TimeUnit.MILLISECONDS, aLong -> customDialog.dismiss(), System.out::println);
                }
            }

            @Override
            public void onFailure(Call<Data<Paper>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.ll_word, R.id.ll_true, R.id.ll_exam, R.id.ll_history, R.id.ll_wrong, R.id.ll_collection, R.id.ll_note, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_word:
                navigateTo(WordActivity.class);
                break;
            case R.id.ll_true:
                navigateTo(TruePaperListActivity.class);
                break;
            case R.id.ll_exam:
                exam();
                break;
            case R.id.ll_history:
                navigateTo(HistoryActivity.class,"select",1);
                break;
            case R.id.ll_wrong:
                navigateTo(WrongQuestionActivity.class);
                break;
            case R.id.ll_collection:
                navigateTo(CollectionActivity.class,"select",2);
                break;
            case R.id.ll_note:
                navigateTo(NoteListActivity.class,"select",1);
                break;
            case R.id.ll_setting:
                navigateTo(SetQuestionActivity.class);
                break;
        }
    }
}