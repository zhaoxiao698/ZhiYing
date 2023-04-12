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
import com.zhaoxiao.zhiying.entity.community.Comment;
import com.zhaoxiao.zhiying.entity.community.ImageViewInfo;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.util.SettingSPUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Comment> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Comment> list) {
        this.list = list;
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
        viewHolder.tvAddTime.setText(StringUtils.formatDate(comment.getAddTime()));
        if (StringUtils.isEmpty(comment.getInfo())){
            viewHolder.tvInfo.setVisibility(View.GONE);
        } else {
            viewHolder.tvInfo.setVisibility(View.VISIBLE);
            viewHolder.tvInfo.setText(comment.getInfo());
        }

        //话题


        //热评


        //操作条
        viewHolder.tvLike.setText(String.valueOf(comment.getLike()));

        //图片
        Picasso.with(mContext)
                .load(comment.getUserAvatar())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivAvatar);

        viewHolder.commentId = comment.getId();
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
                    XToastUtils.toast("点赞");
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer commentId);
    }
}
