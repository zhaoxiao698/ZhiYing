package com.zhaoxiao.zhiying.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.Article;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Article> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Article> list) {
        this.list = list;
    }

    public ArticleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArticleAdapter(Context mContext, List<Article> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Article article = list.get(position);
        viewHolder.tvTitle.setText(article.getTitle());
        viewHolder.tvDuration.setText(article.getDuration());
        viewHolder.tvCount.setText(NumberUtils.intChange2Str(article.getCount()));
        viewHolder.tvAddTime.setText(StringUtils.formatDate(article.getAddTime()));
        Picasso.with(mContext)
                .load(article.getImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.articleId = article.getId();
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
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvDuration;
        private TextView tvCount;
        private TextView tvAddTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = view.findViewById(R.id.iv_img);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDuration = view.findViewById(R.id.tv_duration);
            tvCount = view.findViewById(R.id.tv_count);
            tvAddTime = view.findViewById(R.id.tv_add_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(articleId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer articleId);
    }
}
