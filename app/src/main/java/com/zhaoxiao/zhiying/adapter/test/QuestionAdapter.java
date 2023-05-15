package com.zhaoxiao.zhiying.adapter.test;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<QuestionM> list;
    private boolean share = false;

    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListenerShare mOnItemClickListenerShare;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListenerShare(OnItemClickListenerShare onItemClickListenerShare) {
        mOnItemClickListenerShare = onItemClickListenerShare;
    }

    public void setList(List<QuestionM> list) {
        this.list = list;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public QuestionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public QuestionAdapter(Context mContext, List<QuestionM> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        QuestionM question = list.get(position);
        viewHolder.tvTitle.setText(Html.fromHtml(question.getInfo()));
        viewHolder.tvType.setText(question.getSubType());
        viewHolder.questionId = question.getId();
        viewHolder.question = question;
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

        private Integer questionId;
        private QuestionM question;
        private TextView tvTitle;
        private RoundButton tvType;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            tvType = view.findViewById(R.id.tv_type);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (share){
                        mOnItemClickListenerShare.onItemClick(question);
                    } else {
                        mOnItemClickListener.onItemClick(questionId);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer questionId);
    }

    public interface OnItemClickListenerShare {
        void onItemClick(QuestionM question);
    }
}
