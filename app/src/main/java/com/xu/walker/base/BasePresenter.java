package com.xu.walker.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 *
 * @author xusn10
 * @date 2017/10/26
 */

public class BasePresenter {
    protected CompositeDisposable mCompositeDisposable;

    public BasePresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }
}
