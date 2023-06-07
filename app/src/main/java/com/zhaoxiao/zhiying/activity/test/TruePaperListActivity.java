package com.zhaoxiao.zhiying.activity.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.test.TruePaperAdapter;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.Paper;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TruePaper;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TruePaperListActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;
    private TestService testService;
    private List<TruePaper> truePaperList;
    private TruePaperAdapter truePaperAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;
    private int questionBankId;

    @Override
    protected int initLayout() {
        return R.layout.activity_true_paper_list;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getTruePaperList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getTruePaperList(2);
            }
        });

        testService = (TestService) getService(TestService.class);
        account = SpUtils.getInstance(this).getString("account","");
        questionBankId = getStringFromSp("questionBankId",false);

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        truePaperAdapter = new TruePaperAdapter(this);
        rv.setAdapter(truePaperAdapter);
        truePaperAdapter.setOnItemClickListener(this::getTruePaper);
        getTruePaperList(0);
    }

    private void getTruePaperList(int type){
        Call<Data<PageInfo<TruePaper>>> truePaperListCall = testService.getTruePaperList(pageNum, 8, questionBankId);
        truePaperListCall.enqueue(new Callback<Data<PageInfo<TruePaper>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<TruePaper>>> call, Response<Data<PageInfo<TruePaper>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<TruePaper> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            truePaperList = list;
                            truePaperAdapter.setList(truePaperList);
                            truePaperAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            truePaperList = list;
                            truePaperAdapter.setList(truePaperList);
                            truePaperAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            srl.finishRefresh();
                            break;
                        case 2:
                            if (pageNum > response.body().getData().getPages()) {
                                pageNum = response.body().getData().getPageNum();
                                srl.finishLoadMore();
                                XToastUtils.toast("没有更多数据了");
                                break;
                            }
                            truePaperList.addAll(list);
                            truePaperAdapter.setList(truePaperList);
                            truePaperAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<TruePaper>>> call, Throwable t) {

            }
        });
    }

    private void getTruePaper(TruePaper truePaper) {
        Call<Data<Paper>> truePaperCall = testService.getTruePaper(truePaper.getId(),account);
        truePaperCall.enqueue(new Callback<Data<Paper>>() {
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
                    String subType = truePaper.getName();
                    map.put("subType",subType);
                    navigateTo(QuestionActivity.class, "map", (Serializable) map);
                }
            }

            @Override
            public void onFailure(Call<Data<Paper>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getMyBgColor(),0);
    }
}