package com.zhaoxiao.zhiying.adapter.study;

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
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Recent> list;
    private Integer layoutId;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Recent> list) {
        this.list = list;
    }

    public RecentAdapter(Context mContext, Integer layoutId) {
        this.mContext = mContext;
        this.layoutId = layoutId;
    }

    public RecentAdapter(Context mContext, Integer layoutId, List<Recent> list) {
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Recent recent = list.get(position);
        viewHolder.tvTitle.setText(recent.getTitle());
        viewHolder.tvChannelName.setText(recent.getChannelName());
        viewHolder.tvDuration.setText(recent.getDuration());
        Picasso.with(mContext)
                .load(ApiConfig.BASE_URl+recent.getImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.recentId = recent.getId();
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

        private Integer recentId;
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvChannelName;
        private TextView tvDuration;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = view.findViewById(R.id.iv_img);
            tvTitle = view.findViewById(R.id.tv_title);
            tvChannelName = view.findViewById(R.id.tv_channel_name);
            tvDuration = view.findViewById(R.id.tv_duration);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(recentId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer recentId);
    }
}
