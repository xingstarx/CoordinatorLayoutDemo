package com.star.example.behavior.github;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.star.example.R;
import com.star.example.behavior.helper.HeaderScrollingViewBehavior;

import java.lang.ref.WeakReference;
import java.util.List;


public class ViewPagerBehavior extends HeaderScrollingViewBehavior {
    public static final String TAG = "ViewPagerBehavior";

    private OverScroller mOverScroller;
    private Context mContext;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;

    public ViewPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(mContext);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        Log.d(TAG, "layoutChild");
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<>(parent);
        mChild = new WeakReference<>(child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll: ");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent");
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        child.setTranslationY(target.getTranslationY());
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY((int) (-dependency.getTranslationY() / (getHeaderOffsetRange() * 1.0f) * getScrollRange(child)));
    }

    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - getFinalHeight());
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getHeaderOffsetRange() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset);
    }

    private int getFinalHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.uc_news_tabs_height) + mContext.getResources().getDimensionPixelOffset(R.dimen.uc_news_header_title_height);
    }

    //依赖的判断
    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.player;
    }
}
