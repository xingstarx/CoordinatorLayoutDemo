package com.star.example;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by dell on 2016/11/15.
 */

public class NestLinearLayout extends LinearLayout {
    private static final String TAG = "NestFrameLayout";
    private View first;
    private View second;
    private boolean canPull = false;//是否向上拉动
    private boolean isLayout = false;//是否第一次Layout
    private float moveDeltaY = 0;//滑动的距离
    private float downY;//点击的y坐标点
    private float secondY = 0;

    public NestLinearLayout(Context context) {
        super(context);
    }

    public NestLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NestLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 45
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                secondY = second.getTop();
                Log.e(TAG, "MotionEvent.ACTION_DOWN boolean == " + (downY >= second.getTop()));
                super.dispatchTouchEvent(ev);
                return downY >= second.getTop();
            case MotionEvent.ACTION_MOVE:
                moveDeltaY = downY - ev.getY();
                if (moveDeltaY > getMeasuredHeight()) {
                    moveDeltaY = getMeasuredHeight();
                }
                requestLayout();
                Log.e(TAG, "MotionEvent.ACTION_MOVE ");
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "MotionEvent.ACTION_UP or ACTION_CANCEL ");
                return super.dispatchTouchEvent(ev);
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            first = getChildAt(0);
            second = getChildAt(1);
            isLayout = true;
        }
        if (second.getTop() <= 0 && moveDeltaY > 0) { //滑到顶部
            second.layout(0, 0, second.getMeasuredWidth(), second.getMeasuredHeight());
        } else if (moveDeltaY <= 0 && second.getTop() - first.getTop() >= first.getMeasuredHeight()) {
            super.onLayout(changed, l, t, r, b);
        } else if (second.getTop() > 0) {
            second.layout(0, (int) secondY - (int) moveDeltaY, second.getMeasuredWidth(), (int) secondY - (int) moveDeltaY + second.getMeasuredHeight());
        } else if (moveDeltaY <= 0 && second.getTop() < first.getBottom()) {
            second.layout(0, (int) secondY - (int) moveDeltaY, second.getMeasuredWidth(), (int) secondY - (int) moveDeltaY + second.getMeasuredHeight());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }
}
