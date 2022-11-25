package com.xlong.libui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

	private boolean scroll = true;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScroll(boolean scroll) {
		this.scroll = scroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
        /*return false;//super.onTouchEvent(arg0);*/
        return scroll && super.onTouchEvent(arg0);
    }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return scroll && super.onInterceptTouchEvent(arg0);
    }
}
