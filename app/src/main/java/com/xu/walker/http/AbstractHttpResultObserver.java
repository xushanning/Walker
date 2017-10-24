package com.xu.walker.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.xu.walker.base.BaseResBean;
import com.xu.walker.utils.LoadingAlertDialog;
import com.xu.walker.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xusn10 on 2017/6/23.
 */

public abstract class AbstractHttpResultObserver<T> implements Observer<BaseResBean<T>> {
    protected Context mContext;
    //默认的是加载，如果
    private String defaultShowMsg = "正在加载..";
    private LoadingAlertDialog loadingAlertDialog;
    private static final String TOKEN_OVERDUE = "101";
    private static final String TOKEN_INFO_ERROR = "102";
    private static final String NO_TOKEN = "103";
    private static final String WRONG_PASSWORD = "password error.";
    private static final String USER_NOT_FIND = "user not found.";

    public AbstractHttpResultObserver(Context mContext, String showMsg) {
        this.mContext = mContext;
        this.defaultShowMsg = showMsg;
    }

    public AbstractHttpResultObserver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        //订阅的时候
        onRequestStart();
        Logger.d("正在加载。。");
    }

    @Override
    public void onNext(@NonNull BaseResBean<T> tBaseResModel) {
        //成功
        if (tBaseResModel.isSuccess()) {
            onSuccess(tBaseResModel.getData());
        } else {
            onCodeError(tBaseResModel.getMessage());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //打印连不上服务器之类的。。。
        closeProgressDialog();
        Logger.d(e);
        ToastUtil.toastShort(mContext, "无法连接服务器！");
        loadingAlertDialog.dismiss();
    }

    @Override
    public void onComplete() {
        closeProgressDialog();
        Logger.d("加载完成");
    }

    protected abstract void onSuccess(T t);

    protected void onCodeError(String codeMessage) {
        //代码错误分析，给出各种吐司
        closeProgressDialog();
        switch (codeMessage) {
            case TOKEN_OVERDUE:
                ToastUtil.toastShort(mContext, "登录过期,将重新登录!");
                break;
            case TOKEN_INFO_ERROR:
                ToastUtil.toastShort(mContext, "token有误,将重新登录!");
                break;
            case NO_TOKEN:
                ToastUtil.toastShort(mContext, "token有误,将重新登录!");
                break;
            case WRONG_PASSWORD:
                ToastUtil.toastShort(mContext, "密码错误!");
                break;
            case USER_NOT_FIND:
                ToastUtil.toastShort(mContext, "没有此用户!");
                break;
        }

    }

    protected void onRequestStart() {
        loadingAlertDialog = new LoadingAlertDialog(mContext);
        loadingAlertDialog.show(defaultShowMsg);
    }

    public void closeProgressDialog() {
        loadingAlertDialog.dismiss();
    }
}
