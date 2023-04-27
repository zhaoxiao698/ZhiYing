package com.zhaoxiao.zhiying.fragment.study;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int channelId;

    private int pageNum = 1;

    private StudyService studyService;

    private List<Article> articleList;
    private ArticleAdapter articleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String account;


    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(int channelId) {
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.channelId = channelId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_article_list;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getArticleList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getArticleList(2);
            }
        });

        studyService = (StudyService) getService(StudyService.class);

        account = SpUtils.getInstance(getContext()).getString("account","");

        getArticleList(0);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(getContext());
        rv.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(articleId -> navigateTo(ArticleActivity.class,"articleId",articleId));
    }

    private void getArticleList(int type){
        if (channelId==-2){
            Call<Data<PageInfo<Article>>> articleCall = studyService.getArticleHistoryList(pageNum, 8, account);
            articleCall.enqueue(new Callback<Data<PageInfo<Article>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Article>>> call, Response<Data<PageInfo<Article>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Article> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishRefresh();
                                pageNum = 1;
                                break;
                            case 2:
                                if (pageNum > response.body().getData().getPages()) {
                                    pageNum = response.body().getData().getPageNum();
                                    srl.finishLoadMore();
                                    showToast("没有更多数据了");
                                    break;
                                }
                                articleList.addAll(list);
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    } else System.out.println("请求失败");
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Article>>> call, Throwable t) {

                }
            });
        } else if (channelId == -1) {
            Call<Data<PageInfo<Article>>> articleCall = studyService.getArticleCollectionList(pageNum, 8, account);
            articleCall.enqueue(new Callback<Data<PageInfo<Article>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Article>>> call, Response<Data<PageInfo<Article>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Article> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishRefresh();
                                pageNum = 1;
                                break;
                            case 2:
                                if (pageNum > response.body().getData().getPages()) {
                                    pageNum = response.body().getData().getPageNum();
                                    srl.finishLoadMore();
                                    showToast("没有更多数据了");
                                    break;
                                }
                                articleList.addAll(list);
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    } else System.out.println("请求失败");
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Article>>> call, Throwable t) {

                }
            });
        } else {
            Call<Data<PageInfo<Article>>> articleCall = studyService.getArticleList(pageNum, 8, channelId, false, false);
            articleCall.enqueue(new Callback<Data<PageInfo<Article>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Article>>> call, Response<Data<PageInfo<Article>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Article> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                articleList = list;
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishRefresh();
                                pageNum = 1;
                                break;
                            case 2:
                                if (pageNum > response.body().getData().getPages()) {
                                    pageNum = response.body().getData().getPageNum();
                                    srl.finishLoadMore();
                                    showToast("没有更多数据了");
                                    break;
                                }
                                articleList.addAll(list);
                                articleAdapter.setList(articleList);
                                articleAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    } else System.out.println("请求失败");
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Article>>> call, Throwable t) {

                }
            });
        }
    }
}