package com.cpigeon.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimRes;

import com.cpigeon.app.R;
import com.cpigeon.app.base.FragmentParentActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zhu TingYu on 2017/11/15.
 */

public class IntentBuilder {

    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_DATA = "KEY_DATA";
    public static final String KEY_DATA_2 = "KEY_DATA_2";
    public static final String KEY_BOOLEAN = "KEY_BOOLEAN";
    public static final String KEY_TITLE = "KEY_TITLE";


    private Intent intent;

    private Context mContext;
    private @AnimRes
    int animOpen= R.anim.enter;
    private @AnimRes int animExit=R.anim.exit;

    public static IntentBuilder Builder() {
        return new IntentBuilder();
    }

    public static IntentBuilder Builder(Intent intent) {
        return new IntentBuilder(intent);
    }

    public static IntentBuilder Builder(String action) {
        return new IntentBuilder(action);
    }

    public static IntentBuilder Builder(String action, Uri uri) {
        return new IntentBuilder(action, uri);
    }

    public static IntentBuilder Builder(Context packageContext, Class<?> cls) {
        return new IntentBuilder(packageContext, cls);
    }

    public static IntentBuilder Builder(String action, Uri uri, Context packageContext, Class<?> cls) {
        return new IntentBuilder(action, uri, packageContext, cls);
    }

    public IntentBuilder() {
        intent = new Intent();
    }

    public IntentBuilder(Intent intent) {
        intent = new Intent(intent);
    }

    public IntentBuilder(String action) {
        intent = new Intent(action);
    }

    public IntentBuilder(String action, Uri uri) {
        intent = new Intent(action, uri);
    }

    public IntentBuilder(Context packageContext, Class<?> cls) {
        mContext = packageContext;
        intent = new Intent(packageContext, cls);
    }

    public IntentBuilder(String action, Uri uri, Context packageContext, Class<?> cls) {
        mContext = packageContext;
        intent = new Intent(action, uri, packageContext, cls);
    }

    public IntentBuilder setData(Uri uri){
        intent.setData(uri);
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getAction() {
        return intent.getAction();
    }

    public Uri getData() {
        return intent.getData();
    }

    public String getDataString() {
        return intent.getDataString();
    }

    public String getScheme() {
        return intent.getScheme();
    }

    public String getType() {
        return intent.getType();
    }


    public IntentBuilder setClass(Context packageContext, Class<?> cls) {
        mContext = packageContext;
        intent.setClass(mContext, cls);
        return this;
    }

    public IntentBuilder putExtra(String name, boolean value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, byte value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, char value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, short value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, int value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, long value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, float value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, double value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, String value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, CharSequence value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, Parcelable value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, Parcelable[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder setFlags(int falags) {
        intent.setFlags(falags);
        return this;
    }

    public IntentBuilder putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putStringArrayListExtra(String name, ArrayList<String> value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, Serializable value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, boolean[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, byte[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, short[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, char[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, int[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, long[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, float[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, double[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, String[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, CharSequence[] value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtra(String name, Bundle value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder putExtras(Intent src) {
        intent.putExtras(src);
        return this;
    }

    public IntentBuilder putExtras(Bundle extras) {
        intent.putExtras(extras);
        return this;
    }


    public IntentBuilder finish(Activity activity){
        activity.finish();
        return this;
    }

    public IntentBuilder overridePendingTransition(@AnimRes int animOpen, @AnimRes int animExit){
        this.animExit = animExit;
        this.animOpen = animOpen;
        return this;
    }

    public IntentBuilder startActivity() {
        if (mContext != null){
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(animOpen, animExit);
        }
        return this;
    }



    public void startActivity(Activity activity) {
        activity.startActivity(intent);
        activity.overridePendingTransition(animOpen, animExit);
    }

    public void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(animOpen, animExit);
    }

    public void startActivity(Activity activity, boolean isBack) {
        activity.startActivity(intent);
        if (isBack)
            activity.overridePendingTransition(animOpen, animExit);
        else
            activity.overridePendingTransition(animOpen, animExit);
    }



    public void startParentActivity(Activity context, Class clz) {
        intent.setClass(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        context.startActivity(intent);
        context.overridePendingTransition(animOpen, animExit);
    }

    public void startParentActivity(Context context, Class clz) {
        intent.setClass(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        context.startActivity(intent);
    }

    public void startParentActivity(Activity context, Class clz, boolean isHaveToolbar) {
        intent.setClass(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        intent.putExtra(FragmentParentActivity.KEY_IS_HAVE_TOOLBAR, isHaveToolbar);
        context.startActivity(intent);
        context.overridePendingTransition(animOpen, animExit);
    }
    public void startParentActivity(Activity context, Class clz,int requestCode) {
        intent.setClass(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(animOpen, animExit);

    }
    public void startParentActivity(BaseFragment fragment, Class clz, int requestCode) {
        intent.setClass(fragment.getContext(), FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        fragment.startActivityForResult(intent,requestCode);
        fragment.getActivity().overridePendingTransition(animOpen, animExit);

    }

}
