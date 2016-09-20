package com.zj.example.viewdraghelper;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zj on 2016/9/20.
 */
public class VDHLayout extends LinearLayout {

    private ViewDragHelper mDragHelper;

    public VDHLayout(Context context) {
        super(context);
    }

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);


        /**
         * ViewDragHelper中拦截和处理事件时，需要会回调CallBack中的很多方法来决定一些事，
         * 比如：哪些子View可以移动、对个移动的View的边界的控制等等。
         */
        mDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
                return true;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            /**
             * clampViewPositionHorizontal,clampViewPositionVertical可以在该方法中对child移动的边界进行控制，
             * left , top 分别为即将移动到的位置，比如横向的情况下，我希望只在ViewGroup的内部移动，
             * 即：最小>=paddingleft，最大<=ViewGroup.getWidth()-paddingright-child.getWidth
             * @param child
             * @param left
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId)
            {
                //mDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //通过使用shouldInterceptTouchEvent(event)来决定我们是否应该拦截当前的事件
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //onTouchEvent中通过processTouchEvent(event)处理事件。
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
