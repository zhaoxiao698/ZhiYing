package com.zhaoxiao.zhiying.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhaoxiao.zhiying.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class CardRvAdapter extends RecyclerView.Adapter<CardRvAdapter.ItemViewHolder> {
    private List<Integer> list;
    private Context context;

    public CardRvAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
