package com.xu.walker.ui.activity.guide;

import android.os.Bundle;

import com.xu.walker.base.BaseActivity;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class GuideActivity extends BaseActivity<GuideContract.IGuidePresenter>implements GuideContract.IGuideView{
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initOthers() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }
}
