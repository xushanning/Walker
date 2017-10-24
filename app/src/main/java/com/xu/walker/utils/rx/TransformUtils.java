package com.xu.walker.utils.rx;

import android.support.annotation.NonNull;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xusn10 on 2017/6/26.
 */

public class TransformUtils {
    public static <T> ObservableTransformer<T, T> defaultSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull io.reactivex.Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io());
            }

        };
    }

    public static <T> ObservableTransformer<T, T> allIo() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull io.reactivex.Observable<T> upstream) {
                return upstream.observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io());
            }

        };
    }
}
