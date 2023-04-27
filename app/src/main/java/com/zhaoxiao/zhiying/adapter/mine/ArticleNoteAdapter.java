package com.zhaoxiao.zhiying.adapter.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class ArticleNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ArticleNoteDetail> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<ArticleNoteDetail> list) {
        this.list = list;
    }

    public ArticleNoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArticleNoteAdapter(Context mContext, List<ArticleNoteDetail> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_note_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ArticleNoteDetail articleNoteDetail = list.get(position);
        viewHolder.tvInfo.setText(articleNoteDetail.getInfo());
        viewHolder.tvChannelName.setText(articleNoteDetail.getChannelName());
        viewHolder.tvTitle.setText(articleNoteDetail.getArticleTitle());
        Picasso.with(mContext)
                .load(articleNoteDetail.getArticleImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.articleId = articleNoteDetail.getArticleId();
        viewHolder.articleNoteDetail = articleNoteDetail;
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

        private Integer articleId;
        private ArticleNoteDetail articleNoteDetail;
        private TextView tvInfo;
        private RelativeLayout rlLink;
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvChannelName;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvInfo = view.findViewById(R.id.tv_info);
            rlLink = view.findViewById(R.id.rl_link);
            ivImg = view.findViewById(R.id.iv_img);
            tvTitle = view.findViewById(R.id.tv_title);
            tvChannelName = view.findViewById(R.id.tv_channel_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(articleNoteDetail);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ArticleNoteDetail articleNoteDetail);
    }
}
