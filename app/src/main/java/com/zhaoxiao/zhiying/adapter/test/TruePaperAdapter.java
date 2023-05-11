package com.zhaoxiao.zhiying.adapter.test;

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
import com.zhaoxiao.zhiying.entity.test.TruePaper;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class TruePaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TruePaper> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<TruePaper> list) {
        this.list = list;
    }

    public TruePaperAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TruePaperAdapter(Context mContext, List<TruePaper> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_true_paper_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TruePaper truePaper = list.get(position);
        viewHolder.tvName.setText(truePaper.getName());
        viewHolder.tvQuestionBank.setText(truePaper.getQuestionBankName());
        viewHolder.truePaperId = truePaper.getId();
        viewHolder.truePaper = truePaper;
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

        private Integer truePaperId;
        private TruePaper truePaper;
        private TextView tvName;
        private TextView tvQuestionBank;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvQuestionBank = view.findViewById(R.id.tv_questionBank);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(truePaper);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TruePaper truePaper);
    }
}
