package com.zhaoxiao.zhiying.activity.community;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.SearchBarView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleSelectActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.search_bar_view)
    SearchBarView searchBarView;

    private int pageNum = 1;

    private Retrofit retrofit;
    private StudyService studyService;

    private List<Article> articleList;
    private ArticleAdapter articleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_article_select;
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

        account = SpUtils.getInstance(this).getString("account","");

        //搜索文章
        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
            @Override
            public void searchClick(View view) {
                XToastUtils.toast("搜索文章");
            }

            @Override
            public void rightTextClick(View view) {

            }

            @Override
            public void rightDrawable1Click(View view) {

            }

            @Override
            public void rightDrawable2Click(View view) {

            }
        });
        searchBarView.setOnLeftIconClick(new SearchBarView.onLeftIconClick() {
            @Override
            public void leftIconClick(View view) {
                finish();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        studyService = retrofit.create(StudyService.class);

        getArticleList(0);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(this);
        articleAdapter.setShare(true);
        rv.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListenerShare(article -> {
            Intent intent = new Intent();
            intent.putExtra("article",article);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void getArticleList(int type) {
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
    }
}