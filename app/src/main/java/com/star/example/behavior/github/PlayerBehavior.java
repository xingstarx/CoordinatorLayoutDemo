package com.star.example.behavior.github;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.star.example.R;
import com.star.example.behavior.helper.ViewOffsetBehavior;

import java.util.List;


public class PlayerBehavior extends ViewOffsetBehavior {
    private static final String TAG = "PlayerBehavior";
    private Context mContext;


    public PlayerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        Log.d(TAG, "layoutChild:top" + child.getTop() + ",height" + child.getHeight());
        super.layoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        //拦截垂直方向上的滚动事件且当前状态是打开的并且还可以继续向上收缩
        Log.e(TAG, "onStartNestedScroll");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        // consumed the flinging behavior until Closed
        Log.e(TAG, "onNestedPreFling");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handleActionUp(parent, child);
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private void handleActionUp(CoordinatorLayout parent, View child) {

    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        //dy>0 scroll up;dy<0,scroll down
        float halfOfDis = dy / 4.0f; //消费掉其中的4分之1，不至于滑动效果太灵敏
        if (!canScroll(child, halfOfDis)) {
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
        }
        //只要开始拦截，就需要把所有Scroll事件消费掉
        consumed[1] = dy;
    }

    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        if (pendingTranslationY >= getHeaderOffsetRange() && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }

    //Header偏移量
    private int getHeaderOffsetRange() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset);
    }


}
