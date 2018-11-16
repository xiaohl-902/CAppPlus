package com.cpigeon.app.commonstandard.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BaseFragment extends Fragment implements IView {
    private View parentView;

    protected BaseActivity baseActivity;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    private Unbinder bind;
    /**
     * 加载中--对话框
     */
    protected SweetAlertDialog mLoadingSweetAlertDialog;

    protected Toolbar toolbar;

    protected final CompositeDisposable composite = new CompositeDisposable();

    protected SwipeRefreshLayout refreshLayout;

    protected TextView titleView;

    protected boolean isBack = true;

    private RecyclerView recyclerView;

    protected RxPermissions mRxPermission;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(getLayoutResource(), container, false);
            baseActivity = (BaseActivity) getSupportActivity();
        }
        ViewGroup parent = (ViewGroup) parentView.getParent();
        if (parent != null) {
            parent.removeView(parentView);
        }
        return parentView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.baseActivity = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            isBack  = getArguments().getBoolean(IntentBuilder.KEY_BOOLEAN);
        }
        bind = ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);
        titleView = view.findViewById(R.id.toolbar_title);
        if (toolbar == null) {
            toolbar = getActivity().findViewById(R.id.toolbar);
            titleView = getActivity().findViewById(R.id.toolbar_title);
        }
        if (toolbar != null && isBack) {
            toolbar.setNavigationOnClickListener(v -> {
                getActivity().finish();
            });
        }
        refreshLayout = view.findViewById(R.id.swipeLayout);
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
            refreshLayout.setEnabled(false);
        }

        try {
            recyclerView = findViewById(R.id.list);
        }catch (Throwable e){

        }

        if(recyclerView != null){
            ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        finishCreateView(savedInstanceState);
    }
    public RxPermissions getRxPermission() {
        if (mRxPermission == null)
            mRxPermission = new RxPermissions(getActivity());
        return mRxPermission;
    }

    /**
     * 检查权限
     * permission 获取方式： Manifest.permission.CAMERA
     */
    protected void checkAppPermission(String permission, Consumer<? super Boolean> onNext) {
        bindUi(getRxPermission().request(permission), onNext);
    }

    protected void setToolbarNotBack() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
    }

    public void setTitle(@StringRes int resId) {
        if (null != toolbar) {
            if (titleView != null) {
                titleView.setText(resId);
            }
        }
    }

    public void setTitle(String resId) {
        if (null != toolbar) {
            if (titleView != null) {
                titleView.setText(resId);
            }
        }
    }

    public void setProgressVisible(boolean isVisible) {
        baseActivity.setProgressVisible(isVisible);
    }

    public abstract void finishCreateView(Bundle state);

    public Context getApplicationContext() {

        return this.baseActivity == null
                ? (getActivity() == null ? null :
                getActivity().getApplicationContext())
                : this.baseActivity.getApplicationContext();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {

        lazyLoad();
    }

    protected void lazyLoad() {
    }


    protected void onInvisible() {
    }


    protected void loadData() {
    }


    //获取布局文件
    @LayoutRes
    protected abstract int getLayoutResource();

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (!getActivity().isFinishing()) {
            SweetAlertDialog dialogPrompt;
            switch (tipType) {
                case Dialog:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dialogPrompt.setTitleText(getString(R.string.prompt))
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case DialogSuccess:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialogPrompt.setTitleText("成功")
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case DialogError:
                    dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    dialogPrompt.setTitleText("失败")
                            .setContentText(tip)
                            .setConfirmText(getString(R.string.confirm)).show();
                    return true;
                case View:
                case ViewSuccess:
                case ViewError:
                    return false;
                case HINT:

                    return false;
                case LoadingShow:
                    if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                        mLoadingSweetAlertDialog.dismiss();
                    mLoadingSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    mLoadingSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    mLoadingSweetAlertDialog.setTitleText(tip);
                    mLoadingSweetAlertDialog.setCancelable(false);
                    mLoadingSweetAlertDialog.show();
                    return true;
                case LoadingHide:
                    if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                        mLoadingSweetAlertDialog.dismiss();
                    return true;
                case ToastLong:
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_LONG).show();
                    return true;
                case ToastShort:
                    Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }
        return false;

    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return tag == 0 && showTips(tip, tipType);
    }

    @Override
    public boolean checkLogin() {
        try {
            boolean res = (boolean) SharedPreferencesTool.Get(getActivity(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
            res &= SharedPreferencesTool.Get(getActivity(), "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN).equals(
                    Integer.valueOf(EncryptionTool.decryptAES(SharedPreferencesTool.Get(getActivity(), "token", "", SharedPreferencesTool.SP_FILE_LOGIN).toString()).split("\\|")[0]));
            return res;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getLoginUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getString(R.string.user_name));
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("userid", 0);
        map.put("phone", "");
        return SharedPreferencesTool.Get(getActivity(), map, SharedPreferencesTool.SP_FILE_LOGIN);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        composite.clear();
    }

    protected <T extends View> T findViewById(@NonNull View view, @IdRes int resId) {
        T t = null;
        if (view != null)
            t = (T) view.findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    protected <T extends View> T findViewById(@IdRes int resId) {
        T t = null;
        if (getView() != null)
            t = getView().findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }

    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    protected void addItemDecorationLine(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(R.color.line_color).size(1).build());
    }

    protected <T> void bindUi(Observable<T> observable, Consumer<? super T> onNext) {
        composite.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                            ToastUtils.showLong(getActivity(), throwable.getMessage());
                        }
                ));
    }

    public void showLoading() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        } else {
            showLoading("请稍后...");
        }
    }

    public void setLoadText(String text){
        baseActivity.setLoadText(text);
    }

    protected void showLoading(String message) {
        baseActivity.showTips(message, TipType.LoadingShow);
    }

    public void hideLoading() {

        baseActivity.showTips("", TipType.LoadingHide);


        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    protected void finish() {
        getActivity().finish();
    }

    protected void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setOnRefreshListener(listener);
        }
    }


    // 隐藏软键盘
    protected void hideSoftInput(IBinder token) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(token,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //edittext默认不显示软键盘，只有edittext被点击时，软键盘才弹出

    protected void hideSoftInput() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
