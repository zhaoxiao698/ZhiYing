package com.zhaoxiao.zhiying.activity.word;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.fragment.word.SelectBookFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectBookActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private String[] mTitles = {"全部","四级","六级","考研","英专","其他"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private WordService wordService;

    @Override
    protected int initLayout() {
        return R.layout.activity_select_book;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        for (String mTitle : mTitles) {
            mFragments.add(SelectBookFragment.newInstance(mTitle));
        }
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
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.g_yellow),0);
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

    @Override
    public void onBackPressed() {
        String bookId = getStringFromSp("word_bookId",true);
        if (StringUtils.isEmpty(bookId)){
            navigateTo(HomeActivity.class);
        }else {
            super.onBackPressed();
        }
    }
}