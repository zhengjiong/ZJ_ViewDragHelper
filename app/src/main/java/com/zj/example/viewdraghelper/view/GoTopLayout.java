package com.zj.example.viewdraghelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.zj.example.viewdraghelper.R;

/**
 * Created by zj on 2016/11/2.
 */

public class GoTopLayout extends ViewGroup {
    private ViewGroup topView;
    private ViewGroup bottomView;

    public GoTopLayout(Context context) {
        super(context);
    }

    public GoTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        System.out.println("onLayout");
        if (topView == null) {
            topView = (ViewGroup) findViewById(R.id.top);
            bottomView = (ViewGroup) findViewById(R.id.bottom);
        }
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
                float offset = event.getY() - mLastY;
                if (getScrollY() < 0) {
                    offset = 0;
                }
                scrollBy(0, (int) offset);
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
