package com.cpigeon.app.utils.guide;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.view.guideview.Component;
import com.cpigeon.app.view.guideview.Guide;
import com.cpigeon.app.view.guideview.GuideBuilder;

/**
 * Created by Zhu TingYu on 2018/6/6.
 */

public class GuideManager {
    private static GuideManager guideManager;
    private Guide guide;
    GuideComponent guideComponent;
    private GuideBuilder builder;

    private GuideManager() {
        builder = new GuideBuilder();
        builder.setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        guideComponent = new GuideComponent();
        guideComponent.setOnOkClickListener(() -> {
            guide.dismiss();
        });
    }

    public static GuideManager get() {
        guideManager = new GuideManager();
        return guideManager;
    }

    public GuideManager setVisibilityChangedListener(GuideBuilder.OnVisibilityChangedListener onVisibilityChangedListener) {
        builder.setOnVisibilityChangedListener(onVisibilityChangedListener);
        return this;
    }

    public GuideManager setHintText(String hint) {
        guideComponent.setGuideHint(hint);
        return this;
    }

    public GuideManager setGuideLocation(int location){
        guideComponent.setGuideLocation(location);
        return this;
    }

    public GuideManager setViewLocation(int location){
        guideComponent.setViewLocation(location);
        return this;
    }

    public GuideManager setTagView(View view) {
        builder.setTargetView(view);
        return this;
    }

    public GuideManager setTagViewId(@IdRes int resId) {
        builder.setTargetViewId(resId);
        return this;
    }

    public void show(Activity activity) {
        builder.addComponent(guideComponent);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(activity);
    }

}
