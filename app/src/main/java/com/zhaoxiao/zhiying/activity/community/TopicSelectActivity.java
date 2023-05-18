package com.zhaoxiao.zhiying.activity.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
import com.zhaoxiao.zhiying.adapter.community.TopicAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.SearchBarView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicSelectActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
//    @BindView(R.id.search_bar_view)
//    SearchBarView searchBarView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.cardView)
    CardView cardView;
    private int pageNum = 1;
    private CommunityService communityService;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean isSearch = false;
    private String searchWord;

    @Override
    protected int initLayout() {
        return R.layout.activity_topic_select;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getTopicList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getTopicList(2);
            }
        });

        communityService = (CommunityService) getService(CommunityService.class);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        topicAdapter = new TopicAdapter(this);
        topicAdapter.setOnItemClickListener(new TopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Topic topic) {
                Intent intent = new Intent();
                intent.putExtra("topic",topic);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        rv.setAdapter(topicAdapter);

        getTopicList(0);

        //搜索话题
//        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
//            @Override
//            public void searchClick(View view) {
//                XToastUtils.toast("搜索话题");
//            }
//
//            @Override
//            public void rightTextClick(View view) {
//
//            }
//
//            @Override
//            public void rightDrawable1Click(View view) {
//
//            }
//
//            @Override
//            public void rightDrawable2Click(View view) {
//
//            }
//        });
//        searchBarView.setOnLeftIconClick(new SearchBarView.onLeftIconClick() {
//            @Override
//            public void leftIconClick(View view) {
//                finish();
//            }
//        });
//        EditTextUtil.showSoftInputFromWindow(this, etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchWord = etSearch.getText().toString().trim();
                    if (StringUtils.isEmpty(searchWord)){
                        isSearch = false;
                    } else {
                        isSearch = true;
                    }
                    getTopicList(0);
                }
                return false;
            }
        });
        cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
        cardView.setCardElevation(0);
    }

    private void getTopicList(int type) {
        if (isSearch){
            Call<Data<PageInfo<Topic>>> topicCall = communityService.getTopicSearchList(pageNum, 8, searchWord);
            topicCall.enqueue(new Callback<Data<PageInfo<Topic>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Topic>>> call, Response<Data<PageInfo<Topic>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Topic> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                topicList = list;
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                topicList = list;
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
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
                                topicList.addAll(list);
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Topic>>> call, Throwable t) {

                }
            });
        } else {
            Call<Data<PageInfo<Topic>>> topicCall = communityService.getTopicList(pageNum, 8);
            topicCall.enqueue(new Callback<Data<PageInfo<Topic>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Topic>>> call, Response<Data<PageInfo<Topic>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Topic> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                topicList = list;
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                topicList = list;
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
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
                                topicList.addAll(list);
                                topicAdapter.setList(topicList);
                                topicAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Topic>>> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        finish();
    }
}