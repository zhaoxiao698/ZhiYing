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
import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<User> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public UserAdapter(Context mContext,List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        User user = list.get(position);
        viewHolder.tvName.setText(user.getName());
        Picasso.with(mContext)
                .load(user.getAvatar())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivAvatar);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XToastUtils.toast(user.getName());
            }
        });
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

        private ImageView ivAvatar;
        private TextView tvName;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer hotId);
    }
}
