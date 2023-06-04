package com.zhaoxiao.zhiying.adapter.mine;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.test.TestNoteDetail;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class TestNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TestNoteDetail> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<TestNoteDetail> list) {
        this.list = list;
    }

    public TestNoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TestNoteAdapter(Context mContext, List<TestNoteDetail> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_test_note_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TestNoteDetail testNoteDetail = list.get(position);
        viewHolder.tvInfo.setText(testNoteDetail.getInfo());
        viewHolder.tvSubType.setText(testNoteDetail.getSubType());
        viewHolder.tvTitle.setText(Html.fromHtml(testNoteDetail.getQuestionInfo()));
        viewHolder.tvTime.setText(StringUtils.formatDateTime(testNoteDetail.getAddTime()));
        viewHolder.questionId = testNoteDetail.getQuestionId();
        viewHolder.testNoteDetail = testNoteDetail;
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
        private TestNoteDetail testNoteDetail;
        private TextView tvInfo;
        private TextView tvTitle;
        private TextView tvSubType;
        private TextView tvTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvInfo = view.findViewById(R.id.tv_info);
            tvTitle = view.findViewById(R.id.tv_title);
            tvSubType = view.findViewById(R.id.tv_sub_type);
            tvTime = view.findViewById(R.id.tv_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(testNoteDetail);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TestNoteDetail testNoteDetail);
    }
}
