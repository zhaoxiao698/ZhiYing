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
import com.zhaoxiao.zhiying.activity.community.TrendDetailActivity;
import com.zhaoxiao.zhiying.adapter.community.TrendAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendListFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private String type;
    private int pageNum = 1;
    private CommunityService communityService;
    private List<Trend> trendList;
    private TrendAdapter trendAdapter;
    private LinearLayoutManager linearLayoutManager;

    private int topicId=-1;

    private String account;

    public TrendListFragment() {
    }

    public static TrendListFragment newInstance(String type) {
        TrendListFragment fragment = new TrendListFragment();
        fragment.type = type;
        return fragment;
    }

    public static TrendListFragment newInstance(String type, int topicId) {
        TrendListFragment fragment = new TrendListFragment();
        fragment.type = type;
        fragment.topicId = topicId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_trend_list;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getTrendList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getTrendList(2);
            }
        });

        communityService = (CommunityService) getService(CommunityService.class);

        account = SpUtils.getInstance(getContext()).getString("account","");

        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        trendAdapter = new TrendAdapter(getContext());
//        trendAdapter.setOnItemClickListener(trendId -> XToastUtils.toast(String.valueOf(trendId)));
        trendAdapter.setOnItemClickListener(trend -> navigateTo(TrendDetailActivity.class,"trend", trend));
        rv.setAdapter(trendAdapter);
        getTrendList(0);
    }

    private void getTrendList(int type) {
        if (!this.type.equals("收藏")) {
            Map<String, String> map = new HashMap<>();
            map.put("pageNo", String.valueOf(pageNum));
            map.put("pageSize", String.valueOf(8));
            if (this.type.equals("热门")) {
                map.put("sort", "1");
            }
            map.put("order", "false");
            if (this.type.equals("关注")) {
                map.put("fanAccount", SpUtils.getInstance(getContext()).getString("account", ""));
            }
            if (topicId != -1) {
                map.put("topicId", String.valueOf(topicId));
            }
            Call<Data<PageInfo<Trend>>> trendCall = communityService.getTrendList(map);
            trendCall.enqueue(new Callback<Data<PageInfo<Trend>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Trend>>> call, Response<Data<PageInfo<Trend>>> response) {
                    System.out.println(response);
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Trend> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                trendList = list;
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                trendList = list;
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
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
                                trendList.addAll(list);
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Trend>>> call, Throwable t) {

                }
            });
        } else {
            Call<Data<PageInfo<Trend>>> trendCollectionCall = communityService.getTrendCollectionList(pageNum, 8, account);
            trendCollectionCall.enqueue(new Callback<Data<PageInfo<Trend>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<Trend>>> call, Response<Data<PageInfo<Trend>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<Trend> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                trendList = list;
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                break;
                            case 1:
                                trendList = list;
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
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
                                trendList.addAll(list);
                                trendAdapter.setList(trendList);
                                trendAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<Trend>>> call, Throwable t) {

                }
            });
        }
    }
}