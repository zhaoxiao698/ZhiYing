package com.zhaoxiao.zhiying.util;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

public class DividerItemDecoration extends Y_DividerItemDecoration {

        public DividerItemDecoration(Context context) {
            super(context);
        }

        @Override
        public Y_Divider getDivider(int itemPosition) {
            Y_Divider divider = null;
            switch (itemPosition % 3) {
                case 0:
                case 1:
                    divider = new Y_DividerBuilder()
                            .setRightSideLine(true, 0xff666666, 1, 0, 0)
                            .setTopSideLine(true, 0xffffffff, 7, 0, 0)
                            .setBottomSideLine(true, 0xffffffff, 7, 0, 0)
                            .create();
                    break;
                case 2:
                    divider = new Y_DividerBuilder()
                            .setTopSideLine(true, 0xffffffff, 7, 0, 0)
                            .setBottomSideLine(true, 0xffffffff, 7, 0, 0)
                            .create();
                    break;
                default:
                    break;
            }
            return divider;
        }
    }