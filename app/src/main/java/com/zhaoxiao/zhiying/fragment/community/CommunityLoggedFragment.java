package com.zhaoxiao.zhiying.fragment.community;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.MyPagerAdapter;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;

public class CommunityLoggedFragment extends BaseFragment {

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private String[] mTitles = {"关注", "最新", "热门", "话题"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public CommunityLoggedFragment() {
    }

    public static CommunityLoggedFragment newInstance() {
        return new CommunityLoggedFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_community_logged;
    }

    @Override
    protected void initData() {
        //Tab导航
        mFragments.add(TrendListFragment.newInstance(mTitles[0]));
        mFragments.add(TrendListFragment.newInstance(mTitles[1]));
        mFragments.add(TrendListFragment.newInstance(mTitles[2]));
        mFragments.add(TopicListFragment.newInstance());
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
//        slidingTabLayout.setViewPager(viewPager, mTitles, getActivity(), mFragments);
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
//        setTextSize(slidingTabLayout.getCurrentTab());
        slidingTabLayout.setCurrentTab(1);
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
}