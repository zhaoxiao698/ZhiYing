package com.zhaoxiao.zhiying.adapter.community;

import android.app.Activity;
import android.content.Context;
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
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.imageview.preview.loader.GlideMediaLoader;
import com.xuexiang.xui.widget.textview.ReadMoreTextView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class TrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Trend> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Trend> list) {
        this.list = list;
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
        viewHolder.tvAddTime.setText(StringUtils.formatDate(trend.getAddTime()));
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
            viewHolder.tvHotCommentLike.setText(trend.getHotComment().getLike() + "赞");
            String name = "<font color=\"#FBBC05\">"+trend.getHotComment().getUserName()+"</font>： ";
            String hotComment = StringUtils.foldString(trend.getHotComment().getInfo(),55);
            viewHolder.tvHotComment.setText(Html.fromHtml(name+hotComment));
//            viewHolder.tvHotComment.setText(trend.getHotComment().getInfo());
        }

        //操作条
        viewHolder.tvLike.setText(String.valueOf(trend.getLike()));
        viewHolder.tvComment.setText(String.valueOf(trend.getComment()));
        viewHolder.tvShare.setText(String.valueOf(trend.getShare()));

        //图片
        viewHolder.bind(trend.getImgList());

        Picasso.with(mContext)
                .load(trend.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivAvatar);

        viewHolder.trendId = trend.getId();
        viewHolder.trend = trend;
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(trend);
                }
            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    XToastUtils.toast("点赞");
                }
            });
            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    XToastUtils.toast("评论");
                }
            });
            llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    XToastUtils.toast("分享转发");
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
}
