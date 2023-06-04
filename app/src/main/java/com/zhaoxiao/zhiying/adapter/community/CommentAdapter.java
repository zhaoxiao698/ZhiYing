package com.zhaoxiao.zhiying.adapter.community;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.imageview.nine.ItemImageClickListener;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageViewAdapter;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.textview.ReadMoreTextView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Comment;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Comment> list;
    private CommunityService communityService;
    private String account;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public void setCommunityService(CommunityService communityService) {
        this.communityService = communityService;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public CommentAdapter(Context mContext, List<Comment> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Comment comment = list.get(position);
        viewHolder.tvName.setText(comment.getUserName());
        viewHolder.tvAddTime.setText(StringUtils.formatDateTime(comment.getAddTime()));
        if (StringUtils.isEmpty(comment.getInfo())){
            viewHolder.tvInfo.setVisibility(View.GONE);
        } else {
            viewHolder.tvInfo.setVisibility(View.VISIBLE);
            viewHolder.tvInfo.setText(comment.getInfo());
        }

        //话题


        //热评


        //操作条
        viewHolder.tvLike.setText(NumberUtils.intChange2Str(comment.getLike()));

        //图片
        Picasso.with(mContext)
                .load(ApiConfig.BASE_URl+comment.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivAvatar);

        viewHolder.commentId = comment.getId();
        viewHolder.comment = comment;

        //点赞状态
        boolean likeStatus = comment.getLikeStatus();
        if(likeStatus){
            viewHolder.ivLike.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
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

        private Integer commentId;
        private Comment comment;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvAddTime;
        private TextView tvInfo;
        private LinearLayout llLike;
        private ImageView ivLike;
        private TextView tvLike;


        public ViewHolder(@NonNull View view) {
            super(view);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvName = view.findViewById(R.id.tv_name);
            tvAddTime = view.findViewById(R.id.tv_add_time);
            tvInfo = view.findViewById(R.id.tv_info);
            llLike = view.findViewById(R.id.ll_like);
            ivLike = view.findViewById(R.id.iv_like);
            tvLike = view.findViewById(R.id.tv_like);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(commentId);
//                }
//            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    XToastUtils.toast("点赞");
                    boolean likeStatus = comment.getLikeStatus();
                    like(!likeStatus,comment,ivLike,tvLike);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer commentId);
    }

    private void like(boolean like,Comment comment,ImageView view,TextView num) {
        Call<Data<Boolean>> likeCommentCall = communityService.likeComment(comment.getId(), account, like);
        likeCommentCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        comment.setLikeStatus(like);
                        if (like){
                            view.setImageTintList(mContext.getResources().getColorStateList(R.color.g_yellow));
                            view.setImageResource(R.drawable.like1_community);
                            comment.setLike(comment.getLike()+1);
                            num.setText(NumberUtils.intChange2Str(comment.getLike()));
                        } else {
                            view.setImageTintList(mContext.getResources().getColorStateList(R.color.gray));
                            view.setImageResource(R.drawable.like_community);
                            comment.setLike(comment.getLike()-1);
                            num.setText(NumberUtils.intChange2Str(comment.getLike()));
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
}
