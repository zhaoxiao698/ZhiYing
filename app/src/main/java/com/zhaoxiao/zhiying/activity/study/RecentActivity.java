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
import com.zhaoxiao.zhiying.adapter.study.RecentAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecentActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;

    private Retrofit retrofit;
    private StudyService studyService;

    private List<Recent> recentList;
    private RecentAdapter recentAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_recent;
    }

    @Override
    protected void initData() {
        //刷新和加载
//        srl.setOnRefreshListener(refreshLayout -> {showToast("刷新");getRecentList(1);});
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                showToast("刷新");
                pageNum=1;
                getRecentList(1);
            }
        });
//        srl.setOnRefreshListener(refreshLayout -> {pageNum++;getRecentList(2);});
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getRecentList(2);
            }
        });

        tb.setLeftClickListener(v -> finish());

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        studyService = retrofit.create(StudyService.class);

        getRecentList(0);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        recentAdapter = new RecentAdapter(this, R.layout.item_recent_more_layout);
        rv.setAdapter(recentAdapter);
        recentAdapter.setOnItemClickListener(recentId -> navigateTo(ArticleActivity.class,"articleId",recentId));
//        recentAdapter.setOnItemClickListener(recentId -> showToast(recentId.toString()));
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(getMyBgColor()), 0);
    }

    private void getRecentList(int type) {
        Call<Data<PageInfo<Recent>>> recentCall = studyService.getRecentList(pageNum, 8);
        recentCall.enqueue(new Callback<Data<PageInfo<Recent>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Recent>>> call, Response<Data<PageInfo<Recent>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Recent> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            recentList = list;
                            recentAdapter.setList(recentList);
                            recentAdapter.notifyDataSetChanged();
                            System.out.println("recent成功");
//                            ((LinearLayoutManager)rv.getLayoutManager()).scrollToPositionWithOffset(5,0);
                            break;
                        case 1:
                            recentList = list;
                            recentAdapter.setList(recentList);
                            recentAdapter.notifyDataSetChanged();
                            System.out.println("recent刷新成功");
                            srl.finishRefresh();
                            break;
                        case 2:
                            if (pageNum > response.body().getData().getPages()) {
                                pageNum = response.body().getData().getPageNum();
                                srl.finishLoadMore();
                                showToast("没有更多数据了");
//                                LinearTopSmoothScroller smoothScroller = new LinearTopSmoothScroller(mContext,true);
//                                smoothScroller.setTargetPosition(2);
//                                linearLayoutManager.startSmoothScroll(smoothScroller);

//                                final TopSmoothScroller mScroller = new TopSmoothScroller(mContext);
//                                mScroller.setTargetPosition(2);
//                                linearLayoutManager.startSmoothScroll(mScroller);

//                            linearLayoutManager.scrollToPositionWithOffset(2,200);
                                break;
                            }
                            recentList.addAll(list);
                            recentAdapter.setList(recentList);
                            recentAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            System.out.println("recent加载成功");
                            break;
                    }
                } else System.out.println("recent失败");
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Recent>>> call, Throwable t) {
                System.out.println("recent请求未完成");
            }
        });
    }
}
