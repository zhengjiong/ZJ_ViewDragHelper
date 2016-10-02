package com.zj.example.viewdraghelper.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zj.example.viewdraghelper.R;

/**
 * Created by zj on 2016/9/20.
 */
public class VDHLayout extends LinearLayout {

    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackView;

    private Point mAutoBackViewOriginPoint = new Point();


    public VDHLayout(Context context) {
        super(context);
    }

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * ViewDragHelper中拦截和处理事件时，需要会回调CallBack中的很多方法来决定一些事，
         * 比如：哪些子View可以移动、对个移动的View的边界的控制等等。
         */
        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
                if (child == mDragView || child == mAutoBackView) {
                    return true;
                }
                return false;
            }


            /**
             * 如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent，在onTouchEvent的
             * DOWN的时候就确定了captureView。如果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，
             * 而在判断的过程中会去判断另外两个回调的方法：getViewHorizontalDragRange和getViewVerticalDragRange，
             * 只有这两个方法返回大于0的值才能正常的捕获。
             * 所以，如果你用Button测试，或者给TextView添加了clickable = true ，都记得重写下面这两个方法：
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                System.out.println("getViewHorizontalDragRange return 1");
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                System.out.println("getViewVerticalDragRange return 1");
                return 1;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mAutoBackView) {
                    mViewDragHelper.smoothSlideViewTo(mAutoBackView, mAutoBackViewOriginPoint.x, mAutoBackViewOriginPoint.y);
                    invalidate();
                }
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
                /**
                 * 我们在onEdgeDragStarted回调方法中，主动通过captureChildView对其进行捕获，
                 * 该方法可以绕过tryCaptureView，所以我们的tryCaptureView虽然并为返回true，但却不影响。
                 * 注意如果需要使用边界检测需要添加上mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);。
                 */
                mViewDragHelper.captureChildView(mEdgeTrackView, pointerId);
            }
        });

        //如果需要使用边界检测需要添加上mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);。
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //onLayout中用来确定子view的位置, 谁有可以获取子view的left和top
        mAutoBackViewOriginPoint.x = mAutoBackView.getLeft();
        mAutoBackViewOriginPoint.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = findViewById(R.id.tv_1);
        mAutoBackView = findViewById(R.id.tv_2);
        mEdgeTrackView = findViewById(R.id.tv_3);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //通过使用shouldInterceptTouchEvent(event)来决定我们是否应该拦截当前的事件
        boolean intercept = mViewDragHelper.shouldInterceptTouchEvent(ev);
        String actionName = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionName = "action_down";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "action_move";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "action_up";
                break;
        }
        System.out.println("----->onInterceptTouchEvent intercept=" + intercept + " actionName=" + actionName);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //onTouchEvent中通过processTouchEvent(event)处理事件。
        mViewDragHelper.processTouchEvent(ev);
        String actionName = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionName = "action_down";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "action_move";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "action_up";
                break;
        }
        System.out.println("onTouchEvent return true , actionName=" + actionName);
        return true;
    }
}
