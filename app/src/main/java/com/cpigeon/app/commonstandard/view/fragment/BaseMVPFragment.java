package com.cpigeon.app.commonstandard.view.fragment;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtils;
import com.cpigeon.app.utils.http.RestErrorInfo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenshuai on 2017/4/15.
 */

public abstract class BaseMVPFragment<Pre extends BasePresenter> extends BaseFragment {

    protected Pre mPresenter;

    protected final CompositeDisposable composite = new CompositeDisposable();

    protected SweetAlertDialog errorDialog;
    private BaseFragment baseFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseFragment = this;
        mPresenter = this.initPresenter();
        bindError();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract Pre initPresenter();

    protected void bindError(){
        if(mPresenter != null){
            bindData(mPresenter.getError(), o -> {
                RestErrorInfo error = (RestErrorInfo) o;
                if (error!=null) {
                    error(error.code,error.message);
                }
            });
        }

    }

    protected void error(String message) {
        hideLoading();
        baseActivity.error(message);
    }

    public void error(int code, String error) {
        if (code == 2400||code==2401) {
            hideLoading();
            finish();
            return;
        }
        error(error);
    }


    /**
     * 是否可以释放Presenter与View 的绑定
     *
     * @return
     */
    protected abstract boolean isCanDettach();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isCanDettach() && mPresenter != null && mPresenter.isAttached())
            mPresenter.detach();
        composite.clear();
    }

    protected void addItemDecorationLine(RecyclerView recyclerView){
        addItemDecorationLine(recyclerView, R.color.color_line, ScreenTool.dip2px(0.5f), 0);
    }

    protected void addItemDecorationLine(RecyclerView recyclerView, @ColorRes int color, int size, int margin){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(color).size(size).margin(ScreenTool.dip2px(margin)).build());
    }

    protected void addItemDecorationLine(RecyclerView recyclerView, @ColorRes int color, int size){
        addItemDecorationLine(recyclerView, color, size, 0);
    }



    public <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        composite.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                        throwable -> {
                                ToastUtils.showLong(getActivity(), throwable.getMessage());
                        }
                ));
    }

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

}
