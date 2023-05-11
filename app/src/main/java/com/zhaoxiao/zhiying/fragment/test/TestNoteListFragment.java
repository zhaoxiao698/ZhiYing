package com.zhaoxiao.zhiying.fragment.test;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.activity.test.TestNoteActivity;
import com.zhaoxiao.zhiying.adapter.mine.TestNoteAdapter;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.test.TestNoteDetail;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
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

public class TestNoteListFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_type)
    TextView tvType;
    private int pageNum = 1;
    private int table = 1;
    private TestService testService;
    private List<TestNoteDetail> testNoteDetailList;
    private TestNoteAdapter testNoteAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String account;
    private XUIListPopup mListPopup;

    public TestNoteListFragment() {
    }

    public static TestNoteListFragment newInstance() {
        return new TestNoteListFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_test_note_list;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                getNoteList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getNoteList(2);
            }
        });

        testService = (TestService) getService(TestService.class);

        account = SpUtils.getInstance(getContext()).getString("account","");

        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        testNoteAdapter = new TestNoteAdapter(getContext());
        testNoteAdapter.setOnItemClickListener(testNoteDetail -> {
            Map<String,Object> map = new HashMap<>();
            map.put("questionInfo",testNoteDetail.getQuestionInfo());
            map.put("subType",testNoteDetail.getSubType());
            map.put("questionId",testNoteDetail.getQuestionId());
            map.put("table",table);
            map.put("link",true);
            map.put("edit",true);
            navigateTo(TestNoteActivity.class, "map", (Serializable) map);
        });
        rv.setAdapter(testNoteAdapter);

        getNoteList(0);
    }

    private void getNoteList(int type) {
        Call<Data<PageInfo<TestNoteDetail>>> testNoteListCall = testService.getTestNoteList(pageNum, 8, account, table);
        testNoteListCall.enqueue(new Callback<Data<PageInfo<TestNoteDetail>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<TestNoteDetail>>> call, Response<Data<PageInfo<TestNoteDetail>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<TestNoteDetail> list = response.body().getData().getList();
                    switch (type) {
                        case 0:
                            testNoteDetailList = list;
                            testNoteAdapter.setList(testNoteDetailList);
                            testNoteAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            testNoteDetailList = list;
                            testNoteAdapter.setList(testNoteDetailList);
                            testNoteAdapter.notifyDataSetChanged();
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
                            testNoteDetailList.addAll(list);
                            testNoteAdapter.setList(testNoteDetailList);
                            testNoteAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<TestNoteDetail>>> call, Throwable t) {

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

            XUISimpleAdapter adapter = XUISimpleAdapter.create(getContext(), listItems);
            mListPopup = new XUIListPopup(getContext(), adapter);
            mListPopup.create(DensityUtils.dp2px(getContext(), 100), ViewGroup.LayoutParams.WRAP_CONTENT, (adapterView, view, i, l) -> {
                table = i + 1;
                getNoteList(0);
                tvType.setText(listItems[i]);
                mListPopup.dismiss();
            });
        }
    }
}