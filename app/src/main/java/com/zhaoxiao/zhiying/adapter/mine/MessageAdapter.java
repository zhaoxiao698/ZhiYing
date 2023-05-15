package com.zhaoxiao.zhiying.adapter.mine;

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
import com.zhaoxiao.zhiying.entity.mine.Message;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Message> list;

    public void setList(List<Message> list) {
        this.list = list;
    }

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public MessageAdapter(Context mContext, List<Message> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Message message = list.get(position);
        viewHolder.tvMessage.setText(message.getInfo());
        viewHolder.tvMessage.setText(message.getInfo());
        viewHolder.tvTime.setText(StringUtils.formatDateTime(message.getAddTime()));
        viewHolder.messageId = message.getId();
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

        private int messageId;
        private TextView tvMessage;
        private TextView tvTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvMessage = view.findViewById(R.id.tv_message);
            tvTime = view.findViewById(R.id.tv_time);
        }
    }
}
