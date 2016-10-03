package com.zj.example.viewdraghelper.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.zj.example.viewdraghelper.R;

/**
 * Title: LeftDrawerLayout
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/10/2  10:26
 *
 * @author 郑炯
 * @version 1.0
 */
public class LeftDrawerLayout extends ViewGroup {
    private View mMenuView;
    private View mContentView;

    private static final int MIN_FLING_VELOCITY = 1000; // dips per second

    private ViewDragHelper mDragHelper;

    public LeftDrawerLayout(Context context) {
        this(context, null);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = getResources().getDisplayMetrics().density;
        float minVel = MIN_FLING_VELOCITY * density;

        mDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mMenuView;
            }

            /**
             *
             * @param releasedChild The captured child view now being released
             *
             * (X方向加速度)
             * @param xvel X velocity of the pointer as it left the screen in pixels per second.
             *
             * (Y方向加速度)
             * @param yvel Y velocity of the pointer as it left the screen in pixels per second.
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //这里注意一点xvel的值只有大于我们设置的setMinVelocity才会出现大于0，如果小于我们设置的值则一直是0。
                System.out.println("xvel(x方向速度)=" + xvel);
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1f / childWidth;
                if (xvel > 2 || (xvel == 0 && offset > 0.5f)) {//offset>0.5意思是滑动超过一半
                    //mDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
                } else {
                    mDragHelper.settleCapturedViewAt(-childWidth, releasedChild.getTop());
                }
                invalidate();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > 0) {
                    left = 0;
                }
                return left;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragHelper.captureChildView(mMenuView, pointerId);
            }
        });
        //启用edge检测功能
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        //这里注意一点onViewReleased中xvel的值只有大于我们设置的setMinVelocity才会出现大于0，如果小于我们设置的值则一直是0。
        mDragHelper.setMinVelocity(minVel);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = findViewById(R.id.menu);
        mContentView = findViewById(R.id.content);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView == mContentView) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) mContentView.getLayoutParams();
                //必须用重写onMeasure方法, 不然contentView的内部的view会显示不出来
                mContentView.layout(
                        l + layoutParams.leftMargin,
                        t + layoutParams.topMargin,
                        l + layoutParams.leftMargin + mContentView.getMeasuredWidth(),
                        t + layoutParams.topMargin + mContentView.getMeasuredHeight()
                );
            } else if (childView == mMenuView) {
                //这样写就可以不重写onMeasure,因为用l-r确定了left,0确定了right
                //mMenuView.layout(l - r, t, 0, b);

                MarginLayoutParams layoutParams = (MarginLayoutParams) mContentView.getLayoutParams();

                //如果要用getMeasureWidth, 就必须重写onMeasure方法
                mMenuView.layout(l - mMenuView.getMeasuredWidth(), t, 0, b);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        /*if (true) {
            //这里可以使用measureChildren, 但是因为自己设置了mMinDrawerMargin(drawer离父容器右边的最小外边距),
            所以自己计算
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            return;
        }*/

        //因为layout总设置了width=320,所以这里可以通过LayoutParams来获得宽度
        final MarginLayoutParams lp = (MarginLayoutParams) mMenuView.getLayoutParams();

        //查看源码会发现getChildMeasureSpec的时候,内部会减去leftMargin值和rightMargin值
        //这里也可以使用MeasureSpec.makeMeasureSpec
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                lp.leftMargin + lp.rightMargin, lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin, lp.height);

        mMenuView.measure(drawerWidthSpec, drawerHeightSpec);

        //measureChild();
        MarginLayoutParams lp2 = (MarginLayoutParams) mContentView.getLayoutParams();

        int contentWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() -
                        getPaddingLeft() - getPaddingRight() - lp2.leftMargin - lp2.rightMargin,
                MeasureSpec.EXACTLY);

        int contentHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() -
                        getPaddingTop() - getPaddingBottom() - lp2.topMargin - lp2.bottomMargin,
                MeasureSpec.EXACTLY);

        mContentView.measure(contentWidthSpec, contentHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
