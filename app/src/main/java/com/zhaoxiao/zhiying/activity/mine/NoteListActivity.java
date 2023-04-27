package com.zhaoxiao.zhiying.activity.mine;

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
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.adapter.mine.ArticleNoteAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.ArticleNote;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private int pageNum = 1;
    private StudyService studyService;
    private List<ArticleNoteDetail> articleNoteList;
    private ArticleNoteAdapter articleNoteAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_note_list;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getNoteList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getNoteList(2);
            }
        });

        studyService = (StudyService) getService(StudyService.class);

        account = SpUtils.getInstance(this).getString("account","");

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        articleNoteAdapter = new ArticleNoteAdapter(this);
        articleNoteAdapter.setOnItemClickListener(articleNoteDetail -> {
            Map<String,Object> map = new HashMap<>();
            map.put("articleTitle",articleNoteDetail.getArticleTitle());
            map.put("channelName",articleNoteDetail.getChannelName());
            map.put("img",articleNoteDetail.getArticleImg());
            map.put("articleId",articleNoteDetail.getArticleId());
            map.put("link",true);
            map.put("edit",true);
            navigateTo(NoteActivity.class, "map", (Serializable) map);
        });
        rv.setAdapter(articleNoteAdapter);

        getNoteList(0);
    }

    private void getNoteList(int type) {
        Call<Data<PageInfo<ArticleNoteDetail>>> articleNoteListCall = studyService.getArticleNoteList(pageNum, 8, account);
        articleNoteListCall.enqueue(new Callback<Data<PageInfo<ArticleNoteDetail>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<ArticleNoteDetail>>> call, Response<Data<PageInfo<ArticleNoteDetail>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<ArticleNoteDetail> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            articleNoteList = list;
                            articleNoteAdapter.setList(articleNoteList);
                            articleNoteAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            articleNoteList = list;
                            articleNoteAdapter.setList(articleNoteList);
                            articleNoteAdapter.notifyDataSetChanged();
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
                            articleNoteList.addAll(list);
                            articleNoteAdapter.setList(articleNoteList);
                            articleNoteAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<ArticleNoteDetail>>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.g_yellow),0);
    }
}