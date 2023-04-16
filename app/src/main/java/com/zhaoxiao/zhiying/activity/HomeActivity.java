package com.zhaoxiao.zhiying.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.activity.test.SelectActivity;
import com.zhaoxiao.zhiying.adapter.study.MyPagerAdapter;
import com.zhaoxiao.zhiying.entity.study.TabEntity;
import com.zhaoxiao.zhiying.fragment.community.CommunityFragment;
import com.zhaoxiao.zhiying.fragment.study.StudyFragment;
import com.zhaoxiao.zhiying.fragment.mine.MineFragment;
import com.zhaoxiao.zhiying.fragment.test.SelectedFragment;
import com.zhaoxiao.zhiying.fragment.test.TestFragment;
import com.zhaoxiao.zhiying.service.MediaService;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    FixedViewPager viewPager;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;

    private String[] mTitles = {"学习", "测试", "社区", "我的"};
    private int[] mIconUnselectIds = {
            R.drawable.study_gray, R.drawable.test_gray,
            R.drawable.community_gray, R.drawable.mine_gray};
    private int[] mIconSelectIds = {
            R.drawable.study_yellow, R.drawable.test_yellow,
            R.drawable.community_yellow, R.drawable.mine_yellow};

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        //刷新过期时间
        String account = SpUtils.getInstance(this).getString("account", "");
//        if (!StringUtils.isEmpty(account) && !account.equals("已过期")) {
//            SpUtils.getInstance(this).setString("account", account, 10 * SpUtils.TIME_DAY);
//        }

        if (!StringUtils.isEmpty(account)) {
            if (account.equals("已过期")){
                SpUtils.getInstance(this).setString("account","");
                new MaterialDialog.Builder(this)
                        .title("登录状态已过期")
                        .content("登录状态已过期，请重新登录")
                        .positiveText(R.string.lab_yes)
                        .negativeText(R.string.lab_no)
                        .positiveColor(getResources().getColor(R.color.g_yellow))
                        .negativeColor(getResources().getColor(R.color.gray))
                        .onPositive((dialog, which) -> navigateTo(CodeLoginActivity.class))
                        .show();
            } else {
                SpUtils.getInstance(this).setString("account", account, 10 * SpUtils.TIME_DAY);
            }
        }

//        StatusBarUtil.setTransparent(this);
        StatusBarUtils.setStatusBarLightMode(this);

        //fragment添加到viewPage中
        mFragments.add(StudyFragment.newInstance());
        mFragments.add(TestFragment.newInstance());
        mFragments.add(CommunityFragment.newInstance());
        mFragments.add(MineFragment.newInstance());
        //添加按钮实体
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        //将实体集合绑定到组件
        commonTabLayout.setTabData(mTabEntities);
        //Tab切换点击事件
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        //预加载Fragment
        viewPager.setOffscreenPageLimit(mFragments.size());
        //页面切换，tab也切换
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
//                if(position == 1){
//                    if(getStringFromSp("questionBankId",false)==null||(Integer) getStringFromSp("questionBankId",false)==-1){
////                    showToast("请先选择题库");
//                        navigateTo(SelectActivity.class);
//                        showToast("请先选择题库");
//                    }
//                }
                if (position==0){
                    StatusBarUtil.setTransparent(HomeActivity.this);
                    StatusBarUtils.setStatusBarLightMode(HomeActivity.this);
                } else if(position == 1){
                    if(getStringFromSp("questionBankId",false)==null||(Integer) getStringFromSp("questionBankId",false)==-1){
//                    showToast("请先选择题库");
                        navigateTo(SelectActivity.class);
                        XToastUtils.info("请先选择题库");
                    } else {
                        ((SelectedFragment)mFragments.get(1).getChildFragmentManager().findFragmentById(R.id.fl_test)).startProgress();
                    }
                    StatusBarUtil.setColor(HomeActivity.this, getResources().getColor(R.color.white), 0);
                } else if(position==2||position==3){
                    StatusBarUtil.setColor(HomeActivity.this, getResources().getColor(R.color.g_yellow), 0);
                    StatusBarUtils.setStatusBarLightMode(HomeActivity.this);
                } else {
                    StatusBarUtil.setTransparent(HomeActivity.this);
                    StatusBarUtils.setStatusBarLightMode(HomeActivity.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent mediaServiceIntent = new Intent(this, MediaService.class);
        stopService(mediaServiceIntent);
    }
}