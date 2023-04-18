package com.zhaoxiao.zhiying.adapter.study;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.Sentence;

import java.util.List;

public class SentenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TITLE = 1;
    private static final int TYPE_SENTENCE = 2;
    private static final int TYPE_SENTENCE_LIGHT = 3;

    private String title;
    private String addTime;
    private int light = 1;
    private int translationType = 0;
    private boolean[] translationList;

    private Context mContext;
    private List<Sentence> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    public void setList(List<Sentence> list) {
        this.list = list;
        translationList = new boolean[list.size()];
    }

    public SentenceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public SentenceAdapter(Context mContext, List<Sentence> list) {
        this.mContext = mContext;
        this.list = list;
        translationList = new boolean[list.size()];
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_TITLE;
        if (position == light) return TYPE_SENTENCE_LIGHT;
        return TYPE_SENTENCE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_title_layout, parent, false);
            return new ViewHolderTitle(view);
        } else if (viewType == TYPE_SENTENCE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_sentence_layout, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_sentence_light_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderTitle) {
            ViewHolderTitle viewHolderTitle = (ViewHolderTitle) holder;
            viewHolderTitle.tvTitle.setText(title);
            viewHolderTitle.tvAddTime.setText(addTime);
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            Sentence sentence = list.get(position - 1);
            viewHolder.tvEnglish.setText(sentence.getEnglish());
            viewHolder.tvTranslation.setText(sentence.getTranslation());
            viewHolder.sentenceId = sentence.getId();

            switch (translationType){
                case 0:
                    viewHolder.tvEnglish.setVisibility(View.VISIBLE);
                    viewHolder.tvTranslation.setVisibility(View.VISIBLE);

                    if (position==light) {
                        viewHolder.tvTranslation.setTextColor(mContext.getResources().getColor(R.color.gray));
                        viewHolder.tvTranslation.setTypeface(Typeface.DEFAULT);
                    }

                    viewHolder.tvTranslation.setText(sentence.getTranslation());
                    viewHolder.tvTranslation.setGravity(Gravity.LEFT);
                    viewHolder.tvTranslation.setOnClickListener(null);
                    viewHolder.translation=false;

//                    viewHolder.translation = true;
                    break;
                case 1:
                    viewHolder.tvEnglish.setVisibility(View.VISIBLE);
                    viewHolder.tvTranslation.setVisibility(View.GONE);

                    if (position==light) {
                        viewHolder.tvTranslation.setTextColor(mContext.getResources().getColor(R.color.gray));
                        viewHolder.tvTranslation.setTypeface(Typeface.DEFAULT);
                    }

                    viewHolder.tvTranslation.setText(sentence.getTranslation());
                    viewHolder.tvTranslation.setGravity(Gravity.LEFT);
//                    viewHolder.translation = true;
                    viewHolder.tvTranslation.setOnClickListener(null);
                    viewHolder.translation=false;

                    translationList[position-1]=false;

                    break;
                case 2:
                    viewHolder.tvEnglish.setVisibility(View.GONE);
                    viewHolder.tvTranslation.setVisibility(View.VISIBLE);

                    if (position==light) {
                        viewHolder.tvTranslation.setTextColor(mContext.getResources().getColor(R.color.blue));
                        viewHolder.tvTranslation.setTypeface(Typeface.DEFAULT_BOLD);
                    }

                    viewHolder.tvTranslation.setText(sentence.getTranslation());
                    viewHolder.tvTranslation.setGravity(Gravity.LEFT);
//                    viewHolder.translation = true;
                    viewHolder.tvTranslation.setOnClickListener(null);
                    viewHolder.translation=false;

                    translationList[position-1]=false;

                    break;
                case 3:
                    viewHolder.tvEnglish.setVisibility(View.VISIBLE);
                    viewHolder.tvTranslation.setVisibility(View.VISIBLE);

                    if (position==light) {
                        viewHolder.tvTranslation.setTextColor(mContext.getResources().getColor(R.color.gray));
                        viewHolder.tvTranslation.setTypeface(Typeface.DEFAULT);
                    }

                    System.out.println("position:"+position+"---> "+"translation:"+viewHolder.translation);
                    if (/*viewHolder.translation*/translationList[position-1]) {
                        viewHolder.tvTranslation.setText(sentence.getTranslation());
                        viewHolder.tvTranslation.setGravity(Gravity.LEFT);
                    } else {
                        viewHolder.tvTranslation.setText("（点击显示译文）");
                        viewHolder.tvTranslation.setGravity(Gravity.CENTER);
                    }
                    viewHolder.tvTranslation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (viewHolder.translation){
                                viewHolder.tvTranslation.setText("（点击显示译文）");
                                viewHolder.tvTranslation.setGravity(Gravity.CENTER);
                                viewHolder.translation = false;
                                translationList[position-1]=false;
                            } else {
                                viewHolder.tvTranslation.setText(sentence.getTranslation());
                                viewHolder.tvTranslation.setGravity(Gravity.LEFT);
                                viewHolder.translation = true;
                                translationList[position-1]=true;
                            }
                        }
                    });
                    break;
                case 4:
                    viewHolder.tvEnglish.setVisibility(View.GONE);
                    viewHolder.tvTranslation.setVisibility(View.GONE);

                    if (position==light) {
                        viewHolder.tvTranslation.setTextColor(mContext.getResources().getColor(R.color.gray));
                        viewHolder.tvTranslation.setTypeface(Typeface.DEFAULT);
                    }

                    viewHolder.tvTranslation.setText(sentence.getTranslation());
                    viewHolder.tvTranslation.setGravity(Gravity.LEFT);
//                    viewHolder.translation = true;
                    viewHolder.tvTranslation.setOnClickListener(null);
                    viewHolder.translation=false;

                    translationList[position-1]=false;

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size() + 1;
        }
        return 1;
    }

    //绑定控件，得到控件对象
    public class ViewHolder extends RecyclerView.ViewHolder {

        private Integer sentenceId;
        private TextView tvEnglish;
        private TextView tvTranslation;
        private boolean translation = false;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvEnglish = view.findViewById(R.id.tv_english);
            tvTranslation = view.findViewById(R.id.tv_translation);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(sentenceId);
                }
            });

        }
    }

    //标题ViewHolder
    public class ViewHolderTitle extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAddTime;

        public ViewHolderTitle(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            tvAddTime = view.findViewById(R.id.tv_add_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer sentenceId);
    }
}
