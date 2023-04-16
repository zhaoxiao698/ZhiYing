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
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.ArticleAdapter;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.word.Book;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Book> list;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Book> list) {
        this.list = list;
    }

    public BookAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public BookAdapter(Context mContext, List<Book> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Book book = list.get(position);
        viewHolder.tvName.setText(book.getName());
        viewHolder.tvId.setText(book.getId());
        viewHolder.tvNum.setText("单词量："+book.getNum());
        Picasso.with(mContext)
                .load(book.getImg())
                .transform(new CircleCornerTransForm())
                .into(viewHolder.ivImg);
        viewHolder.bookId = book.getId();
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

        private String bookId;
        private ImageView ivImg;
        private TextView tvName;
        private TextView tvId;
        private TextView tvNum;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = view.findViewById(R.id.iv_img);
            tvName = view.findViewById(R.id.tv_name);
            tvId = view.findViewById(R.id.tv_id);
            tvNum = view.findViewById(R.id.tv_num);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(bookId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String bookId);
    }
}
