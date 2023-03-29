package com.zhaoxiao.zhiying.activity.study;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.SortAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.Data;
import com.zhaoxiao.zhiying.entity.Ftype;
import com.zhaoxiao.zhiying.entity.Stype;
import com.zhaoxiao.zhiying.fragment.study.SortFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortActivity extends BaseActivity {

    @BindView(R.id.tb)
    TitleBar tb;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;

    private List<String> mTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private HashMap<String,Object> map;
    private int ftypeId;
    private int stypeId;

    private StudyService studyService;

    @Override
    protected int initLayout() {
        return R.layout.activity_sort;
    }

    @Override
    protected void initData() {
        tb.setLeftClickListener(v -> finish());

        map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
        ftypeId = (int) map.get("ftypeId");
        stypeId = (int) map.get("stypeId");

        studyService = (StudyService) getService(StudyService.class);
        getFtype();
    }

    private void getFtype() {
        Call<Data<Ftype>> ftypeCall = studyService.getFtypeById(ftypeId);
        ftypeCall.enqueue(new Callback<Data<Ftype>>() {
            @Override
            public void onResponse(Call<Data<Ftype>> call, Response<Data<Ftype>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Ftype ftype = response.body().getData();

                    tb.setTitle(ftype.getName());
                    mTitles.add("全部");
                    mFragments.add(SortFragment.newInstance(ftype.getId(), 0));

                    int i = 1;
                    int position=0;
                    for (Stype stype : ftype.getStypeList()) {
                        mFragments.add(SortFragment.newInstance(ftype.getId(), stype.getId()));
                        mTitles.add(stype.getName());
                        if(stype.getId()==stypeId)position=i;
                        i++;
                    }
                    viewPager.setOffscreenPageLimit(mFragments.size());
                    viewPager.setAdapter(new SortAdapter(getSupportFragmentManager(), mTitles, mFragments));
                    slidingTabLayout.setViewPager(viewPager);
                    if (position==0){
//            slidingTabLayout.setCurrentTab(1);
                        setTextSize(slidingTabLayout.getCurrentTab());
                    }
//        slidingTabLayout.setCurrentTab(2);
//        viewPager.setCurrentItem(1);
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
                    slidingTabLayout.setCurrentTab(position);
                } /*else showToast("请求类型失败");*/
                System.out.println("responseBody:"+response.body());
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Data<Ftype>> call, Throwable t) {
                showToast("请求未完成");
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
    }
}