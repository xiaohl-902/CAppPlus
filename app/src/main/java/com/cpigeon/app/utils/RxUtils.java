package com.cpigeon.app.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.utils.http.RxNet;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.NewThreadScheduler;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class RxUtils {
    public static Observable<Object> click(View view) {
        return Observable.create(subscriber -> {
            view.setOnClickListener(v -> {
                v.setEnabled(false);
                v.postDelayed(() -> {
                    v.setEnabled(true);
                }, 350);
                subscriber.onNext(new Object());
            });
        });
    }

    public static Disposable runOnNewThread(Consumer<Object> consumer) {
        return Observable.create(e -> e.onNext(new Object()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(new NewThreadScheduler())
                .subscribe(consumer);
    }

    public static Disposable delayed(int milliseconds, Consumer<Long> consumer) {
      return Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(new NewThreadScheduler())
                .subscribe(consumer);
    }

    public static Observable<Long> delayed_1(int seconds) {
       return Observable.timer(seconds, TimeUnit.MILLISECONDS);
    }

    public static Disposable rollPoling(long initialDelay, long period, Consumer<Long> consumer) {
        return Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(new NewThreadScheduler())
                .subscribe(consumer);
    }

    public static Observable<String> textChanges(TextView view) {
        return Observable.create(subscriber -> {
            final TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subscriber.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            if (view != null) {
                view.removeTextChangedListener(watcher);
                view.addTextChangedListener(watcher);
                subscriber.onNext(view.getText().toString());
            }
        });
    }

    public static Consumer<? super Boolean> enabled(final View view) {
        return b -> {
            if (view != null)
                view.setEnabled(b);
        };
    }


}
