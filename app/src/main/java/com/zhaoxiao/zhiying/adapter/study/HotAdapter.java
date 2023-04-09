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
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Hot> list;
    private Integer layoutId;
    private Boolean more;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Hot> list) {
        this.list = list;
    }

    public HotAdapter(Context mContext, Integer layoutId, Boolean more) {
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.more = more;
    }

    public HotAdapter(Context mContext, Integer layoutId, Boolean more, List<Hot> list) {
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.more = more;
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
        Hot hot = list.get(position);
        viewHolder.tvName.setText(hot.getName());
        if (more) {
            viewHolder.tvNum.setText("共"+hot.getNum()+"篇");
            viewHolder.tvCollection.setText("已订阅："+hot.getCollection());
            viewHolder.tvLastTime.setText("更新时间："+ StringUtils.formatDate(hot.getLastTime()));
        }
        Picasso.with(mContext)
                .load(hot.getImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.hotId = hot.getId();
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

        private Integer hotId;
        private ImageView ivImg;
        private TextView tvName;
        private TextView tvNum;
        private TextView tvCollection;
        private TextView tvLastTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = view.findViewById(R.id.iv_img);
            tvName = view.findViewById(R.id.tv_name);
            if (more) {
                tvNum = view.findViewById(R.id.tv_num);
                tvCollection = view.findViewById(R.id.tv_collection);
                tvLastTime = view.findViewById(R.id.tv_last_time);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(hotId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer hotId);
    }
}
