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
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.adapter.mine.MessageAdapter;
import com.zhaoxiao.zhiying.adapter.study.RecentAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.Message;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private int pageNum = 1;
    private UserService userService;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getMessages(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getMessages(2);
            }
        });

        tb.setLeftClickListener(v -> finish());

        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(this).getString("account","");

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this);
        rv.setAdapter(messageAdapter);
        getMessages(0);
    }

    private void getMessages(int type) {
        Call<Data<PageInfo<Message>>> officialMessageCall = userService.getOfficialMessage(pageNum, 8, account);
        officialMessageCall.enqueue(new Callback<Data<PageInfo<Message>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Message>>> call, Response<Data<PageInfo<Message>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Message> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            messages = list;
                            messageAdapter.setList(messages);
                            messageAdapter.notifyDataSetChanged();
                            System.out.println("recent成功");
                            break;
                        case 1:
                            messages = list;
                            messageAdapter.setList(messages);
                            messageAdapter.notifyDataSetChanged();
                            System.out.println("recent刷新成功");
                            srl.finishRefresh();
                            break;
                        case 2:
                            if (pageNum > response.body().getData().getPages()) {
                                pageNum = response.body().getData().getPageNum();
                                srl.finishLoadMore();
                                showToast("没有更多数据了");
                                break;
                            }
                            messages.addAll(list);
                            messageAdapter.setList(messages);
                            messageAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            System.out.println("recent加载成功");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Message>>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.g_yellow), 0);
    }
}