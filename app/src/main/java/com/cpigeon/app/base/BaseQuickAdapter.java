package com.cpigeon.app.base;


import android.animation.Animator;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.http.CommonUitls;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/11.
 */

public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends com.chad.library.adapter.base.BaseQuickAdapter<T, K> {

    private View.OnClickListener OnClickListener = null;

    public BaseQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
        setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.item_adpter_load_more_layout;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.load_more;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.load_more;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_more;
            }
        });

    }



    public void setLoadMore(boolean isEnd) {
        if (isEnd) {
            this.loadMoreEnd();
        }
        else this.loadMoreComplete();
    }

    @Override
    public void setNewData(List<T> data) {
        if(getRecyclerView() != null){
            getRecyclerView().getRecycledViewPool().clear();
            notifyDataSetChanged();
            ((DefaultItemAnimator) getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);
        }

        if (data == null || data.isEmpty()) {
            if(CommonTool.isNetworkConnected(MyApp.getInstance().getBaseContext())){
                if (!getEmptyViewText().isEmpty()) {
                    setEmptyView();
                }
            }else {
                CommonTool.setEmptyView(this, OnClickListener);
            }
        }


        super.setNewData(data);
    }

    public void setEmptyView() {
        if (getEmptyViewImage() == 0) {
            CommonTool.setEmptyView(this, getEmptyViewText());
        } else {
            CommonTool.setEmptyView(this, getEmptyViewImage(), getEmptyViewText());
        }
    }

    protected String getEmptyViewText() {
        return "";
    }


    protected @DrawableRes int getEmptyViewImage() {
        return 0;
    }

    protected int getColor(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }

    protected float getDimension(@DimenRes int resId) {
        return mContext.getResources().getDimension(resId);
    }

    protected BaseActivity getBaseActivity(){
        return (BaseActivity) mContext;
    }

    public void setOnNotNetClickListener(View.OnClickListener onClickListener) {
        OnClickListener = onClickListener;
    }
}

