package com.xu.walker.base;

/**
 * Created by xusn10 on 2017/6/9.
 */

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);

    void detachView();

    void start();
}
