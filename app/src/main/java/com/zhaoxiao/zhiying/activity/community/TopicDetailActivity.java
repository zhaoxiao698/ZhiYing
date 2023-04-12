package com.zhaoxiao.zhiying.activity.community;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.textview.ExpandableTextView;
import com.xuexiang.xui.widget.textview.ReadMoreTextView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_desc)
    LinearLayout llDesc;
    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;
    @BindView(R.id.tv_join)
    TextView tvJoin;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_info)
    ExpandableTextView tvInfo;
    private int topicId;
    private Topic topic;
    private CommunityService communityService;

    private String[] mTitles = {"热门", "最新"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initData() {
        topicId = (int) getIntent().getSerializableExtra("topicId");
        communityService = (CommunityService) getService(CommunityService.class);
        getTopicDetail();
    }

    private void getTopicDetail() {
        Call<Data<Topic>> topicCall = communityService.getTopic(topicId);
        topicCall.enqueue(new Callback<Data<Topic>>() {
            @Override
            public void onResponse(Call<Data<Topic>> call, Response<Data<Topic>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    topic = response.body().getData();
                    afterRequest();
                }
            }

            @Override
            public void onFailure(Call<Data<Topic>> call, Throwable t) {

            }
        });
    }

    private void afterRequest() {
        tvName.setText("#"+topic.getName());
        tvJoin.setText(String.valueOf(topic.getJoin()));
        tvCollection.setText(String.valueOf(topic.getCollection()));
        if (StringUtils.isEmpty(topic.getInfo())){
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(topic.getInfo());
        }

        //Tab导航
        mFragments.add(TrendListFragment.newInstance(mTitles[0], topicId));
        mFragments.add(TrendListFragment.newInstance(mTitles[0], topicId));
        viewPager.setOffscreenPageLimit(mFragments.size());
        slidingTabLayout.setViewPager(viewPager, mTitles, this, mFragments);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextSize(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTextSize(slidingTabLayout.getCurrentTab());

        //标题栏初始化
        toolbar.setNavigationOnClickListener(v -> finish());
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);

        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠
                StatusBarUtils.setStatusBarDarkMode(this);
                collapseLayout.setTitle(topic.getName());
            } else {
                //展开
                StatusBarUtils.setStatusBarLightMode(this);
                collapseLayout.setTitle("");
            }
        });
    }

    private void setTextSize(int position) {
        for (int i = 0; i < slidingTabLayout.getTabCount(); i++) {
            TextView textView = slidingTabLayout.getTitleView(i);
            if (position == i) {
                textView.setTextSize(20);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                textView.setTextSize(17);
                textView.setTypeface(Typeface.DEFAULT);
            }
        }
    }

    @OnClick(R.id.btn_collect)
    public void onClick() {
        XToastUtils.toast("收藏话题");
    }
}