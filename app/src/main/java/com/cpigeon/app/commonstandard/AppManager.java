package com.cpigeon.app.commonstandard;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * auth:AndySong
 * Created by Administrator on 2017/4/11.
 */

public class AppManager {
    /**
     * Activity栈
     */
    private Stack<WeakReference<AppCompatActivity>> mActivityStack;

    private enum MyEnumSingleton{
        INSTANCE;
        private AppManager appManager;
        MyEnumSingleton(){
            appManager = new AppManager();
        }
        public AppManager getAppManager(){
            return appManager;
        }

    }

    public static AppManager getAppManager(){
        return MyEnumSingleton.INSTANCE.getAppManager();
    }

    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int stackSize() {
        return mActivityStack.size();
    }

    /***
     * 获得Activity栈
     *
     * @return Activity栈
     */
    public Stack<WeakReference<AppCompatActivity>> getStack() {
        return mActivityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(WeakReference<AppCompatActivity> activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    public void removeActivity(WeakReference<AppCompatActivity> activity){
        if(mActivityStack!=null){
            mActivityStack.remove(activity);
        }
    }


    public Activity getTopActivity() {
        return mActivityStack.lastElement().get();
    }


    public Activity getActivityByClass(Class<?> cls) {
        AppCompatActivity return_activity = null;
        if(mActivityStack != null && !mActivityStack.empty()){
            for (WeakReference<AppCompatActivity> activity : mActivityStack) {
                if (activity.get().getClass().equals(cls)) {
                    return_activity = activity.get();
                    break;
                }
            }
        }
        return return_activity;
    }


    public void killTopActivity() {
        try {
            WeakReference<AppCompatActivity> activity = mActivityStack.lastElement();
            killActivity(activity);
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    private void killActivity(WeakReference<AppCompatActivity> activity) {
        try {
            Iterator<WeakReference<AppCompatActivity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<AppCompatActivity> stackActivity = iterator.next();
                if(stackActivity.get()==null){
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    break;
                }
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }


    public void killAllToLoginActivity(Class<?> cls) {
        try {

            ListIterator<WeakReference<AppCompatActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null && cls != activity.getClass()) {
                    listIterator.remove();
                    activity.finish();
                }
            }

        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }



    public void killAllToLoginActivity() {
        try {
            ListIterator<WeakReference<AppCompatActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    listIterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }


    public void killActivity(Class<?> cls) {
        try {

            ListIterator<WeakReference<AppCompatActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    activity.finish();
                    break;
                }
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }


    private void killAllActivity() {
        try {
            ListIterator<WeakReference<AppCompatActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    activity.finish();
                }
                listIterator.remove();
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            killAllActivity();
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
        }
    }
}