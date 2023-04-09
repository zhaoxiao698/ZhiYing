package com.zhaoxiao.zhiying.adapter.test;


import androidx.annotation.Nullable;

import com.ahao.basetreeview.adapter.SingleLayoutTreeAdapter;
import com.ahao.basetreeview.model.TreeNode;
import com.ahao.basetreeview.util.DpUtil;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.test.QuestionType;

import java.util.List;

public class MySingleLayoutTreeAdapter extends SingleLayoutTreeAdapter<QuestionType> {

    public MySingleLayoutTreeAdapter(int layoutResId, @Nullable List<TreeNode<QuestionType>> dataToBind) {
        super(layoutResId, dataToBind);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeNode<QuestionType> item) {
        super.convert(helper,item);
        helper.setText(R.id.textView, item.getId() + ":" + item.getData().getName() + " level=" + item.getLevel());
        if (item.isLeaf()) {
            helper.setImageResource(R.id.level_icon, R.drawable.video);
        } else {
            if (item.isExpand()) {
                helper.setImageResource(R.id.level_icon, R.drawable.tree_icon_collapse);
            } else {
                helper.setImageResource(R.id.level_icon, R.drawable.tree_icon_expand);
            }
        }

    }

    @Override
    protected int getTreeNodeMargin() {
        return  DpUtil.dip2px(this.mContext, 30);
    }
}
