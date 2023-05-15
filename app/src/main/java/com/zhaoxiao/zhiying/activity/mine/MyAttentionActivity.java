package com.zhaoxiao.zhiying.activity.mine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.zhaoxiao.zhiying.adapter.mine.UserAdapter;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAttentionActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;
    private UserService userService;
    private List<User> userList;
    private UserAdapter userAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;

    public MyAttentionActivity() {
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_my_attention;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getMyAttentionList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getMyAttentionList(2);
            }
        });

        tb.setLeftClickListener(v -> finish());

        userService = (UserService) getService(UserService.class);

        account = SpUtils.getInstance(this).getString("account","");

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        userAdapter = new UserAdapter(this);
        userAdapter.setOnItemClickListener(trend -> navigateTo(TrendDetailActivity.class,"trend", trend));
        rv.setAdapter(userAdapter);
        getMyAttentionList(0);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.g_yellow), 0);
    }

    private void getMyAttentionList(int type) {
        Call<Data<PageInfo<User>>> myAttentionListCall = userService.getMyAttentionList(pageNum, 8, account);
        myAttentionListCall.enqueue(new Callback<Data<PageInfo<User>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<User>>> call, Response<Data<PageInfo<User>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<User> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            userList = list;
                            userAdapter.setList(userList);
                            userAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            userList = list;
                            userAdapter.setList(userList);
                            userAdapter.notifyDataSetChanged();
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
                            userList.addAll(list);
                            userAdapter.setList(userList);
                            userAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<User>>> call, Throwable t) {

            }
        });
    }
}