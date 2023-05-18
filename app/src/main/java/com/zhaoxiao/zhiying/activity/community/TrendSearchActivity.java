package com.zhaoxiao.zhiying.activity.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.community.TrendAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendSearchActivity extends BaseActivity {
    
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.cardView)
    CardView cardView;

    private int pageNum = 1;
    private CommunityService communityService;
    private List<Trend> trendList;
    private TrendAdapter trendAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;
    private String searchWord;

    @Override
    protected int initLayout() {
        return R.layout.activity_trend_search;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getTrendSearchList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getTrendSearchList(2);
            }
        });

        communityService = (CommunityService) getService(CommunityService.class);

        account = SpUtils.getInstance(this).getString("account","");

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        trendAdapter = new TrendAdapter(this);
        trendAdapter.setCommunityService(communityService);
        trendAdapter.setAccount(account);
//        trendAdapter.setOnItemClickListener(trendId -> XToastUtils.toast(String.valueOf(trendId)));
        trendAdapter.setOnItemClickListener(trend -> navigateTo(TrendDetailActivity.class,"trend", trend));
        rv.setAdapter(trendAdapter);

        EditTextUtil.showSoftInputFromWindow(this, etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchWord = etSearch.getText().toString().trim();
                    getTrendSearchList(0);
                }
                return false;
            }
        });
        cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
        cardView.setCardElevation(0);
    }
    
    private void getTrendSearchList(int type){
        if (StringUtils.isEmpty(searchWord)) {
            XToastUtils.toast("请输入搜索词");
            return;
        }
//        XToastUtils.toast(searchWord);
        EditTextUtil.hideKeyboard(this);
        Call<Data<PageInfo<Trend>>> trendCollectionCall = communityService.getTrendSearchList(pageNum, 8, searchWord,account);
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

    @OnClick(R.id.iv_search)
    public void onClick() {
        finish();
    }
}