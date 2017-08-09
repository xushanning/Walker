package com.xu.walker.base;

/**
 * Created by xusn10 on 2017/3/28.
 */

public interface IBaseView {

    void showError(String msg);

    //是否切换为夜间模式
    void useNightMode(boolean isNight);
}
