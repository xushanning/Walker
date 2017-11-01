package com.xu.walker.ui.activity.login;

import android.text.TextUtils;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.orhanobut.logger.Logger;
import com.xu.walker.base.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;


/**
 * Created by xusn10 on 2017/8/11.
 */

public class LoginPresenter extends BasePresenter implements LoginContract.ILoginPresenter {
    private LoginContract.ILoginView loginView;
    private TextView tvUserName, tvPassWord, tvLogin;

    @Override
    public void attachView(LoginContract.ILoginView view) {
        this.loginView = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void start() {
        tvUserName = loginView.getUserNameText();
        tvPassWord = loginView.getPassWordText();
        tvLogin = loginView.getLoginText();
        checkInputText();
    }

    @Override
    public void onUiDestroy() {

    }

    private void checkInputText() {
        Observable<CharSequence> userNameObservable = RxTextView.textChanges(tvUserName).skip(1);
        Observable<CharSequence> passWordObservable = RxTextView.textChanges(tvPassWord).skip(1);
        Observable.combineLatest(userNameObservable, passWordObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence userName, CharSequence passWord) throws Exception {
                //用户名有效条件:不为空+长度大于2且小于9
                boolean isUserNameValid = !TextUtils.isEmpty(userName) && userName.length() > 2 && userName.length() < 9;
                if (!isUserNameValid) {
                    loginView.getUserNameText().setError("用户名无效");
                }
                //密码有效条件：不能为空
                boolean isPassWordValid = !TextUtils.isEmpty(passWord);
                if (!isPassWordValid) {
                    loginView.getPassWordText().setError("密码不能为空");
                }
                Logger.d(isUserNameValid + "" + isPassWordValid);
                return isUserNameValid && isPassWordValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                tvLogin.setEnabled(aBoolean);
            }
        });
    }

    @Override
    public void doLogin() {
        loginView.showError("正在登陆...");
    }
}
