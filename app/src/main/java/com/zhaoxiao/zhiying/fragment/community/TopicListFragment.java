package com.zhaoxiao.zhiying.fragment.community;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.community.TopicDetailActivity;
import com.zhaoxiao.zhiying.adapter.community.TopicAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
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

public class TopicListFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private int pageNum = 1;
    private CommunityService communityService;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean isCollect = false;
    private String account;

    public TopicListFragment() {
    }

    public static TopicListFragment newInstance() {
        return new TopicListFragment();
    }

    public static TopicListFragment newInstance(boolean isCollect) {
        TopicListFragment fragment = new TopicListFragment();
        fragment.isCollect = isCollect;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_topic;
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

        account = SpUtils.getInstance(getContext()).getString("account","");

        communityService = (CommunityService) getService(CommunityService.class);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        topicAdapter = new TopicAdapter(getContext());
//        topicAdapter.setOnItemClickListener(topicId -> navigateTo(TopicDetailActivity.class, "topicId", topicId));
        topicAdapter.setOnItemClickListener(topic -> navigateTo(TopicDetailActivity.class,"topicId",topic.getId()));
        rv.setAdapter(topicAdapter);

        getTopicList(0);
    }

    private void getTopicList(int type) {
        if (!isCollect) {
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
        } else {
            Call<Data<PageInfo<Topic>>> topicCall = communityService.getTopicCollectionList(pageNum, 8,account);
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
}