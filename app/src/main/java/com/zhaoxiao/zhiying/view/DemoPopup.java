package com.zhaoxiao.zhiying.view;

import android.content.Context;

import com.zhaoxiao.zhiying.R;

import razerdp.basepopup.BasePopupWindow;

public class DemoPopup extends BasePopupWindow {
    public DemoPopup(Context context) {
        super(context);
        setContentView(R.layout.layout_popup);
        // 设置View，建议使用createPopupById()，使BasePopup能正确读取xml参数
        // setContentView(createPopupById(R.layout.popup_normal));
    }

}