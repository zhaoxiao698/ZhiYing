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
import com.zhaoxiao.zhiying.entity.study.Channel;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Channel> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Channel> list) {
        this.list = list;
    }

    public ChannelAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ChannelAdapter(Context mContext, List<Channel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_more_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Channel channel = list.get(position);
        viewHolder.tvName.setText(channel.getName());
        viewHolder.tvNum.setText("共" + NumberUtils.intChange2Str(channel.getNum()) + "篇");
        viewHolder.tvCollection.setText("已订阅：" + NumberUtils.intChange2Str(channel.getCollection()));
        viewHolder.tvLastTime.setText("更新时间：" + StringUtils.formatDate(channel.getLastTime()));
        Picasso.with(mContext)
                .load(ApiConfig.BASE_URl+channel.getImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.channelId = channel.getId();
        viewHolder.channel = channel;
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

        private Integer channelId;
        private Channel channel;
        private ImageView ivImg;
        private TextView tvName;
        private TextView tvNum;
        private TextView tvCollection;
        private TextView tvLastTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = view.findViewById(R.id.iv_img);
            tvName = view.findViewById(R.id.tv_name);
            tvNum = view.findViewById(R.id.tv_num);
            tvCollection = view.findViewById(R.id.tv_collection);
            tvLastTime = view.findViewById(R.id.tv_last_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(channelId);
//                    System.out.println(channelId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int channelId);
    }
}
