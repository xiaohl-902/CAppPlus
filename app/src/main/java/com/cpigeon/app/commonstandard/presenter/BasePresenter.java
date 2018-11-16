package com.cpigeon.app.commonstandard.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.utils.WeakHandler;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.cpigeon.app.utils.http.RestErrorInfo;

import org.xutils.common.Callback;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BasePresenter<TView extends IView, TDao extends IBaseDao> {

    protected TView mView;

    protected TDao mDao;

    private WeakHandler mHandler;

    private WeakHashMap<String, Callback.Cancelable> mCancelableWeakHashMap;

    protected final CompositeDisposable composite = new CompositeDisposable();
    protected final BehaviorSubject<RestErrorInfo> error = BehaviorSubject.<RestErrorInfo>create();

    Activity activity;

    BaseFragment baseFragment;

    public BasePresenter(TView mView) {
        onAttach();
        this.mView = mView;
        onAttached();
        mCancelableWeakHashMap = new WeakHashMap<>();
        this.mHandler = new WeakHandler();
        mDao = initDao();
    }

    public BasePresenter(Activity activity) {
        onAttach();
        this.mView = mView;
        onAttached();
        mCancelableWeakHashMap = new WeakHashMap<>();
        this.mHandler = new WeakHandler();
        mDao = initDao();
        this.activity = activity;
    }

    public BasePresenter(BaseFragment baseFragment) {
        onAttach();
        this.mView = mView;
        onAttached();
        mCancelableWeakHashMap = new WeakHashMap<>();
        this.mHandler = new WeakHandler();
        mDao = initDao();
        this.baseFragment = baseFragment;
    }

    protected abstract TDao initDao();

    /**
     * 是否已绑定视图
     *
     * @return
     */
    public boolean isAttached() {
        return mView != null;
    }

    /**
     * 是否已释放视图
     *
     * @return
     */
    public boolean isDetached() {
        return mView == null;
    }

    /**
     * 解除绑定，释放视图的引用
     */
    public void detach() {
        onDetach();
        this.mView = null;
        this.mHandler = null;
//        if (mDao != null)
//        {
//            mDao = null;
//        }
        if (mCancelableWeakHashMap != null){
            for (String key : mCancelableWeakHashMap.keySet()) {
                Callback.Cancelable cancelable = mCancelableWeakHashMap.get(key);
                if (cancelable != null && !cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        }

        onDestroy();

        onDetached();
    }

    /**
     * 添加需要在解绑视图时取消请求的Cancelable
     * <p>key若在Map中存在，将会取消Map中的Cancelable，并将新的Cancelable放入Map中</p>
     *
     * @param key
     */
    public void addCancelableIntoMap(String key, Callback.Cancelable cancelable) {
        if (mCancelableWeakHashMap.containsKey(key)) {
            Callback.Cancelable temp = mCancelableWeakHashMap.get(key);
            if (temp != null && !temp.isCancelled()) {
                temp.cancel();
            }
            mCancelableWeakHashMap.remove(key);
        }
        mCancelableWeakHashMap.put(key, cancelable);
    }

    /**
     * 绑定视图之前
     */
    public void onAttach() {
    }

    /**
     * 绑定视图之后
     */
    public void onAttached() {
    }

    /**
     * 解绑视图之前
     */
    public void onDetach() {
    }

    /**
     * 解绑视图之后
     */
    public void onDetached() {
    }

    /**
     * 执行Handler.post
     *
     * @param r
     */
    public void post(@NonNull CheckAttachRunnable r) {
        if (mHandler == null || r == null || isDetached()) return;
        mHandler.post(r);
    }

    /**
     * 执行Handler.postDelayed
     *
     * @param r
     * @param delayMillis
     */
    public void postDelayed(@NonNull CheckAttachRunnable r, long delayMillis) {
        if (mHandler == null || r == null || isDetached()) return;
        mHandler.postDelayed(r, delayMillis);
    }

    /**
     * 自动检查Presenter与视图的绑定情况的Runnable
     */
    public abstract class CheckAttachRunnable implements Runnable {
        @Override
        public void run() {
            if (isAttached()) {
                runAttached();
            } else {
                runDetached();
            }
        }

        /**
         * 绑定情况下
         */
        protected abstract void runAttached();

        /**
         * 解绑或未绑定情况下
         */
        protected void runDetached() {
        }
    }

    public <T> void submitRequestThrowError(Observable<T> observable, final Consumer<? super T> onNext) {
            composite.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable))));
    }

    public RestErrorInfo getError(Throwable throwable) {
        if (throwable instanceof HttpErrorException) {
            return new RestErrorInfo(((HttpErrorException) throwable).getResponseJson());
        }

        if(throwable instanceof ConnectException) return new RestErrorInfo("网络不给力，请稍后重试！");
        if(throwable instanceof SocketTimeoutException) return new RestErrorInfo("网络不给力，请稍后重试！");

        if (throwable != null) return new RestErrorInfo(throwable.getMessage());
        return new RestErrorInfo("");
    }

    public RestErrorInfo getErrorString(@StringRes int r) {
        if (mView != null) {
            return new RestErrorInfo(MyApp.getInstance().getBaseContext().getString(r));
        }
        return new RestErrorInfo("");
    }

    public RestErrorInfo getErrorString(String r) {
        return new RestErrorInfo(r);
    }

    public <T> void submitRequest(Observable<T> observable, final Consumer<? super T> onNext, final Consumer<Throwable> onError) {

            composite.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));

    }


    public void onDestroy(){
        composite.clear();
    }

    public BehaviorSubject<RestErrorInfo> getError() {
        return error;
    }

    public void clearError() {
        this.error.onNext(null);
    }

    public Activity getActivity() {
        return activity;
    }

    public BaseActivity getBaseActivity() {
       if(baseFragment == null){
           return (BaseActivity) getActivity();
       }else {
           return (BaseActivity) baseFragment.getActivity();
       }
    }

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

    public String getString(@StringRes int resId){
        return MyApp.getInstance().getBaseContext().getResources().getString(resId);
    }
}