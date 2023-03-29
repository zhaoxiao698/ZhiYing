package com.zhaoxiao.zhiying.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class FixedViewPager extends ViewPager {

    private boolean scrollable = true;

    public FixedViewPager(@NonNull Context context) {
        super(context);
    }

    public FixedViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollable) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
