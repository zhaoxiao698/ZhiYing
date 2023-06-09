package com.zhaoxiao.zhiying.adapter.community;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xui.widget.textview.ReadMoreTextView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.community.PublishTrendActivity;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Trend> list;
    private CommunityService communityService;
    private String account;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Trend> list) {
        this.list = list;
    }

    public void setCommunityService(CommunityService communityService) {
        this.communityService = communityService;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public TrendAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TrendAdapter(Context mContext, List<Trend> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trend_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Trend trend = list.get(position);
        viewHolder.tvName.setText(trend.getUserName());
        viewHolder.tvAddTime.setText(StringUtils.formatDateTime(trend.getAddTime()));
        if (StringUtils.isEmpty(trend.getTitle())){
            viewHolder.tvTitle.setVisibility(View.GONE);
        } else {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(trend.getTitle());
        }
        if (StringUtils.isEmpty(trend.getInfo())){
            viewHolder.tvInfo.setVisibility(View.GONE);
        } else {
            String info = StringUtils.foldString(trend.getInfo(),60);
            viewHolder.tvInfo.setText(info);
            viewHolder.tvInfo.setVisibility(View.VISIBLE);
//            viewHolder.tvInfo.setText(trend.getInfo());
        }

        //话题
        viewHolder.flTopic.removeAllViews();
        for (Topic topic : trend.getTopicList()) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_topic_item, null);
            RoundButton child = viewGroup.findViewById(R.id.tv_option);
            viewGroup.removeView(child);
            child.setText("#"+topic.getName());
            child.setOnClickListener(view -> XToastUtils.info(topic.getName()));
            viewHolder.flTopic.addView(child);
        }

        //热评
        if (trend.getHotComment()==null||trend.getHotComment().getLike()<=0){
            viewHolder.llHotComment.setVisibility(View.GONE);
        } else {
            viewHolder.llHotComment.setVisibility(View.VISIBLE);
            viewHolder.tvHotCommentLike.setText(NumberUtils.intChange2Str(trend.getHotComment().getLike()) + "赞");
            String name = "<font color=\"#FBBC05\">"+trend.getHotComment().getUserName()+"</font>： ";
            String hotComment = StringUtils.foldString(trend.getHotComment().getInfo(),55);
            viewHolder.tvHotComment.setText(Html.fromHtml(name+hotComment));
//            viewHolder.tvHotComment.setText(trend.getHotComment().getInfo());
        }

        //操作条
        viewHolder.tvLike.setText(NumberUtils.intChange2Str(trend.getLike()));
        viewHolder.tvComment.setText(NumberUtils.intChange2Str(trend.getComment()));
        viewHolder.tvShare.setText(NumberUtils.intChange2Str(trend.getShare()));

        //图片
        viewHolder.bind(trend.getImgList());

        Picasso.with(mContext)
                .load(ApiConfig.BASE_URl+trend.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivAvatar);

        viewHolder.trendId = trend.getId();
        viewHolder.trend = trend;

        //点赞状态
        boolean likeStatus = trend.getLikeStatus();
        if(likeStatus){
            viewHolder.ivLike.setImageTintList(ColorStateList.valueOf(((BaseActivity)mContext).getMyBgColor()));
            viewHolder.ivLike.setImageResource(R.drawable.like1_community);
        } else {
            viewHolder.ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
            viewHolder.ivLike.setImageResource(R.drawable.like_community);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    //绑定控件，得到控件对象
    public class ViewHolder extends RecyclerView.ViewHolder {

        private Integer trendId;
        private Trend trend;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvAddTime;
        private TextView tvTitle;
        private TextView tvInfo;
        private NineGridImageView<ImageViewInfo> nglImages;
        private FlexboxLayout flTopic;
        private LinearLayout llHotComment;
        private TextView tvHotCommentLike;
        private TextView tvHotComment;
        private LinearLayout llLike;
        private ImageView ivLike;
        private TextView tvLike;
        private LinearLayout llComment;
        private ImageView ivComment;
        private TextView tvComment;
        private LinearLayout llShare;
        private ImageView ivShare;
        private TextView tvShare;
        private ImageView ivMore;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvName = view.findViewById(R.id.tv_name);
            tvAddTime = view.findViewById(R.id.tv_add_time);
            tvTitle = view.findViewById(R.id.tv_title);
            tvInfo = view.findViewById(R.id.tv_info);
            nglImages = view.findViewById(R.id.ngl_images);
            flTopic = view.findViewById(R.id.fl_topic);
            llHotComment = view.findViewById(R.id.ll_hot_comment);
            tvHotCommentLike = view.findViewById(R.id.tv_hot_comment_like);
            tvHotComment = view.findViewById(R.id.tv_hot_comment);
            llLike = view.findViewById(R.id.ll_like);
            ivLike = view.findViewById(R.id.iv_like);
            tvLike = view.findViewById(R.id.tv_like);
            llComment = view.findViewById(R.id.ll_comment);
            ivComment = view.findViewById(R.id.iv_comment);
            tvComment = view.findViewById(R.id.tv_comment);
            llShare = view.findViewById(R.id.ll_share);
            ivShare = view.findViewById(R.id.iv_share);
            tvShare = view.findViewById(R.id.tv_share);
            ivMore = view.findViewById(R.id.iv_more);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(trend);
                }
            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    XToastUtils.toast("点赞");
                    boolean likeStatus = trend.getLikeStatus();
                    like(!likeStatus,trend,ivLike,tvLike);
                }
            });
            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(trend);
