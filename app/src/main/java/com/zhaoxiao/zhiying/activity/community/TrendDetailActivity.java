package com.zhaoxiao.zhiying.activity.community;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.activity.test.QuestionDetailActivity;
import com.zhaoxiao.zhiying.activity.test.TestNoteActivity;
import com.zhaoxiao.zhiying.adapter.community.CommentAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Comment;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.fragment.community.CommentFragment;
import com.zhaoxiao.zhiying.util.EPSoftKeyBoardListener;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.tv_share_title)
    TextView tvShareTitle;
    @BindView(R.id.tv_share_type)
    TextView tvShareType;
    @BindView(R.id.ll_share_link)
    LinearLayout llShareLink;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.btn_attention)
    Button btnAttention;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.ll_reply1)
    LinearLayout llReply1;

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
                pageNum = 1;
                refreshFlag = 0;
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

        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                llReply1.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                llReply1.setVisibility(View.GONE);
            }
        });

        account = SpUtils.getInstance(this).getString("account", "");
        communityService = (CommunityService) getService(CommunityService.class);

        //detail
        setImagesAdapter();
        setDetail();

        //comment
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(this);
        commentAdapter.setAccount(account);
        commentAdapter.setCommunityService(communityService);
        rv.setAdapter(commentAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setFocusable(false);

//        communityService = (CommunityService) getService(CommunityService.class);
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
//        account = SpUtils.getInstance(this).getString("account", "");
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
        tvLike.setText(NumberUtils.intChange2Str(trend.getLike()));
        tvComment.setText(NumberUtils.intChange2Str(trend.getComment()));
        tvCommentNum1.setText("（" + NumberUtils.intChange2Str(trend.getComment()) + "条）");
        tvShare.setText(NumberUtils.intChange2Str(trend.getShare()));
        tvCollection.setText(NumberUtils.intChange2Str(trend.getCollection()));

        //图片
        nglImages.setImagesData(trend.getImgList(), 0);

        Picasso.with(mContext)
                .load(trend.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(ivAvatar);

        //分享链接
        llShareLink.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            switch (trend.getLinkType()) {
                case 1:
                    map.put("articleTitle", trend.getLinkTitle());
                    map.put("channelName", trend.getChannelName());
                    map.put("img", trend.getArticleImg());
                    map.put("articleId", trend.getLinkId());
                    map.put("link", true);
                    map.put("edit", false);
                    map.put("account", trend.getUserAccount());
                    navigateTo(NoteActivity.class, "map", (Serializable) map);
                    break;
                case 2:
                    map.put("questionInfo", trend.getLinkTitle());
                    map.put("subType", trend.getSubType());
                    map.put("questionId", trend.getLinkId());
                    map.put("table", trend.getLinkTable());
                    map.put("link", true);
                    map.put("edit", false);
                    map.put("account", trend.getUserAccount());
                    navigateTo(TestNoteActivity.class, "map", (Serializable) map);
                    break;
                case 3:
                    navigateTo(ArticleActivity.class, "articleId", trend.getLinkId());
                    break;
                case 4:
                    map.put("questionId", trend.getLinkId());
                    map.put("table", trend.getLinkTable());
                    navigateTo(QuestionDetailActivity.class, "map", (Serializable) map);
                    break;
                case 5:
                    getTrendShare(trend.getLinkId());
                    break;
                default:
                    llShareLink.setVisibility(View.GONE);
                    break;
            }
        });
        tvShareTitle.setText(trend.getLinkTitle());
        tvShareType.setText(trend.getLinkTypeS());

        //点赞收藏关注状态
        boolean likeStatus = trend.getLikeStatus();
        if (likeStatus) {
            ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
            ivLike.setImageResource(R.drawable.like1_community);
//            trend.setLike(trend.getLike()+1);
//            tvLike.setText(String.valueOf(trend.getLike()));
        } else {
            ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
            ivLike.setImageResource(R.drawable.like_community);
//            trend.setLike(trend.getLike()-1);
//            tvLike.setText(String.valueOf(trend.getLike()));
        }
        boolean collectStatus = trend.getCollectStatus();
        if (collectStatus) {
            ivCollection.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
            ivCollection.setImageResource(R.drawable.star1_community);
//            trend.setCollection(trend.getCollection()+1);
//            tvCollection.setText(String.valueOf(trend.getCollection()));
        } else {
            ivCollection.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
            ivCollection.setImageResource(R.drawable.star_community);
//            trend.setCollection(trend.getCollection()-1);
//            tvCollection.setText(String.valueOf(trend.getCollection()));
        }
        boolean attentionStatus = trend.getAttentionStatus();
        if (attentionStatus) {
            btnAttention.setText("已关注");
            btnAttention.setTextColor(getResources().getColor(R.color.gray));
            btnAttention.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
        } else {
            btnAttention.setText("关注");
            btnAttention.setTextColor(getResources().getColor(R.color.white));
            btnAttention.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn));
        }
    }

    private void getCommentList(int type) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(8));
        map.put("sort", String.valueOf(commentSortType));
        map.put("order", "false");
        map.put("trendId", String.valueOf(trend.getId()));
        map.put("account", account);
        Call<Data<PageInfo<Comment>>> commentCall = communityService.getCommentList(map);
        commentCall.enqueue(new Callback<Data<PageInfo<Comment>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Comment>>> call, Response<Data<PageInfo<Comment>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    List<Comment> list = response.body().getData().getList();
                    switch (type) {
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
                            if (++refreshFlag == 2) srl.finishRefresh();
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
        Call<Data<Trend>> trendCall = communityService.getTrend(trend.getId(), account);
        trendCall.enqueue(new Callback<Data<Trend>>() {
            @Override
            public void onResponse(Call<Data<Trend>> call, Response<Data<Trend>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    switch (type) {
                        case 0:
                            trend = response.body().getData();
                            setDetail();
                            break;
                        case 1:
                            trend = response.body().getData();
                            setDetail();
                            if (++refreshFlag == 2) srl.finishRefresh();
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

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_attention, R.id.ll_reply, R.id.ll_comment, R.id.ll_like, R.id.ll_collection, R.id.ll_share, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
//                XToastUtils.toast("更多");
                showSimpleBottomSheetList();
                break;
            case R.id.btn_attention:
//                XToastUtils.toast("关注");
                boolean attentionStatus = trend.getAttentionStatus();
                attention(!attentionStatus);
                break;
            case R.id.ll_reply:
//                llReply1.setVisibility(View.VISIBLE);
                EditTextUtil.showSoftInputFromWindow(this, etReply);
//                XToastUtils.toast("回复");
                break;
            case R.id.ll_comment:
//                XToastUtils.toast("评论");
                break;
            case R.id.ll_like:
//                XToastUtils.toast("点赞");
                boolean likeStatus = trend.getLikeStatus();
                like(!likeStatus);
                break;
            case R.id.ll_collection:
//                XToastUtils.toast("收藏");
                boolean collectStatus = trend.getCollectStatus();
                collect(!collectStatus);
                break;
            case R.id.ll_share:
//                XToastUtils.toast("分享转发");
                Map<String, Object> linkMap = new HashMap<>();
                linkMap.put("linkId", trend.getId());
                linkMap.put("info", trend.getInfo());
                navigateTo(PublishTrendActivity.class, "linkMap", (Serializable) linkMap);
                break;
            case R.id.tv_send:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        String info = etReply.getText().toString().trim();
        if (StringUtils.isEmpty(info)){
            return;
        }
        Call<Data<Boolean>> sendCommentCall = communityService.sendComment(trend.getId(), account, info);
        sendCommentCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        XToastUtils.toast("评论成功");
                        etReply.setText("");
                        EditTextUtil.hideKeyboard(TrendDetailActivity.this);
                        getCommentList(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(this);
    }

    private void like(boolean like) {
        Call<Data<Boolean>> likeCall = communityService.like(account, trend.getId(), like);
        likeCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        trend.setLikeStatus(like);
                        if (like) {
                            ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
                            ivLike.setImageResource(R.drawable.like1_community);
                            trend.setLike(trend.getLike() + 1);
                            tvLike.setText(NumberUtils.intChange2Str(trend.getLike()));
                        } else {
                            ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
                            ivLike.setImageResource(R.drawable.like_community);
                            trend.setLike(trend.getLike() - 1);
                            tvLike.setText(NumberUtils.intChange2Str(trend.getLike()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (like) {
                    XToastUtils.toast("点赞失败");
                } else {
                    XToastUtils.toast("取消点赞失败");
                }
            }
        });
    }

    private void collect(boolean collect) {
        Call<Data<Boolean>> collectCall = communityService.collect(account, trend.getId(), collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        trend.setCollectStatus(collect);
                        if (collect) {
                            ivCollection.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
                            ivCollection.setImageResource(R.drawable.star1_community);
                            trend.setCollection(trend.getCollection() + 1);
                            tvCollection.setText(NumberUtils.intChange2Str(trend.getCollection()));
                        } else {
                            ivCollection.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
                            ivCollection.setImageResource(R.drawable.star_community);
                            trend.setCollection(trend.getCollection() - 1);
                            tvCollection.setText(NumberUtils.intChange2Str(trend.getCollection()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (collect) {
                    XToastUtils.toast("收藏失败");
                } else {
                    XToastUtils.toast("取消收藏失败");
                }
            }
        });
    }

    private void getTrendShare(int trendId) {
        Call<Data<Trend>> trendShareCall = communityService.getTrend(trendId, account);
        trendShareCall.enqueue(new Callback<Data<Trend>>() {
            @Override
            public void onResponse(Call<Data<Trend>> call, Response<Data<Trend>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Trend trendShare = response.body().getData();
                    navigateTo(TrendDetailActivity.class, "trend", trendShare);
                }
            }

            @Override
            public void onFailure(Call<Data<Trend>> call, Throwable t) {

            }
        });
    }

    //更多
    private void showSimpleBottomSheetList() {
        boolean collectStatus = trend.getCollectStatus();
        BottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new BottomSheet.BottomListSheetBuilder(this);
        bottomListSheetBuilder
                .setTitle("更多");
        if (collectStatus) {
            bottomListSheetBuilder
                    .addItem("取消收藏");
        } else {
            bottomListSheetBuilder
                    .addItem("收藏");
        }
        bottomListSheetBuilder
                .addItem("举报")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if (position == 0) {
                        collect(!collectStatus);
                    } else if (position == 1) {
                        XToastUtils.toast("举报");
                    }
                })
                .build()
                .show();
    }

    private void attention(boolean attention) {
        Call<Data<Boolean>> attentionCall = communityService.attention(trend.getUserAccount(), account, attention);
        attentionCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        trend.setAttentionStatus(attention);
                        if (attention) {
                            btnAttention.setText("已关注");
                            btnAttention.setTextColor(getResources().getColor(R.color.gray));
                            btnAttention.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn1));
                        } else {
                            btnAttention.setText("关注");
                            btnAttention.setTextColor(getResources().getColor(R.color.white));
                            btnAttention.setBackground(getResources().getDrawable(R.drawable.shape_attention_btn));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (attention) {
                    XToastUtils.toast("关注失败");
                } else {
                    XToastUtils.toast("取消关注失败");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
}