package com.zj.example.viewdraghelper.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zj.example.viewdraghelper.R;

/**
 * Created by zhengjiong on 14/12/29.
 */
public class SlidingLayout extends LinearLayout{
    private ViewDragHelper mViewDragHelper;

    private View mDragView;

    public SlidingLayout(Context context) {
        super(context);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDragView = findViewById(R.id.dragview);


        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View view, int i) {
                //要捕获的view
                return mDragView == view;
            }

            /**
             * 上下滑動
             * @param child
             * @param top
             * @param dy
             * @return
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();//頂部邊界
                int bottomBound = getMeasuredHeight() - mDragView.getMeasuredHeight() - getPaddingBottom();//下邊界

                //讓DragView滑動不超出邊界
                return Math.min(Math.max(topBound, top), bottomBound);
            }

            /**
             * 水平滑動
             * @param child
             * @param left
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //Log.i("zj", "left = " + left + " , dx = " + dx);
                int leftBound = getPaddingLeft();//左邊界
                int rightBound = getMeasuredWidth() - mDragView.getMeasuredWidth() - getPaddingRight();//右邊界

                //讓DragView滑動不超出邊界
                return Math.min(Math.max(leftBound, left), rightBound);
            }

        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int top, int r, int b) {
        super.onLayout(changed, l, top, r, b);
        /**
         * clampViewPositionVertical,和clampViewPositionHorizontal
         * 不会触发onLayout
         */
        System.out.println("onLayout top=" + top);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

}
