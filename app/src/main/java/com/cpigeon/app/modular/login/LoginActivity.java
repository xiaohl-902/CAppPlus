package com.cpigeon.app.modular.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.login.presenter.LoginPre;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zhu TingYu on 2018/6/1.
 */

public class LoginActivity extends BaseActivity<LoginPre> {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.tvLoginIn)
    TextView tvLoginIn;
    @BindView(R.id.tvForgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.imgShowPass)
    ImageView imgShowPass;
    /*@BindView(R.id.bg2)
    ImageView bg2;
    @BindView(R.id.bg1)
    ImageView bg1;
    @BindView(R.id.bg3)
    ImageView bg3;*/

    boolean isPasswordVisible = false;

    Animation inFromRight;
    Animation outToLeft;
    Animation animationBg3;
    Animation animationBg1Out;

    private static boolean isExit = false;
    @BindView(R.id.back)
    ImageView back;
    private boolean isLoginOut = false;

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    public static void startActivity(Activity activity) {
        IntentBuilder.Builder(activity, LoginActivity.class).startActivity();
    }

    @Override
    public LoginPre initPresenter() {
        return new LoginPre(getActivity());
    }

    @Override
    protected void doBeforeSetcontentView() {
        super.doBeforeSetcontentView();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int flags;
        int curApiVersion = Build.VERSION.SDK_INT;
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        } else {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        isLoginOut = getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
        if(isLoginOut){
            AppManager.getAppManager().killAllToLoginActivity(LoginActivity.class);
        }


        bindData(RxUtils.textChanges(etUserName), mPresenter.setUserId());
        bindData(RxUtils.textChanges(etPassWord), mPresenter.setPassword());

        etUserName.setText(SharedPreferencesTool.Get(this, "loginname", "", SharedPreferencesTool.SP_FILE_LOGIN).toString());
        etPassWord.setText(SharedPreferencesTool.Get(this, "loginPassWord", "", SharedPreferencesTool.SP_FILE_LOGIN).toString());
        String username = etUserName.getText().toString();
        etUserName.setSelection(username.length() > 0 ? username.length() : 0);
        MyApp.clearJPushAlias();

        tvLoginIn.setOnClickListener(v -> {
            showLoading();
            mPresenter.loginIn(s -> {
                hideLoading();
                if (s.status) {
                    SharedPreferencesTool.Save(this, "loginname", mPresenter.userId, SharedPreferencesTool.SP_FILE_LOGIN);
                    SharedPreferencesTool.Save(this, "loginPassWord", mPresenter.password, SharedPreferencesTool.SP_FILE_LOGIN);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    MyApp.setJPushAlias();
                    finish();
                } else {
                    if (s.errorCode == 1000) {
                        error("用户名或密码为空");
                    } else if (s.errorCode == 1001) {
                        error("用户名或密码错误");
                    }
                }
            });
        });

        imgShowPass.setOnClickListener(view -> {
            if (isPasswordVisible) {
                isPasswordVisible = false;
                etPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                etPassWord.setSelection(etPassWord.getText().toString().length());//将光标移至文字末尾
                ((AppCompatImageView) view).setImageResource(R.mipmap.ic_login_hide_password);
            } else {
                isPasswordVisible = true;
                etPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                etPassWord.setSelection(etPassWord.getText().toString().length());//将光标移至文字末尾
                ((AppCompatImageView) view).setImageResource(R.mipmap.ic_login_show_password);
            }
        });

        tvForgetPassword.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), ForgetPasswordFragment.class);
        });

        tvRegister.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), SignUpFragment.class);
        });

        back.setOnClickListener(v -> finish());

        /*composite.add(RxUtils.delayed(1000, aLong -> {
            initAnimation();
        }));*/

    }

    /*private void initAnimation() {
        inFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.login_in_from_right);
        outToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.login_out_to_left);
        animationBg3 = AnimationUtils.loadAnimation(getActivity(), R.anim.login_bg_3);
        animationBg1Out = AnimationUtils.loadAnimation(getActivity(), R.anim.login_bg_1_out);

        bg1.startAnimation(outToLeft);
        bg2.startAnimation(inFromRight);

        inFromRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    bg1.setVisibility(View.GONE);
                    bg3.setVisibility(View.VISIBLE);
                    bg3.startAnimation(animationBg3);
                    RxUtils.delayed(2500,aLong -> {
                        try {
                            bg1.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationBg3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    bg3.setVisibility(View.GONE);
                    bg1.startAnimation(outToLeft);
                    bg2.startAnimation(inFromRight);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void onBackPressed() {
        if (isLoginOut) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Then_click_one_exit_procedure),
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                AppManager.getAppManager().AppExit(mContext);
            }
        } else {
            finish();
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
