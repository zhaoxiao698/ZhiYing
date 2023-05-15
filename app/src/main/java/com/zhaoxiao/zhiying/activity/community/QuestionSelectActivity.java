package com.zhaoxiao.zhiying.activity.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.test.QuestionDetailActivity;
import com.zhaoxiao.zhiying.adapter.test.QuestionAdapter;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.SearchBarView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionSelectActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.search_bar_view)
    SearchBarView searchBarView;

    private int pageNum = 1;
    private int table = 1;
    private TestService testService;
    private List<QuestionM> questionList;
    private QuestionAdapter questionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;
    private XUIListPopup mListPopup;
    
    @Override
    protected int initLayout() {
        return R.layout.activity_question_select;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getQuestionList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getQuestionList(2);
            }
        });

        testService = (TestService) getService(TestService.class);
        account = SpUtils.getInstance(this).getString("account", "");

        //搜索题目
        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
            @Override
            public void searchClick(View view) {
                XToastUtils.toast("搜索题目");
            }

            @Override
            public void rightTextClick(View view) {

            }

            @Override
            public void rightDrawable1Click(View view) {

            }

            @Override
            public void rightDrawable2Click(View view) {

            }
        });
        searchBarView.setOnLeftIconClick(new SearchBarView.onLeftIconClick() {
            @Override
            public void leftIconClick(View view) {
                finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapter(this);
        questionAdapter.setShare(true);
        rv.setAdapter(questionAdapter);
        questionAdapter.setOnItemClickListenerShare(question -> {
            Intent intent = new Intent();
            intent.putExtra("question",question);
            intent.putExtra("table",table);
            setResult(RESULT_OK, intent);
            finish();
        });
        getQuestionList(0);
    }

    private void getQuestionList(int type) {
        Call<Data<PageInfo<QuestionM>>> testCollectionCall = testService.getTestCollectionList(pageNum, 8, account, table);
        testCollectionCall.enqueue(new Callback<Data<PageInfo<QuestionM>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<QuestionM>>> call, Response<Data<PageInfo<QuestionM>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<QuestionM> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            questionList = list;
                            questionAdapter.setList(questionList);
                            questionAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            questionList = list;
                            questionAdapter.setList(questionList);
                            questionAdapter.notifyDataSetChanged();
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
                            questionList.addAll(list);
                            questionAdapter.setList(questionList);
                            questionAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<QuestionM>>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.ll_type)
    public void onClick(View view) {
        initListPopupIfNeed();
        mListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
        mListPopup.show(view);
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "听力",
                    "选词填空",
                    "匹配",
                    "仔细阅读",
                    "翻译",
                    "写作",
                    "完形填空",
                    "新题型",
            };

            XUISimpleAdapter adapter = XUISimpleAdapter.create(this, listItems);
            mListPopup = new XUIListPopup(this, adapter);
            mListPopup.create(DensityUtils.dp2px(this, 100), ViewGroup.LayoutParams.WRAP_CONTENT, (adapterView, view, i, l) -> {
                table = i + 1;
                getQuestionList(0);
                tvType.setText(listItems[i]);
                mListPopup.dismiss();
            });
        }
    }
}