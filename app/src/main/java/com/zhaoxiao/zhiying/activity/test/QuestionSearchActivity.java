package com.zhaoxiao.zhiying.activity.test;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.adapter.test.QuestionAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

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

public class QuestionSearchActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.tv_type)
    TextView tvType;

    private int pageNum = 1;
    private int table = 1;
    private TestService testService;
    private List<QuestionM> questionList;
    private QuestionAdapter questionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;
    private XUIListPopup mListPopup;

    private String searchWord;

    @Override
    protected int initLayout() {
        return R.layout.activity_question_search;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getQuestionSearchList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getQuestionSearchList(2);
            }
        });
        
        testService = (TestService) getService(TestService.class);
        account = SpUtils.getInstance(this).getString("account", "");

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapter(this);
        rv.setAdapter(questionAdapter);
        questionAdapter.setOnItemClickListener(questionId -> {
            Map<String,Integer> map = new HashMap<>();
            map.put("questionId",questionId);
            map.put("table",table);
            navigateTo(QuestionDetailActivity.class,"map", (Serializable) map);
        });

        EditTextUtil.showSoftInputFromWindow(this, etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchWord = etSearch.getText().toString().trim();
                    getQuestionSearchList(0);
                }
                return false;
            }
        });
        cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
        cardView.setCardElevation(0);
    }

    private void getQuestionSearchList(int type) {
        if (StringUtils.isEmpty(searchWord)) {
            XToastUtils.toast("请输入搜索词");
            return;
        }
//        XToastUtils.toast(searchWord);
        EditTextUtil.hideKeyboard(this);
        Call<Data<PageInfo<QuestionM>>> testCollectionCall = testService.getQuestionSearchList(pageNum, 8, account, table, searchWord);
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
                            if (list.size()==0){
                                XToastUtils.toast("搜索无结果");
                            }
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

    @OnClick({R.id.iv_search,R.id.ll_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                finish();
                break;
            case R.id.ll_type:
                initListPopupIfNeed();
                mListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
                mListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                mListPopup.show(view);
                break;
        }
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
//                getQuestionSearchList(0);
                tvType.setText(listItems[i]);
                mListPopup.dismiss();
            });
        }
    }
}