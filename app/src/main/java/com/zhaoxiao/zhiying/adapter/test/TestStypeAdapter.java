package com.zhaoxiao.zhiying.adapter.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.test.TestStype;

import java.util.List;

public class TestStypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TestStype> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<TestStype> list) {
        this.list = list;
    }

    public TestStypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TestStypeAdapter(Context mContext, List<TestStype> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_test_stype_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TestStype stype = list.get(position);
        viewHolder.tvName.setText(stype.getName());
        viewHolder.tvNum.setText("共"+stype.getNum()+"题  |  已练习"+stype.getFinish()+"题");
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
        private TextView tvName;
        private TextView tvNum;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvNum = view.findViewById(R.id.tv_num);

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
