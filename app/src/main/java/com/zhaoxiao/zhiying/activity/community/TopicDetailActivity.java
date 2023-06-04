package com.zhaoxiao.zhiying.activity.community;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.textview.ExpandableTextView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
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
    @BindView(R.id.btn_collect)
    Button btnCollect;
    private int topicId;
    private Topic topic;
    private CommunityService communityService;

    private String[] mTitles = {"热门", "最新"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initData() {
        topicId = (int) getIntent().getSerializableExtra("topicId");
        communityService = (CommunityService) getService(CommunityService.class);
        account = SpUtils.getInstance(this).getString("account", "");
        getTopicDetail();
    }

    private void getTopicDetail() {
        Call<Data<Topic>> topicCall = communityService.getTopic(topicId, account);
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
        tvName.setText("#" + topic.getName());
        tvJoin.setText(NumberUtils.intChange2Str(topic.getJoin()));
        tvCollection.setText(NumberUtils.intChange2Str(topic.getCollection()));
        if (StringUtils.isEmpty(topic.getInfo())) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(topic.getInfo());
        }

        //Tab导航
        mFragments.add(TrendListFragment.newInstance(mTitles[0], topicId));
        mFragments.add(TrendListFragment.newInstance(mTitles[1], topicId));
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
                collapseLayout.setTitle("#" + topic.getName());
            } else {
                //展开
                StatusBarUtils.setStatusBarLightMode(this);
                collapseLayout.setTitle("");
            }
        });

        //收藏状态
        boolean collectStatus = topic.getCollectStatus();
        if (collectStatus) {
            btnCollect.setText("已订阅");
            btnCollect.setTextColor(getResources().getColor(R.color.gray));
            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
        } else {
            btnCollect.setText("订阅");
            btnCollect.setTextColor(getResources().getColor(R.color.g_yellow));
            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_topic_collect_btn));
        }
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
//        XToastUtils.toast("收藏话题");
        boolean collectStatus = topic.getCollectStatus();
        collect(!collectStatus);
    }

    private void collect(boolean collect) {
        Call<Data<Boolean>> channelCollectCall = communityService.topicCollect(account, topicId, collect);
        channelCollectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        topic.setCollectStatus(collect);
                        if (collect) {
                            btnCollect.setText("已收藏");
                            btnCollect.setTextColor(getResources().getColor(R.color.gray));
                            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
                        } else {
                            btnCollect.setText("收藏话题");
                            btnCollect.setTextColor(getResources().getColor(R.color.g_yellow));
                            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_topic_collect_btn));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (collect) {
                    XToastUtils.toast("订阅失败");
                } else {
                    XToastUtils.toast("取消订阅失败");
                }
            }
        });
    }
}