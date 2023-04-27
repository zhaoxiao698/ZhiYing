package com.zhaoxiao.zhiying.activity.community;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.flexbox.FlexboxLayout;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.community.CommentAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.community.Comment;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.fragment.community.CommentFragment;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendDetailActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    RadiusImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_add_time)
    TextView tvAddTime;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.ngl_images)
    NineGridImageView<ImageViewInfo> nglImages;
    @BindView(R.id.fl_topic)
    FlexboxLayout flTopic;
    @BindView(R.id.tv_comment_num1)
    TextView tvCommentNum1;
    @BindView(R.id.stl)
    SegmentTabLayout stl;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private Trend trend;
    private String[] mTitles = {"最新", "热门"};
    private FragmentManager manager;
    CommentFragment commentFragment;

    private int pageNum = 1;
    private CommunityService communityService;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int commentSortType = 0;
    private int refreshFlag;

    private String account;

    @Override
    protected int initLayout() {
        return R.layout.activity_trend_detail;
    }

    @Override
    protected void initData() {
        trend = (Trend) getIntent().getSerializableExtra("trend");

        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum=1;
                refreshFlag=0;
                getTrendDetail(1);
                getCommentList(1);
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum++;
                getCommentList(2);
            }
        });

        //detail
        setImagesAdapter();
        setDetail();

        //comment
        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(this);
        rv.setAdapter(commentAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setFocusable(false);

        communityService = (CommunityService) getService(CommunityService.class);
        getCommentList(0);
//        manager = getSupportFragmentManager();
//
//        FragmentTransaction transaction = manager.beginTransaction();
//        commentFragment = CommentFragment.newInstance(trend.getId(),0);
//        transaction.add(R.id.fl_comment,commentFragment).commit();

        stl.setTabData(mTitles);
        stl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                switch (position) {
//                    case 0:
////                        FragmentTransaction transaction0 = manager.beginTransaction();
////                        commentFragment = CommentFragment.newInstance(trend.getId(),0);
////                        transaction0.replace(R.id.fl_comment,commentFragment).commit();
//                        commentSortType = 0;
//                        getCommentList(0);
//                        break;
//                    case 1:
////                        FragmentTransaction transaction1 = manager.beginTransaction();
////                        commentFragment = CommentFragment.newInstance(trend.getId(),1);
////                        transaction1.replace(R.id.fl_comment,commentFragment).commit();
//                        commentSortType = 1;
//                        getCommentList(0);
//                        break;
//                }
                commentSortType = position;
                getCommentList(0);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        //添加动态记录
        account = SpUtils.getInstance(this).getString("account", "");
        if (!account.equals("") && !account.equals("已过期")) {
            addTrendRecord(account, trend.getId());
        }
    }

    private void addTrendRecord(String account, int id) {
        Call<Data<Boolean>> addTrendRecordCall = communityService.addTrendRecord(account, id);
        addTrendRecordCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        System.out.println("添加动态记录成功");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    private void setDetail() {
        tvName.setText(trend.getUserName());
        tvAddTime.setText(StringUtils.formatDate(trend.getAddTime()));
        if (StringUtils.isEmpty(trend.getTitle())) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(trend.getTitle());
        }
        if (StringUtils.isEmpty(trend.getInfo())) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(trend.getInfo());
        }

        //话题
        flTopic.removeAllViews();
        for (Topic topic : trend.getTopicList()) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_topic_item, null);
            RoundButton child = viewGroup.findViewById(R.id.tv_option);
            viewGroup.removeView(child);
            child.setText("#" + topic.getName());
            child.setOnClickListener(view -> XToastUtils.info(topic.getName()));
            flTopic.addView(child);
        }

        //操作条
        tvLike.setText(String.valueOf(trend.getLike()));
        tvComment.setText(String.valueOf(trend.getComment()));
        tvCommentNum1.setText("（" + trend.getComment() + "条）");
        tvShare.setText(String.valueOf(trend.getShare()));
        tvCollection.setText(String.valueOf(trend.getCollection()));

        //图片
        nglImages.setImagesData(trend.getImgList(), 0);

        Picasso.with(mContext)
                .load(trend.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(ivAvatar);
    }

    private void getCommentList(int type) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(8));
        map.put("sort",String.valueOf(commentSortType));
        map.put("order","false");
        map.put("trendId",String.valueOf(trend.getId()));
        Call<Data<PageInfo<Comment>>> commentCall = communityService.getCommentList(map);
        commentCall.enqueue(new Callback<Data<PageInfo<Comment>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Comment>>> call, Response<Data<PageInfo<Comment>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Comment> list = response.body().getData().getList();
                    switch (type){
                        case 0:
                            commentList = list;
                            commentAdapter.setList(commentList);
                            commentAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            break;
                        case 1:
                            commentList = list;
                            commentAdapter.setList(commentList);
                            commentAdapter.notifyDataSetChanged();
                            pageNum = 1;
                            if(++refreshFlag==2) srl.finishRefresh();
                            break;
                        case 2:
                            if (pageNum > response.body().getData().getPages()) {
                                pageNum = response.body().getData().getPageNum();
                                srl.finishLoadMore();
                                XToastUtils.toast("没有更多数据了");
                                break;
                            }
                            commentList.addAll(list);
                            commentAdapter.setList(commentList);
                            commentAdapter.notifyDataSetChanged();
                            srl.finishLoadMore();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Comment>>> call, Throwable t) {

            }
        });
    }

    private void getTrendDetail(int type) {
        Call<Data<Trend>> trendCall = communityService.getTrend(trend.getId());
        trendCall.enqueue(new Callback<Data<Trend>>() {
            @Override
            public void onResponse(Call<Data<Trend>> call, Response<Data<Trend>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    switch (type){
                        case 0:
                            trend = response.body().getData();
                            setDetail();
                            break;
                        case 1:
                            trend = response.body().getData();
                            setDetail();
                            if(++refreshFlag==2) srl.finishRefresh();
                            break;
                        case 2:
                            //detail没有加载
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Trend>> call, Throwable t) {

            }
        });
    }

    private void setImagesAdapter() {
        /**
         * 图片加载
         *
         * @param context
         * @param imageView
         * @param imageViewInfo 图片信息
         */
        NineGridImageViewAdapter<ImageViewInfo> mAdapter = new NineGridImageViewAdapter<ImageViewInfo>() {
            /**
             * 图片加载
             *
             * @param context
             * @param imageView
             * @param imageViewInfo 图片信息
             */
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, ImageViewInfo imageViewInfo) {
                Picasso.with(mContext)
                        .load(imageViewInfo.getUrl())
                        .transform(new CircleCornerTransForm())
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };
        nglImages.setAdapter(mAdapter);
        nglImages.setItemImageClickListener(new ItemImageClickListener<ImageViewInfo>() {
            @SingleClick
            @Override
            public void onItemImageClick(ImageView imageView, int index, List<ImageViewInfo> list) {
                computeBoundsBackward(list);//组成数据
                PreviewBuilder.from((Activity) imageView.getContext())
                        .setImgs(list)
                        .setCurrentIndex(index)
                        .setProgressColor(SettingSPUtils.getInstance().isUseCustomTheme() ? R.color.custom_color_main_theme : R.color.xui_config_color_main_theme)
                        .setType(PreviewBuilder.IndicatorType.Dot)
                        .start();//启动
            }
        });
    }

    /**
     * 查找信息
     *
     * @param list 图片集合
     */
    private void computeBoundsBackward(List<ImageViewInfo> list) {
        for (int i = 0; i < nglImages.getChildCount(); i++) {
            View itemView = nglImages.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView;
                thumbView.getGlobalVisibleRect(bounds);
            }
            list.get(i).setBounds(bounds);
            list.get(i).setUrl(list.get(i).getUrl());
        }

    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_attention, R.id.ll_reply, R.id.ll_comment, R.id.ll_like, R.id.ll_collection, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                XToastUtils.toast("更多");
                break;
            case R.id.btn_attention:
                XToastUtils.toast("关注");
                break;
            case R.id.ll_reply:
                XToastUtils.toast("回复");
                break;
            case R.id.ll_comment:
                XToastUtils.toast("评论");
                break;
            case R.id.ll_like:
                XToastUtils.toast("点赞");
                break;
            case R.id.ll_collection:
                XToastUtils.toast("收藏");
                break;
            case R.id.ll_share:
                XToastUtils.toast("分享转发");
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(this);
    }
}