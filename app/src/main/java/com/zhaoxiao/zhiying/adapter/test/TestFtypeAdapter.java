package com.zhaoxiao.zhiying.adapter.test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.StypeAdapter;
import com.zhaoxiao.zhiying.entity.study.Ftype;
import com.zhaoxiao.zhiying.entity.test.TestFtype;

import java.util.List;

public class TestFtypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TestFtype> list;

    private OnItemClickListener mOnItemClickListener;

    private TestStypeAdapter stypeAdapter;
    private LinearLayoutManager linearLayoutManager;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<TestFtype> list) {
        this.list = list;
    }

    public TestFtypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TestFtypeAdapter(Context mContext, List<TestFtype> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_test_ftype_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //给控件对象赋值，给ViewHoleder绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TestFtype ftype = list.get(position);
        viewHolder.tvName.setText(ftype.getName());
        viewHolder.ftypeId = ftype.getId();
        viewHolder.ftype = ftype;
        viewHolder.position = position;
        viewHolder.tvNum.setText("共"+ftype.getNum()+"题  |  已练习"+ftype.getFinish()+"题");
        viewHolder.tvStype.setText(ftype.stypeToString());

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        viewHolder.rv.setLayoutManager(linearLayoutManager);
        stypeAdapter = new TestStypeAdapter(mContext);
//        stypeAdapter = new TestStypeAdapter(mContext,ftype.getStypeList());
        viewHolder.stypeAdapter = stypeAdapter;
        viewHolder.rv.setAdapter(stypeAdapter);
        stypeAdapter.setOnItemClickListener(stypeId -> mOnItemClickListener.onStypeClick(viewHolder.ftypeId,stypeId));

//        if(ftype.isExpand()){
//            viewHolder.rv.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.rv.setVisibility(View.GONE);
//        }
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
        private TestFtype ftype;
        private RecyclerView rv;
        private TextView tvName;

        private Integer position;
        private TestStypeAdapter stypeAdapter;
        private ImageView ivImg;
        private TextView tvStype;
        private TextView tvNum;

        public ViewHolder(@NonNull View view) {
            super(view);
            rv = view.findViewById(R.id.rv);
            tvName = view.findViewById(R.id.tv_name);
            ivImg = view.findViewById(R.id.iv_img);
            tvStype = view.findViewById(R.id.tv_stype);
            tvNum = view.findViewById(R.id.tv_num);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(ftypeId);
                    TestFtype ftype = list.get(position);
//                    ftype.setExpand(!ftype.isExpand());
//                    if(ftype.isExpand()){
//                        ftype.setExpand(false);
//                        ivImg.setImageResource(R.drawable.dropdown_gray);
//                    } else {
//                        ftype.setExpand(true);
//                        ivImg.setImageResource(R.drawable.pullup_gray);
//                    }
//                    notifyDataSetChanged();

                    if(ftype.isExpand()){
                        ftype.setExpand(false);
                        stypeAdapter.setList(null);
                        ivImg.setImageResource(R.drawable.dropdown_gray);
                        tvStype.setVisibility(View.VISIBLE);
                    } else {
                        ftype.setExpand(true);
                        stypeAdapter.setList(ftype.getStypeList());
                        ivImg.setImageResource(R.drawable.pullup_gray);
                        tvStype.setVisibility(View.GONE);
                    }
                    stypeAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int ftypeId);

        void onStypeClick(int ftypeId,int stypeId);
    }
}
