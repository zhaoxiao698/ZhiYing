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
import com.zhaoxiao.zhiying.fragment.community.TopicListFragment;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.fragment.study.ArticleListFragment;
import com.zhaoxiao.zhiying.fragment.study.SortFragment;
import com.zhaoxiao.zhiying.fragment.test.QuestionFragment;
import com.zhaoxiao.zhiying.fragment.test.QuestionListFragment;
import com.zhaoxiao.zhiying.fragment.word.SelectBookFragment;
import com.zhaoxiao.zhiying.fragment.word.WordListFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private String[] mTitles = {"频道","文章","题目","单词","动态","话题"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private WordService wordService;

    private int select = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {
        if (getIntent().getSerializableExtra("select")!=null){
            select = (int) getIntent().getSerializableExtra("select");
        }

        tb.setLeftClickListener(v -> finish());

        mFragments.add(SortFragment.newInstance(-1,0));
        mFragments.add(ArticleListFragment.newInstance(-1));
        mFragments.add(QuestionListFragment.newInstance());
        mFragments.add(WordListFragment.newInstance("收藏"));
        mFragments.add(TrendListFragment.newInstance("收藏"));
        mFragments.add(TopicListFragment.newInstance(true));
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
        slidingTabLayout.setCurrentTab(select);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getMyBgColor(),0);
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