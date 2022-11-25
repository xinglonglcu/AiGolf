package com.xlong.libui.pullToRef;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlong.libui.R;

public class JsSwipeRefreshLayout extends SuperSwipeRefreshLayout {
    private float mPrevX; // 避免RecyclerView 滚动冲突

    private ImageView mIvRefresh;
    private TextView mTvDes;

    public JsSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public JsSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setHeaderViewBackgroundColor(0xffffffff);
        setHeaderView(createHeaderView());

    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.pull_to_refresh_header_swipe, null);

        mIvRefresh = (ImageView) headerView.findViewById(R.id.pull_to_refresh_image);
        Drawable imageDrawable = getResources().getDrawable(R.drawable.refresh_imageview);
        mIvRefresh.setImageDrawable(imageDrawable);

        mTvDes = (TextView) headerView.findViewById(R.id.pull_to_refresh_text);
        mTvDes.setText("下拉刷新");
        return headerView;
    }

    @Override
    public void onAnimStart() {
        super.onAnimStart();
        handleAnim(true);
        mTvDes.setText("正在载入");
    }

    @Override
    public void onAnimEnd() {
        super.onAnimEnd();
        mTvDes.setText("下拉刷新");
        handleAnim(false);
    }

    @Override
    public void onAgentPullEnable(boolean enable) {
        super.onAgentPullEnable(enable);
        mTvDes.setText(enable ? "放开刷新" : "下拉刷新");
    }

    private void handleAnim(boolean start) {
        boolean mUseIntrinsicAnimation = (mIvRefresh.getDrawable() instanceof AnimationDrawable);
        if (mUseIntrinsicAnimation) {
            if (start) {
                ((AnimationDrawable) mIvRefresh.getDrawable()).start();
            } else {
                ((AnimationDrawable) mIvRefresh.getDrawable()).stop();
                ((AnimationDrawable) mIvRefresh.getDrawable()).selectDrawable(0);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

}