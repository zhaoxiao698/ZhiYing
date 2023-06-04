package com.zhaoxiao.zhiying.fragment.study;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_type)
    TextView tvType;

    private int channelId;

    private int pageNum = 1;

    private StudyService studyService;

    private List<Article> articleList;
    private ArticleAdapter articleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private XUIListPopup mListPopup;
    private boolean title = false;
    private boolean asc = false;

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

        if (channelId==-1||channelId==-2){
            llType.setVisibility(View.GONE);
        }

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
            Call<Data<PageInfo<Article>>> articleCall = studyService.getArticleList(pageNum, 8, channelId, title, asc);
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

    @OnClick(R.id.ll_type)
    public void onClick(View view) {
        initListPopupIfNeed();
        mListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
        mListPopup.show(view);
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "按时间降序",
                    "按时间升序",
                    "按标题降序",
                    "按标题升序",
            };

            XUISimpleAdapter adapter = XUISimpleAdapter.create(getContext(), listItems);
            mListPopup = new XUIListPopup(getContext(), adapter);
            mListPopup.create(DensityUtils.dp2px(getContext(), 100), ViewGroup.LayoutParams.WRAP_CONTENT, (adapterView, view, i, l) -> {
                switch (i) {
                    case 0:
                        title = false;
                        asc = false;
                        getArticleList(0);
                        break;
                    case 1:
                        title = false;
                        asc = true;
                        getArticleList(0);
                        break;
                    case 2:
                        title = true;
                        asc = false;
                        getArticleList(0);
                        break;
                    case 3:
                        title = true;
                        asc = true;
                        getArticleList(0);
                        break;
                }
                tvType.setText(listItems[i]);
                mListPopup.dismiss();
            });
        }
    }
}