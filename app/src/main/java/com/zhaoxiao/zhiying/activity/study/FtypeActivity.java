package com.zhaoxiao.zhiying.activity.study;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.study.FtypeAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Ftype;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FtypeActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private Retrofit retrofit;
    private StudyService studyService;

    private List<Ftype> ftypeList;
    private FtypeAdapter ftypeAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_ftype;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getFtypeList();
            }
        });

        tb.setLeftClickListener(v -> finish());

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        studyService = retrofit.create(StudyService.class);

        getFtypeList();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ftypeAdapter = new FtypeAdapter(this);
        rv.setAdapter(ftypeAdapter);
        ftypeAdapter.setOnItemClickListener(new FtypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int ftypeId) {
//                showToast(ftypeId.toString());
                HashMap<String,Object> map = new HashMap<>();
                map.put("ftypeId",ftypeId);
                map.put("stypeId",0);
                navigateTo(SortActivity.class,"map", map);
            }

            @Override
            public void onStypeClick(int ftypeId,int stypeId) {
//                showToast(stypeId.toString());
                HashMap<String,Object> map = new HashMap<>();
                map.put("ftypeId",ftypeId);
                map.put("stypeId",stypeId);
                navigateTo(SortActivity.class,"map", map);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.g_yellow), 0);
    }

    private void getFtypeList() {
        Call<Data<List<Ftype>>> ftypeCall = studyService.getFtypeList();
        ftypeCall.enqueue(new Callback<Data<List<Ftype>>>() {
            @Override
            public void onResponse(Call<Data<List<Ftype>>> call, Response<Data<List<Ftype>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Ftype> list = response.body().getData();
                    ftypeAdapter.setList(list);
                    ftypeAdapter.notifyDataSetChanged();
                    srl.finishRefresh();
//                    srl.finishLoadMore();
//                    showToast("请求类型成功");
                } /*else showToast("请求类型失败");*/
                System.out.println("responseBody:"+response.body());
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Data<List<Ftype>>> call, Throwable t) {
                showToast("请求未完成");
            }
        });
    }
}