package com.zhaoxiao.zhiying.adapter.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.word.Book;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class WordCompleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WordSimple> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<WordSimple> list) {
        this.list = list;
    }

    public WordCompleteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public WordCompleteAdapter(Context mContext, List<WordSimple> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_word_complete_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        WordSimple wordSimple = list.get(position);
        viewHolder.tvWord.setText(wordSimple.getWordHead());
        if (wordSimple.getNextDayNum()<0){
            viewHolder.tvNextTime.setText("复习完成");
            viewHolder.tvNextTime.setTextColor(mContext.getResources().getColor(R.color.g_green));
            viewHolder.hpv.setStartColor(mContext.getResources().getColor(R.color.g_green));
            viewHolder.hpv.setEndColor(mContext.getResources().getColor(R.color.g_green));
        } else {
            viewHolder.tvNextTime.setText(wordSimple.getNextDayNum()+"天后");
        }
        viewHolder.hpv.setProgress((float)wordSimple.getProficiency()/7*100);

        viewHolder.wordId = wordSimple.getWordId();
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

        private String wordId;
        private TextView tvWord;
        private HorizontalProgressView hpv;
        private TextView tvNextTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvWord = view.findViewById(R.id.tv_word);
            hpv = view.findViewById(R.id.hpv);
            tvNextTime = view.findViewById(R.id.tv_next_time);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(wordId);
//                }
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String wordId);
    }
}
