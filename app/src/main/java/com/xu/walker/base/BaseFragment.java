package com.xu.walker.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        initScreen();
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


    public abstract void initOthers();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initPresenter();

    private void initScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getActivity().getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            winParams.flags = winParams.flags & ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
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
        //中断所有的rx请求和其他
        mPresenter.onUiDestroy();
    }
}
