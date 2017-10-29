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
     * 切断所有rx操作
     */
    void UiDestroy();
}
