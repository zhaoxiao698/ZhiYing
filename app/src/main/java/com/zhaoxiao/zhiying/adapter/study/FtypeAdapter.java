package com.zhaoxiao.zhiying.adapter.study;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.study.Ftype;

import java.util.List;

public class FtypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Ftype> list;

    private OnItemClickListener mOnItemClickListener;

    private StypeAdapter stypeAdapter;
    private GridLayoutManager gridLayoutManager;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Ftype> list) {
        this.list = list;
    }

    public FtypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public FtypeAdapter(Context mContext, List<Ftype> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ftype_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Ftype ftype = list.get(position);
        viewHolder.tvName.setText(ftype.getName());
        viewHolder.ftypeId = ftype.getId();
        viewHolder.ftype = ftype;

        gridLayoutManager = new GridLayoutManager(mContext, 3);
        viewHolder.rv.setLayoutManager(gridLayoutManager);
        stypeAdapter = new StypeAdapter(mContext, ftype.getStypeList());
        viewHolder.rv.setAdapter(stypeAdapter);
//        stypeAdapter.setOnItemClickListener(stypeId -> XToastUtils.toast(ftype.getId()));
//        stypeAdapter.setOnItemClickListener(stypeId -> XToastUtils.toast(ftype.getId()));
        stypeAdapter.setOnItemClickListener(stypeId -> mOnItemClickListener.onStypeClick(viewHolder.ftypeId,stypeId));

//        viewHolder.rv.addItemDecoration(new DividerItemDecoration(mContext));

        DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new AgileDividerLookup());
        viewHolder.rv.addItemDecoration(itemDecoration);
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

        private Integer ftypeId;
        private Ftype ftype;
        private RecyclerView rv;
        private TextView tvName;

        public ViewHolder(@NonNull View view) {
            super(view);
            rv = view.findViewById(R.id.rv);
            tvName = view.findViewById(R.id.tv_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(ftypeId);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int ftype);

        void onStypeClick(int ftype,int stypeId);
    }

    private static class AgileDividerLookup implements DividerItemDecoration.DividerLookup {
        @Override
        public Divider getVerticalDivider(int position) {
            return new Divider.Builder()
                    .margin(25,25)
                    .size(5)
                    .color(0xFFFBBC05)
                    .build();
        }

        @Override
        public Divider getHorizontalDivider(int position) {
            return new Divider.Builder()
                    .size(0)
                    .color(Color.RED)
                    .build();
        }
    }
}
