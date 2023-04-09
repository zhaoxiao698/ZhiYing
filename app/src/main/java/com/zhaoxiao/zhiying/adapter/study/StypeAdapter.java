package com.zhaoxiao.zhiying.adapter.study;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.Stype;

import java.util.List;

public class StypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Stype> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Stype> list) {
        this.list = list;
    }

    public StypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public StypeAdapter(Context mContext, List<Stype> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_stype_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Stype stype = list.get(position);
        viewHolder.tv.setText(stype.getName());
        viewHolder.stypeId = stype.getId();
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

        private Integer stypeId;
        private TextView tv;

        public ViewHolder(@NonNull View view) {
            super(view);
            tv = view.findViewById(R.id.tv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(stypeId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer stypeId);
    }
}
