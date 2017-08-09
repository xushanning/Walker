package com.xu.walker.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xusn10 on 2017/7/3.
 */

public abstract class BaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView {
    private View mView;
    protected T mPresenter;
    private Unbinder bind;
    //全屏模式
    public int FULL_SCREEN_MODE = 0;
    //着色模式
    public int DYEING_MODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        initScreen(getScreenMode());
        bind = ButterKnife.bind(this, mView);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView(savedInstanceState);
        initOthers();
        return mView;
    }

    public abstract int getLayoutId();

    public abstract int getScreenMode();

    public abstract void initOthers();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initPresenter();

    private void initScreen(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            if (mode == FULL_SCREEN_MODE) {
                //全屏模式
                //设置透明状态栏,这样才能让 ContentView 向上
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (mode == DYEING_MODE) {
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            int statusColor = Color.parseColor("#212121");
            window.setStatusBarColor(statusColor);

            ViewGroup mContentView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mode == FULL_SCREEN_MODE && mChildView != null) {
                //全屏模式
                ViewCompat.setFitsSystemWindows(mChildView, true);
            } else if (mode == DYEING_MODE && mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }
        } else {
            Logger.d("版本过低，不支持沉浸式标题栏");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        bind.unbind();
    }
}
