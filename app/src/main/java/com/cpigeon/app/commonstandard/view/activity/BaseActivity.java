package com.cpigeon.app.commonstandard.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.StatusBarSetting;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.ToastUtils;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.utils.http.RestErrorInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.UMShareAPI;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2017/4/5.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IView{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    protected SweetAlertDialog errorDialog;
    protected final CompositeDisposable composite = new CompositeDisposable();

    public Context mContext;
    private Unbinder mUnbinder;
    private WeakReference<AppCompatActivity> weakReference;
    protected SweetAlertDialog dialogPrompt;
    protected T mPresenter;


    public Toolbar toolbar;
    protected TextView tvTitle;

    ViewGroup rootView;
    protected View progressView;

    //AppBarLayout appBarLayout;
    /**
     * 加载中--对话框
     */
    protected SweetAlertDialog mLoadingSweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        doBeforeSetcontentView();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.toolbar_title);
        setToolbar();
        mPresenter = this.initPresenter();
        bindError();
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
        initView(savedInstanceState);

    }

    private void initProgressLayout() {
        if(progressView == null){
            progressView = getLayoutInflater().inflate(R.layout.load_layout, rootView
                    , false);
            GifImageView imageView = progressView.findViewById(R.id.load_image);
            imageView.setImageResource(R.drawable.load);
        }
        setProgressVisible(false);
        rootView.addView(progressView);
    }

    public void setLoadText(String text){
        TextView textView = progressView.findViewById(R.id.load_text);
        textView.setText(text);
    }

    public void setProgressVisible(boolean isVisible) {
        if(progressView != null){
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    isVisible ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

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
    public <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        composite.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                        throwable -> {
                            ToastUtils.showLong(getActivity(), throwable.getMessage());
                        }
                ));
    }

    public void error(String message) {
        hideLoading();
        if(!TextUtils.isEmpty(message)) {
            if(errorDialog == null || !errorDialog.isShowing()){
                errorDialog = DialogUtils.createErrorDialog(getActivity(), message);
            }
        }
    }

    public void error(int code, String error) {
        if (code == 2400||code==2401) {
            hideLoading();
            finish();
            return;
        }
        error(error);
    }

    public void setToolbar() {
        if (null != toolbar) {
            toolbar.setTitle("");
            toolbar.setNavigationOnClickListener(v -> finish());
        }

    }

    protected void setTitle(String title){
        if(StringValid.isStringValid(title)){
            if(tvTitle != null){
                tvTitle.setText(title);
            }
        }
    }

    //获取布局文件
    @LayoutRes
    public abstract int getLayoutId();

    public abstract T initPresenter();

    public abstract void initView(Bundle savedInstanceState);


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetcontentView() {
        weakReference = new WeakReference<AppCompatActivity>(this);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(weakReference);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
    }


    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null && !mPresenter.isDetached()) {
            mPresenter.onDestroy();
            mPresenter.detach();
        }

        CommonTool.hideIME(this);
        mUnbinder.unbind();
        AppManager.getAppManager().removeActivity(weakReference);
        composite.clear();
    }

    @Override
    public void finish() {
        if (mPresenter != null) mPresenter.detach();
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    protected void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void hideSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }


    @Override
    public boolean showTips(String tip, TipType tipType) {

        switch (tipType) {
            case Dialog:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText(getString(R.string.prompt))
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogSuccess:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("成功")
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogError:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("失败")
                        .setContentText(tip).
                        setConfirmText(getString(R.string.confirm)).show();
                return true;
            case View:
            case ViewSuccess:
            case ViewError:
                return false;
            case LoadingShow:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                mLoadingSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                mLoadingSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mLoadingSweetAlertDialog.setTitleText(tip);
                mLoadingSweetAlertDialog.show();
                return true;
            case LoadingHide:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                return true;
            case ToastLong:
                ToastUtil.showToast(this, tip, Toast.LENGTH_LONG);
                return true;
            case ToastShort:
                ToastUtil.showToast(this, tip, Toast.LENGTH_SHORT);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == 0)
            return showTips(tip, tipType);
        return false;
    }

    /**
     * 检查是否登录
     *
     * @return
     */
    @Override
    public boolean checkLogin() {
        try {
            boolean res = (boolean) SharedPreferencesTool.Get(this, "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
            res &= SharedPreferencesTool.Get(this, "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN).equals(
                    Integer.valueOf(EncryptionTool.decryptAES(SharedPreferencesTool.Get(this, "token", "", SharedPreferencesTool.SP_FILE_LOGIN).toString()).split("\\|")[0]));
            return res;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public Map<String, Object> getLoginUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getString(R.string.user_name));
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("userid", 0);
        map.put("phone", "");
        return SharedPreferencesTool.Get(this, map, SharedPreferencesTool.SP_FILE_LOGIN);
    }

    /**
     * 清除用户登录信息
     */
    protected void clearLoginInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "");
        map.put("token", "");
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("logined", false);
        map.put("sltoken", "");
        map.put("userid",0);
        SharedPreferencesTool.Save(mContext, map, SharedPreferencesTool.SP_FILE_LOGIN);
        CpigeonData.getInstance().initialization();
    }



    public void showLoading(){
        showTips("请稍后...", TipType.LoadingShow);
    }

    public void hideLoading(){
        showTips("", TipType.LoadingHide);
    }

    public Activity getActivity() {
        return this;
    }

    protected void addItemDecorationLine(RecyclerView recyclerView){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(R.color.line_color).size(ScreenTool.dip2px(0.5f)).build());
    }

    protected void addItemDecorationLine(RecyclerView recyclerView, @ColorRes int colorId){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(colorId).size(1)
                .build());
    }

    protected void addItemDecorationLine(RecyclerView recyclerView, @ColorRes int colorId, float width){
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(recyclerView.getContext())
                .colorResId(colorId).size(ScreenTool.dip2px(width))
                .build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