//                    XToastUtils.toast("评论");
                }
            });
            llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    XToastUtils.toast("分享转发");
                    Map<String,Object> linkMap = new HashMap<>();
                    linkMap.put("linkId",trend.getId());
                    linkMap.put("info",trend.getInfo());
                    ((BaseActivity)mContext).navigateTo(PublishTrendActivity.class,"linkMap", (Serializable) linkMap);
                }
            });
            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSimpleBottomSheetList(trend);
                }
            });
//            tvInfo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mOnItemClickListener.onItemClick(trend);
//                }
//            });
//            tvInfo.setTrimCollapsedText("");
//            tvInfo.setClickable(false);

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
//                    Glide.with(imageView.getContext())
//                            .load(imageViewInfo.getUrl())
//                            .apply(GlideMediaLoader.getRequestOptions())
//                            .into(imageView);

                    Picasso.with(mContext)
                            .load(ApiConfig.BASE_URl+imageViewInfo.getUrl())
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
         * @param list 图片集合
         */
        private void computeBoundsBackward(List<ImageViewInfo> list) {
            for (int i = 0;i < nglImages.getChildCount(); i++) {
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

        public void bind(List<ImageViewInfo> imgList) {
            nglImages.setImagesData(imgList, 0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Trend trend);
    }

    private void like(boolean like,Trend trend,ImageView view,TextView num) {
        Call<Data<Boolean>> likeCall = communityService.like(account, trend.getId(), like);
        likeCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        trend.setLikeStatus(like);
                        if (like){
                            view.setImageTintList(ColorStateList.valueOf(((BaseActivity)mContext).getMyBgColor()));
                            view.setImageResource(R.drawable.like1_community);
                            trend.setLike(trend.getLike()+1);
                            num.setText(NumberUtils.intChange2Str(trend.getLike()));
                        } else {
                            view.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
                            view.setImageResource(R.drawable.like_community);
                            trend.setLike(trend.getLike()-1);
                            num.setText(NumberUtils.intChange2Str(trend.getLike()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (like){
                    XToastUtils.toast("点赞失败");
                } else {
                    XToastUtils.toast("取消点赞失败");
                }
            }
        });
    }

    private void collect(boolean collect,Trend trend) {
        Call<Data<Boolean>> collectCall = communityService.collect(account, trend.getId(), collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        trend.setCollectStatus(collect);
                        if (collect){
                            XToastUtils.toast("收藏成功");
                        } else {
                            XToastUtils.toast("取消收藏成功");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                if (collect){
                    XToastUtils.toast("收藏失败");
                } else {
                    XToastUtils.toast("取消收藏失败");
                }
            }
        });
    }

    //更多
    private void showSimpleBottomSheetList(Trend trend) {
        boolean collectStatus = trend.getCollectStatus();
        BottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new BottomSheet.BottomListSheetBuilder(mContext);
        bottomListSheetBuilder
                .setTitle("更多");
        if (collectStatus){
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
                        collect(!collectStatus,trend);
                    } else if (position == 1) {
                        XToastUtils.toast("举报");
                    }
                })
                .build()
                .show();
    }
}
