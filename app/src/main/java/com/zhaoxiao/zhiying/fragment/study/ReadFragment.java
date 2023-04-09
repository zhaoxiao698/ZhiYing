package com.zhaoxiao.zhiying.fragment.study;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.ParagraphAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.ArticleDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Sentence;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_add_time)
    TextView tvAddTime;
    @BindView(R.id.rv)
    RecyclerView rv;
//    @BindView(R.id.srl)
//    SmartRefreshLayout srl;

    private int articleId;

    private StudyService studyService;

    private ArticleDetail articleDetail;
    private List<String> paragraphList = new ArrayList<>();
    private ParagraphAdapter paragraphAdapter;
    private LinearLayoutManager linearLayoutManager;

    public ReadFragment() {
    }

    public static ReadFragment newInstance(int articleId) {
        ReadFragment fragment = new ReadFragment();
        fragment.articleId = articleId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initData() {
        //刷新和加载
//        srl.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                getArticleDetail();
//            }
//        });

        studyService = (StudyService) getService(StudyService.class);

        getArticleDetail();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        paragraphAdapter = new ParagraphAdapter(getContext());
        rv.setAdapter(paragraphAdapter);
        paragraphAdapter.setOnItemClickListener(paragraphId -> showToast("段落"+(paragraphId+1)));

        rv.setNestedScrollingEnabled(false);
        rv.setFocusable(false);
    }

    private void getArticleDetail() {
        Call<Data<ArticleDetail>> articleDetailCall = studyService.getArticleDetail(articleId);
        articleDetailCall.enqueue(new Callback<Data<ArticleDetail>>() {
            @Override
            public void onResponse(Call<Data<ArticleDetail>> call, Response<Data<ArticleDetail>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    articleDetail = response.body().getData();
                    for (Sentence sentence : articleDetail.getSentenceList()) {
                        if (sentence.getFirst() == 1||paragraphList.size()==0) {
                            String paragraph = sentence.getEnglish();
                            paragraphList.add(paragraph);
                        } else {
                            paragraphList.set(paragraphList.size()-1,paragraphList.get(paragraphList.size()-1)+sentence.getEnglish());
                        }
                    }
                    tvTitle.setText(articleDetail.getTitle());
                    tvAddTime.setText(StringUtils.formatDate2English(articleDetail.getAddTime()));
                    paragraphAdapter.setList(paragraphList);
                    paragraphAdapter.notifyDataSetChanged();
//                    srl.finishRefresh();
                }
            }

            @Override
            public void onFailure(Call<Data<ArticleDetail>> call, Throwable t) {

            }
        });
    }
}