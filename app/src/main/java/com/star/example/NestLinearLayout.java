package com.star.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
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
    private float offsetX = 0;//滑动的x轴距离
    private float offsetY = 0;//滑动的y轴距离
    private float downX;//点击的x坐标点
    private float downY;//点击的y坐标点
    private float secondY = 0;
    private int pagingTouchSlop;
    private boolean preventForHorizontal = false;

    public NestLinearLayout(Context context) {
        this(context, null);
    }

    public NestLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pagingTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2;
    }
    /**
     * 45
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                secondY = second.getTop();
                preventForHorizontal = false;
                Log.e(TAG, "MotionEvent.ACTION_DOWN boolean == " + (downY >= second.getTop()));
                super.dispatchTouchEvent(ev);
                return downY >= second.getTop();
            case MotionEvent.ACTION_MOVE:
                offsetX = downX - ev.getX();
                offsetY = downY - ev.getY();
                if (!preventForHorizontal && (Math.abs(offsetX) > pagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))) {
                    Log.e(TAG, "MotionEvent.ACTION_MOVE  , preventForHorizontal = true");
                    preventForHorizontal = true;
                }
                if (preventForHorizontal) {
                    return super.dispatchTouchEvent(ev);
                }
                if (offsetY > getMeasuredHeight()) {
                    offsetY = getMeasuredHeight();
                    return super.dispatchTouchEvent(ev);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        first = getChildAt(0);
        second = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (second.getTop() <= 0 && offsetY > 0) { //滑到顶部
            second.layout(0, 0, second.getMeasuredWidth(), second.getMeasuredHeight());
        } else if (offsetY <= 0 && second.getTop() - first.getTop() >= first.getMeasuredHeight()) {
            super.onLayout(changed, l, t, r, b);
        } else if (second.getTop() > 0) {
            second.layout(0, (int) secondY - (int) offsetY, second.getMeasuredWidth(), (int) secondY - (int) offsetY + second.getMeasuredHeight());
        } else if (offsetY <= 0 && second.getTop() < first.getBottom()) {
            second.layout(0, (int) secondY - (int) offsetY, second.getMeasuredWidth(), (int) secondY - (int) offsetY + second.getMeasuredHeight());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }
}
