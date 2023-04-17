package com.zhaoxiao.zhiying.activity.word;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.word.WordListAdapter;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordListActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private int pageNum = 1;
    private WordService wordService;
    private List<WordSimple> wordSimpleList;
    private WordListAdapter wordListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String bookId;
    private String account;
    private String bookName;
    private List<WordSimple> preList;

    @Override
    protected int initLayout() {
        return R.layout.activity_word_list;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getList(2);
            }
        });

//        bookId = (String) getIntent().getSerializableExtra("bookId");
        bookName = (String) getIntent().getSerializableExtra("bookName");
                bookId = getStringFromSp("word_bookId",true);
        account = SpUtils.getInstance(this).getString("account","");

        if (bookName.equals("收藏")) {
            tvTopTitle.setText("收藏");
        } else {
            tvTopTitle.setText(bookName);
        }

        wordService = (WordService) getService(WordService.class);

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        wordListAdapter = new WordListAdapter(this);
        rv.setAdapter(wordListAdapter);
        wordListAdapter.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String wordId) {

            }

//            @Override
//            public void onIconClick(WordSimple wordSimple, ImageView ivCollection) {
//                boolean isCollected = wordSimple.getCollect();
//                collect(wordSimple, !isCollected, ivCollection);
//            }

            @Override
            public void onIconClick(WordSimple wordSimple) {
                navigateTo(WordDetailActivity.class, "wordSimple", wordSimple);
            }
        });
        getList(0);
    }

    private void getList(int type) {
        if (bookName.equals("收藏")){
            Call<Data<PageInfo<WordSimple>>> collectionListCall = wordService.getCollectionList(pageNum, 16, account,bookId);
            collectionListCall.enqueue(new Callback<Data<PageInfo<WordSimple>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<WordSimple>>> call, Response<Data<PageInfo<WordSimple>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<WordSimple> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                wordSimpleList = list;
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                preList = list;
                                break;
                            case 1:
                                wordSimpleList = list;
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                srl.finishRefresh();
                                pageNum = 1;
                                preList = list;
                                break;
                            case 2:
//                                if (pageNum > response.body().getData().getPages()) {
//                                    pageNum = response.body().getData().getPageNum();
//                                    srl.finishLoadMore();
//                                    showToast("没有更多数据了");
//                                    break;
//                                }
                                if (list==null||list.size()==0||list.get(0).getWordId().equals(preList.get(0).getWordId())){
//                                    if (list.equals(preList)){
                                    pageNum --;
                                    srl.finishLoadMore();
                                    showToast("没有更多数据了");
                                    break;
                                }
                                wordSimpleList.addAll(list);
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                preList = list;
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<WordSimple>>> call, Throwable t) {

                }
            });
        } else {
            Call<Data<PageInfo<WordSimple>>> wordSimplePageInfoCall = wordService.getWordSimplePageInfo(pageNum, 16, account, bookId);
            wordSimplePageInfoCall.enqueue(new Callback<Data<PageInfo<WordSimple>>>() {
                @Override
                public void onResponse(Call<Data<PageInfo<WordSimple>>> call, Response<Data<PageInfo<WordSimple>>> response) {
                    if (response.body() != null && response.body().getCode() == 10000) {
                        List<WordSimple> list = response.body().getData().getList();
                        switch (type) {
                            case 0:
                                wordSimpleList = list;
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                pageNum = 1;
                                preList = list;
                                break;
                            case 1:
                                wordSimpleList = list;
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                srl.finishRefresh();
                                pageNum = 1;
                                preList = list;
                                break;
                            case 2:
//                                if (pageNum > response.body().getData().getPages()) {
//                                    pageNum = response.body().getData().getPageNum();
//                                    srl.finishLoadMore();
//                                    showToast("没有更多数据了");
//                                    break;
//                                }
                                if (list==null||list.size()==0||list.get(0).getWordId().equals(preList.get(0).getWordId())){
//                                    if (list.equals(preList)){
                                    pageNum --;
                                    srl.finishLoadMore();
                                    showToast("没有更多数据了");
                                    break;
                                }
                                wordSimpleList.addAll(list);
                                wordListAdapter.setList(wordSimpleList);
                                wordListAdapter.notifyDataSetChanged();
                                srl.finishLoadMore();
                                preList = list;
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<PageInfo<WordSimple>>> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    private void collect(WordSimple wordSimple,boolean collect, ImageView ivCollection) {
        Call<Data<Boolean>> collectCall = wordService.collect(account, wordSimple.getWordId(), bookId, collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        wordSimple.setCollect(collect);
                        if (collect){
                            ivCollection.setImageResource(R.drawable.star1_community);
                        } else {
                            ivCollection.setImageResource(R.drawable.star_community);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}