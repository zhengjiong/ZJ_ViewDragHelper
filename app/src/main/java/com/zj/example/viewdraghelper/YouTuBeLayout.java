package com.zj.example.viewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 *
 * Created by zhengjiong on 14/12/29.
 */
public class YouTuBeLayout extends LinearLayout{
    private int mTop;
    private int mDragRange;
    private float mDragOffset;

    private View mHeadView;

    private ViewDragHelper mViewDragHelper;

    public YouTuBeLayout(Context context) {
        this(context, null);
    }

    public YouTuBeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTuBeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.mHeadView = findViewById(R.id.header);

        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View view, int i) {
                return mHeadView == view;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - mHeadView.getHeight() - mHeadView.getPaddingBottom();

                return Math.min(Math.max(topBound, top), bottomBound);
            }

            /**
             * 当View 的位置发生变化时
             * @param changedView
             * @param left
             * @param top
             * @param dx
             * @param dy
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                //Log.i("zj", "top=" + top);
                mTop = top;
                mDragOffset = (float) top / mDragRange;
                //requestLayout();//这里可以不加,不知道vhc demo为什么要加这个
            }

            /**
             * 当触摸释放后操作
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                Log.i("zj", "released yvel=" + yvel);
                int top = getPaddingTop();
                if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                    top += mDragRange;
                }
                /**
                 * 区别在于settleCapturedViewAt()会以最后松手前的滑动速率为初速度将View滚动到最终位置，
                 * 而smoothSlideViewTo()滚动的初速度是0。
                 */
                //mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                mViewDragHelper.smoothSlideViewTo(releasedChild, releasedChild.getLeft(), top);
                invalidate();
            }

            /**
             * 可以拖动的范围(不重写也可以, 但是重写后滑动更快跟平滑一些)
             * @param child
             * @return
             */
            @Override
            public int getViewVerticalDragRange(View child) {
                return mDragRange;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //Log.i("zj", "onLayout mTop" + mTop);

        mDragRange = getMeasuredHeight() - mHeadView.getMeasuredHeight();

        mHeadView.layout(0, mTop, r, mTop + mHeadView.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

//        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
//                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/
}
