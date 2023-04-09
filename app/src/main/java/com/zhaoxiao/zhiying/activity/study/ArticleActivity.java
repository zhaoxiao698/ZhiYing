package com.zhaoxiao.zhiying.activity.study;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.study.MyPagerAdapter;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;
import com.zhaoxiao.zhiying.fragment.study.ReadFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;

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

    @Override
    protected int initLayout() {
        return R.layout.activity_article;
    }

    @Override
    protected void initData() {
        articleId = (int) getIntent().getSerializableExtra("articleId");

        ivBack.setOnClickListener(v -> finish());

        mFragments.add(ListenFragment.newInstance(articleId));
        mFragments.add(ReadFragment.newInstance(articleId));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mTitles,mFragments));
        viewPager.setScrollable(false);
        segmentTabLayout.setTabData(mTitles);
        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                showToast(mTitles[position]);
                viewPager.setCurrentItem(position);
                if(position==1){
                    ((ListenFragment)mFragments.get(0)).readPause();
                }
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(this);
    }
}