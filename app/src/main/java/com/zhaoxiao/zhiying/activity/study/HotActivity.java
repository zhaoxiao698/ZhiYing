package com.zhaoxiao.zhiying.activity.study;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.study.HotAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.entity.study.PageInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;

    private Retrofit retrofit;
    private StudyService studyService;

    private List<Hot> hotList;
    private HotAdapter hotAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_hot;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getHotList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getHotList(2);
            }
        });

        tb.setLeftClickListener(v -> finish());

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        studyService = retrofit.create(StudyService.class);

        getHotList(0);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        hotAdapter = new HotAdapter(this, R.layout.item_hot_more_layout,true);
        rv.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener(hotId -> navigateTo(ChannelActivity.class,"channelId",hotId));
//        hotAdapter.setOnItemClickListener(hotId -> showToast(hotId.toString()));
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getMyBgColor(), 0);
    }

    private void getHotList(int type) {
        Call<Data<PageInfo<Hot>>> hotCall = studyService.getHotList(pageNum, 8);
        hotCall.enqueue(new Callback<Data<PageInfo<Hot>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Hot>>> call, Response<Data<PageInfo<Hot>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Hot> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            hotList = list;
                            hotAdapter.setList(hotList);
                            hotAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            hotList = list;
                            hotAdapter.setList(hotList);
                            hotAdapter.notifyDataSetChanged();
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
                            hotList.addAll(list);
                            hotAdapter.setList(hotList);
                            hotAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Hot>>> call, Throwable t) {
                System.out.println("请求未完成");
            }
        });
    }
}