package com.cpigeon.app.modular.matchlive.view;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Zhu TingYu on 2018/5/4.
 */

public class LineChart$ScrollingViewBehavior extends  CoordinatorLayout.Behavior<RelativeLayout> {

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return type == ViewCompat.SCROLL_AXIS_HORIZONTAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,type);
        if(Math.abs(dxConsumed) <= child.getWidth()){
            ViewCompat.offsetLeftAndRight(child, dxConsumed);
        }
    }
}
