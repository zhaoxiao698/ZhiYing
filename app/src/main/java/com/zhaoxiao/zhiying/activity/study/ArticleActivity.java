package com.zhaoxiao.zhiying.activity.study;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.adapter.study.MyPagerAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;
import com.zhaoxiao.zhiying.fragment.study.ReadFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.segmentTabLayout)
    SegmentTabLayout segmentTabLayout;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private int articleId;

    private String[] mTitles = {"听力", "阅读"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private StudyService studyService;

    private int position;

    private String account;

    private long startTime;
    private long endTime;
    private long duration;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_article;
    }

    @Override
    protected void initData() {
        articleId = (int) getIntent().getSerializableExtra("articleId");

        //添加学习记录
        account = SpUtils.getInstance(this).getString("account", "");
        if (!account.equals("") && !account.equals("已过期")) {
            studyService = (StudyService) getService(StudyService.class);
            addArticleRecord(account, articleId);
            userService = (UserService) getService(UserService.class);
        }

        ivBack.setOnClickListener(v -> finish());

        mFragments.add(ListenFragment.newInstance(articleId));
        mFragments.add(ReadFragment.newInstance(articleId));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
        viewPager.setScrollable(false);
        segmentTabLayout.setTabData(mTitles);
        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                showToast(mTitles[position]);
                viewPager.setCurrentItem(position);
                if (position == 1) {
                    ((ListenFragment) mFragments.get(0)).readPause();
                }
                ArticleActivity.this.position = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                segmentTabLayout.setCurrentTab(position);
                ArticleActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addArticleRecord(String account, int articleId) {
        Call<Data<Boolean>> addArticleRecordCall = studyService.addArticleRecord(account, articleId);
        addArticleRecordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        System.out.println("添加学习记录成功");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(this);
    }

    @Override
    public void onBackPressed() {
        if (!((ListenFragment) mFragments.get(0)).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.iv_more)
    public void onClick() {
        showSimpleBottomSheetList();
    }

    //更多弹窗
    private void showSimpleBottomSheetList() {
//        Fragment fragment = mFragments.get(position);
//        boolean isCollect = false;
//        if (position==0){
//            isCollect = ((ListenFragment) fragment).getCollect();
//        } else if (position==1){
//            isCollect = ((ReadFragment) fragment).getCollect();
//        }
        ListenFragment listenFragment = (ListenFragment) mFragments.get(0);
        boolean isCollect = listenFragment.getCollect();
        int channelId = listenFragment.getChannelId();
        Map<String,Object> map = listenFragment.getMap();

        BottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new BottomSheet.BottomListSheetBuilder(this);
        bottomListSheetBuilder
                .setTitle("更多")
                .addItem("笔记")
                .addItem("查看频道");
        if (isCollect){
            bottomListSheetBuilder
                    .addItem("取消收藏");
        } else {
            bottomListSheetBuilder
                    .addItem("收藏");
        }
        bottomListSheetBuilder
                .addItem("分享")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position){
                        case 0:
//                            XToastUtils.toast("笔记");
                            if (!account.equals("") && !account.equals("已过期")) {
                                map.put("link",false);
                                map.put("edit",true);
                                navigateTo(NoteActivity.class, "map", (Serializable) map);
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                        case 1:
                            navigateTo(ChannelActivity.class,"channelId",channelId);
                            break;
                        case 2:
                            if (!account.equals("") && !account.equals("已过期")) {
                                collect(!isCollect, listenFragment);
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                        case 3:
                            if (!account.equals("") && !account.equals("已过期")) {
                                XToastUtils.toast("分享");
                            } else {
                                navigateTo(CodeLoginActivity.class);
                            }
                            break;
                    }
                })
                .build()
                .show();
    }

    private void collect(boolean collect/*, Fragment fragment*/,ListenFragment listenFragment) {
        Call<Data<Boolean>> collectCall = studyService.collect(account, articleId, collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
//                        if (position==0){
//                            ((ListenFragment) fragment).setCollect(collect);
//                        } else if (position==1){
//                            ((ReadFragment) fragment).setCollect(collect);
//                        }
                        listenFragment.setCollect(collect);
                        if (collect){
                            XToastUtils.toast("收藏成功");
                        } else {
                            XToastUtils.toast("取消收藏成功");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (collect) {
                    XToastUtils.toast("收藏失败");
                } else {
                    XToastUtils.toast("取消收藏失败");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        if (!account.equals("") && !account.equals("已过期")) {
            addPlanDo();
        }
    }

    private void addPlanDo(){
        Call<Data<Boolean>> addPlanDoCall = userService.addPlanDo(account, duration);
        addPlanDoCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        System.out.println("添加学习记录："+duration+"ms");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}