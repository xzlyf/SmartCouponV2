package com.xz.jlw2.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class SlideViewPage extends ViewPager {
    private boolean slide = true;

    public SlideViewPage(@NonNull Context context) {
        super(context);
    }

    public SlideViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否能左右滑动
     * 默认能滑动
     *
     * @param b
     */
    public void canSlide(boolean b) {
        slide = b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (slide) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

    }
}
