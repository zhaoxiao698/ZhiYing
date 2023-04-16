package com.zhaoxiao.zhiying.fragment.word;

import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.word.SetPlanActivity;
import com.zhaoxiao.zhiying.adapter.word.BookAdapter;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.word.Book;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectBookFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private String type;
    private WordService wordService;
    private int pageNum = 1;
    private List<Book> bookList;
    private BookAdapter bookAdapter;
    private LinearLayoutManager linearLayoutManager;

    public SelectBookFragment() {
    }

    public static SelectBookFragment newInstance(String type) {
        SelectBookFragment fragment = new SelectBookFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_select_book;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getBookList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getBookList(2);
            }
        });

        wordService = (WordService) getService(WordService.class);
        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        bookAdapter = new BookAdapter(getContext());
        bookAdapter.setOnItemClickListener(bookId -> {
            saveStringToSp("word_bookId",bookId);
            if (getStringFromSp("word_group", false) == null || (int) getStringFromSp("word_group", false) == -1) {
                saveStringToSp("word_group",10);
            }
            if (getStringFromSp("word_plan", false) == null || (int) getStringFromSp("word_plan", false) == -1) {
                saveStringToSp("word_plan",0);
            }
            navigateTo(SetPlanActivity.class);
        });
        rv.setAdapter(bookAdapter);
        getBookList(0);
    }

    private void getBookList(int type) {
        Call<Data<PageInfo<Book>>> bookCall = wordService.getBookList(pageNum, 8, SelectBookFragment.this.type);
        bookCall.enqueue(new Callback<Data<PageInfo<Book>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Book>>> call, Response<Data<PageInfo<Book>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Book> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            bookList = list;
                            bookAdapter.setList(bookList);
                            bookAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            bookList = list;
                            bookAdapter.setList(bookList);
                            bookAdapter.notifyDataSetChanged();
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
                            bookList.addAll(list);
                            bookAdapter.setList(bookList);
                            bookAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Book>>> call, Throwable t) {

            }
        });
    }
}