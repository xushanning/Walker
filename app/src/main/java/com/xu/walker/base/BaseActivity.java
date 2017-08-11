package com.xu.walker.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.xu.walker.MyApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xusn10 on 2017/6/9.
 * base
 */

public abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    protected T mPresenter;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initScreen();
        initPresenter();
        bind = ButterKnife.bind(this);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView(savedInstanceState);
        initOthers();
    }


    public abstract int getLayoutId();

    public abstract void initPresenter();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initOthers();

    private void initScreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int color = Color.parseColor("#000000");
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        bind.unbind();
    }
}
