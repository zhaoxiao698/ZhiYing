package com.zhaoxiao.zhiying.activity.study;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleSearchActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.cardView)
    CardView cardView;

    private int pageNum = 1;

    private StudyService studyService;

    private List<Article> articleList;
    private ArticleAdapter articleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String searchWord;

    @Override
    protected int initLayout() {
        return R.layout.activity_article_search;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getArticleSearchList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getArticleSearchList(2);
            }
        });

        studyService = (StudyService) getService(StudyService.class);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(this);
        rv.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(articleId -> navigateTo(ArticleActivity.class, "articleId", articleId));

        EditTextUtil.showSoftInputFromWindow(this, etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchWord = etSearch.getText().toString().trim();
                    getArticleSearchList(0);
                }
                return false;
            }
        });
        cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
        cardView.setCardElevation(0);
    }

    private void getArticleSearchList(int type) {
        if (StringUtils.isEmpty(searchWord)) {
            XToastUtils.toast("请输入搜索词");
            return;
        }
//        XToastUtils.toast(searchWord);
        EditTextUtil.hideKeyboard(this);
        Call<Data<PageInfo<Article>>> articleSearchListCall = studyService.getArticleSearchList(pageNum, 8, searchWord);
        articleSearchListCall.enqueue(new Callback<Data<PageInfo<Article>>>() {
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
                            if (list.size()==0){
                                XToastUtils.toast("搜索无结果");
                            }
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
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Article>>> call, Throwable t) {
                System.out.println("t:"+t);
            }
        });
    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        finish();
    }
}