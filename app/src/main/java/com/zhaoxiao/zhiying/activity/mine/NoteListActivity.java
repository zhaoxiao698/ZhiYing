package com.zhaoxiao.zhiying.activity.mine;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.adapter.mine.ArticleNoteAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.ArticleNote;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.fragment.community.TrendListFragment;
import com.zhaoxiao.zhiying.fragment.study.ArticleListFragment;
import com.zhaoxiao.zhiying.fragment.study.ArticleNoteListFragment;
import com.zhaoxiao.zhiying.fragment.test.QuestionListFragment;
import com.zhaoxiao.zhiying.fragment.test.TestNoteListFragment;
import com.zhaoxiao.zhiying.fragment.word.WordListFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends BaseActivity {
    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private String[] mTitles = {"文章","题目"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private int select = 0;
    private boolean share = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_note_list;
    }

    @Override
    protected void initData() {
        if (getIntent().getSerializableExtra("select")!=null){
            select = (int) getIntent().getSerializableExtra("select");
        }
        if (getIntent().getSerializableExtra("share")!=null){
            share = (boolean) getIntent().getSerializableExtra("share");
        }

        tb.setLeftClickListener(v -> finish());

        mFragments.add(ArticleNoteListFragment.newInstance(share));
        mFragments.add(TestNoteListFragment.newInstance(share));
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
        StatusBarUtil.setColor(this,getResources().getColor(getMyBgColor()),0);
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