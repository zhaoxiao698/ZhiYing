package com.zhaoxiao.zhiying.activity.mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.community.TrendDetailActivity;
import com.zhaoxiao.zhiying.activity.study.ChannelActivity;
import com.zhaoxiao.zhiying.adapter.community.TrendAdapter;
import com.zhaoxiao.zhiying.adapter.study.HotAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyTrendActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;
    private UserService userService;
    private CommunityService communityService;
    private List<Trend> trendList;
    private TrendAdapter trendAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_my_trend;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getMyTrendList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getMyTrendList(2);
            }
        });

        tb.setLeftClickListener(v -> finish());

        userService = (UserService) getService(UserService.class);
        communityService = (CommunityService) getService(CommunityService.class);

        account = SpUtils.getInstance(this).getString("account","");

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        trendAdapter = new TrendAdapter(this);
        trendAdapter.setCommunityService(communityService);
        trendAdapter.setAccount(account);
        trendAdapter.setOnItemClickListener(trend -> navigateTo(TrendDetailActivity.class,"trend", trend));
        rv.setAdapter(trendAdapter);
        getMyTrendList(0);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(getMyBgColor()), 0);
    }

    private void getMyTrendList(int type) {
        Call<Data<PageInfo<Trend>>> myTrendListCall = userService.getMyTrendList(pageNum, 8, account);
        myTrendListCall.enqueue(new Callback<Data<PageInfo<Trend>>>() {
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