package com.zhaoxiao.zhiying.activity.study;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Channel;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.study.ArticleListFragment;
import com.zhaoxiao.zhiying.fragment.study.ChannelDetailFragment;
import com.zhaoxiao.zhiying.util.BitMapUtil;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;
import com.zhaoxiao.zhiying.view.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelActivity extends BaseActivity {

    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.aiv)
    AppCompatImageView aiv;
    @BindView(R.id.iv_img)
    RadiusImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_last_time)
    TextView tvLastTime;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;
    @BindView(R.id.btn_collect)
    Button btnCollect;

    private int channelId;
    private Channel channel;

    private String[] mTitles = {"详情", "目录"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private StudyService studyService;

    private String account;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int initLayout() {
        return R.layout.activity_channel;
    }

    @Override
    protected void initData() {
        account = SpUtils.getInstance(this).getString("account", "");
        channelId = (int) getIntent().getSerializableExtra("channelId");
        studyService = (StudyService) getService(StudyService.class);
        getChannel();
    }

    private void getChannel() {
        Call<Data<Channel>> channelCall = studyService.getChannelById(channelId, account);
        channelCall.enqueue(new Callback<Data<Channel>>() {
            @Override
            public void onResponse(Call<Data<Channel>> call, Response<Data<Channel>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    channel = response.body().getData();
                    afterRequest();
                } else System.out.println("请求失败");
            }

            @Override
            public void onFailure(Call<Data<Channel>> call, Throwable t) {
                System.out.println("请求未完成");
            }
        });
    }

    private void afterRequest() {
        tvName.setText(channel.getName());
        tvNum.setText("共" + NumberUtils.intChange2Str(channel.getNum()) + "篇");
        tvCollection.setText("已订阅：" + NumberUtils.intChange2Str(channel.getCollection()));
        tvLastTime.setText("更新时间：" + StringUtils.formatDate(channel.getLastTime()));
        Picasso.with(mContext)
                .load(ApiConfig.BASE_URl+channel.getImg())
                .transform(new CircleCornerTransForm())
                .into(ivImg);
//        Picasso.with(mContext)
//                .load(ApiConfig.BASE_URl+channel.getImg())
//                .transform(new CircleCornerTransForm())
//                .into(aiv);

        //毛玻璃
//        Bitmap bp = BitmapFactory.decodeResource(this.getBaseContext().getResources(), R.drawable.img_beautiful_girl);
//        Bitmap bp = BitMapUtil.getBitmap1(ApiConfig.BASE_URl+channel.getImg());
//        if (bp != null) {
//            Blurry.with(this)
//                    .radius(25)
//                    .sampling(2)
//                    .from(bp)
//                    .into(aiv);
//        }

        //异步执行
        BitMapUtil.getBitmap(ApiConfig.BASE_URl + channel.getImg(), new BitMapUtil.BitmapCallback() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                if (bitmap != null) {
//                    Blurry.with(ChannelActivity.this)
//                            .radius(25)
//                            .sampling(2)
//                            .from(bitmap)
//                            .into(aiv);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Blurry.with(ChannelActivity.this)
                                    .radius(25)
                                    .sampling(2)
                                    .from(bitmap)
                                    .into(aiv);
                        }
                    });
                }
            }

            @Override
            public void onBitmapFailed(Exception e) {

            }
        });

        //Tab导航
        mFragments.add(ChannelDetailFragment.newInstance(channel.getInfo()));
        mFragments.add(ArticleListFragment.newInstance(channel.getId()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        slidingTabLayout.setViewPager(viewPager, mTitles, this, mFragments);
//        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                setTextSize(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//
//            }
//        });
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
//        slidingTabLayout.setCurrentTab(1);
//        slidingTabLayout.setCurrentTab(0);
        setTextSize(slidingTabLayout.getCurrentTab());

        //标题栏初始化
        toolbar.setNavigationOnClickListener(v -> finish());
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);

        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠
                StatusBarUtils.setStatusBarDarkMode(this);
//                collapseLayout.setStatusBarScrimColor(0);
//                toolbar.setTitle("文章标题");
//                toolbar.setNavigationIcon(R.drawable.icon_add);
                collapseLayout.setTitle(channel.getName());
//                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.g_yellow));
//                slidingTabLayout.setIndicatorColor(getResources().getColor(R.color.white));
//                slidingTabLayout.setTextSelectColor(getResources().getColor(R.color.white));
//                slidingTabLayout.setTextUnselectColor(getResources().getColor(R.color.bright_gray));
//                setTextSize(slidingTabLayout.getCurrentTab());
            } else {
                //展开
                StatusBarUtils.setStatusBarLightMode(this);
//                collapseLayout.setStatusBarScrimColor(0);
//                toolbar.setTitle("");
//                toolbar.setNavigationIcon(R.drawable.icon_arrow_right);
                collapseLayout.setTitle("");
//                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                slidingTabLayout.setIndicatorColor(getResources().getColor(R.color.g_yellow));
//                slidingTabLayout.setTextSelectColor(getResources().getColor(R.color.g_yellow));
//                slidingTabLayout.setTextUnselectColor(getResources().getColor(R.color.gray));
//                setTextSize(slidingTabLayout.getCurrentTab());
            }
        });

        //订阅状态
        boolean collectStatus = channel.getCollectStatus();
        if (collectStatus) {
            btnCollect.setText("已订阅");
            btnCollect.setTextColor(getResources().getColor(R.color.gray));
            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
        } else {
            btnCollect.setText("订阅");
            btnCollect.setTextColor(getResources().getColor(R.color.white));
            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn));
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
//        StatusBarUtils.translucent(this, Colors.TRANSPARENT);
//        StatusBarUtils.setStatusBarLightMode(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_collect)
    public void onClick() {
        boolean collectStatus = channel.getCollectStatus();
        collect(!collectStatus);
    }

    private void collect(boolean collect) {
        Call<Data<Boolean>> channelCollectCall = studyService.channelCollect(account, channelId, collect);
        channelCollectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        channel.setCollectStatus(collect);
                        if (collect) {
                            btnCollect.setText("已订阅");
                            btnCollect.setTextColor(getResources().getColor(R.color.gray));
                            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
                            channel.setCollection(channel.getCollection()+1);
                            tvCollection.setText("已订阅：" + channel.getCollection());
//                            tvCollection.setText(String.valueOf(channel.getCollection()));
                        } else {
                            btnCollect.setText("订阅");
                            btnCollect.setTextColor(getResources().getColor(R.color.white));
                            btnCollect.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn));
                            channel.setCollection(channel.getCollection()-1);
                            tvCollection.setText("已订阅：" + channel.getCollection());
//                            tvCollection.setText(String.valueOf(channel.getCollection()));
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