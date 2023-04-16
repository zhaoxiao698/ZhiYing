package com.zhaoxiao.zhiying.adapter.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.util.UnitConversion;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WordSimple> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<WordSimple> list) {
        this.list = list;
    }

    public WordListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public WordListAdapter(Context mContext, List<WordSimple> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_word_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        WordSimple wordSimple = list.get(position);
        viewHolder.tvWord.setText(wordSimple.getWordHead());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, UnitConversion.dp2px(mContext, 5), 0, 0);
        viewHolder.llTran.removeAllViews();
        for (WordSimple.TransBean tran : wordSimple.getTrans()) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_word_tran, null);
            TextView tvPos = viewGroup.findViewById(R.id.tv_pos);
            TextView tvTran = viewGroup.findViewById(R.id.tv_tran);
            tvPos.setText(tran.getPos());
            tvTran.setText(tran.getTranCn());
            viewHolder.llTran.addView(viewGroup, lp);
        }
        boolean isCollected = wordSimple.getCollect();
        if(isCollected){
            viewHolder.ivCollection.setImageResource(R.drawable.star1_community);
        } else {
            viewHolder.ivCollection.setImageResource(R.drawable.star_community);
        }

        viewHolder.wordId = wordSimple.getWordId();
        viewHolder.wordSimple = wordSimple;
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
        private WordSimple wordSimple;
        private TextView tvWord;
        private ImageView ivCollection;
        private LinearLayout llTran;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvWord = view.findViewById(R.id.tv_word);
            ivCollection = view.findViewById(R.id.iv_collection);
            llTran = view.findViewById(R.id.ll_tran);
            llTran.setVisibility(View.GONE);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(wordId);
//                }
//            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (llTran.getVisibility()==View.GONE){
                        llTran.setVisibility(View.VISIBLE);
                    } else if (llTran.getVisibility()==View.VISIBLE){
                        llTran.setVisibility(View.GONE);
                    }
                }
            });
            ivCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onIconClick(wordSimple,ivCollection);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String wordId);

        void onIconClick(WordSimple wordSimple,ImageView ivCollection);
    }
}
