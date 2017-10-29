package com.xu.walker.base;

/**
 * Created by xusn10 on 2017/6/9.
 *
 * @author 许善宁
 */

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);

    void detachView();

    void start();

    /**
     * 当activity或者fragement销毁的时候调用
     */
    void onUiDestroy();
}
