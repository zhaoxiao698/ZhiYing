package com.zhaoxiao.zhiying.activity.mine;

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
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.fragment.study.ArticleListFragment;
import com.zhaoxiao.zhiying.fragment.test.QuestionListFragment;
import com.zhaoxiao.zhiying.fragment.word.WordListFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private String[] mTitles = {"文章","题目","单词","动态"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private WordService wordService;

    @Override
    protected int initLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        mFragments.add(ArticleListFragment.newInstance(-2));
        mFragments.add(QuestionListFragment.newInstance("历史"));
        mFragments.add(WordListFragment.newInstance("历史"));
        mFragments.add(TrendListFragment.newInstance("历史"));
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
}