package com.zj.example.viewdraghelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zj.example.viewdraghelper.R;

/**
 * Created by zj on 2016/11/2.
 */

public class GoTopLayout extends ViewGroup {
    private ViewGroup topView;
    private ViewGroup bottomView;
    private Scroller mScroller;

    public GoTopLayout(Context context) {
        this(context, null);
    }

    public GoTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        System.out.println("onFinishInflate");
        topView = (ViewGroup) findViewById(R.id.top);
        bottomView = (ViewGroup) findViewById(R.id.bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println("============================onLayout====================================");
        topView.layout(0, 0, r, b);
        bottomView.layout(0, topView.getMeasuredHeight(), r, topView.getMeasuredHeight() + bottomView.getMeasuredHeight());
    }

    float mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offset = mLastY - event.getY();

                System.out.println("offset=" + offset);
                scrollBy(0, (int) offset);
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            System.out.println("computeScroll -> scrollTo=" + mScroller.getCurrY());
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void goBottom(){
        mScroller.startScroll(0, 0, 0, bottomView.getMeasuredHeight(), 1000);
        invalidate();
    }
    public void goTop(){
        System.out.println("goTop finalY=" + mScroller.getFinalY());
        mScroller.startScroll(0, mScroller.getFinalY(), 0, -mScroller.getFinalY(), 1000);
        invalidate();
    }
}
