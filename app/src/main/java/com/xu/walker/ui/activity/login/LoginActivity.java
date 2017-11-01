package com.xu.walker.ui.activity.login;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;
import com.xu.walker.ui.activity.register.RegisterActivity;
import com.xu.walker.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xusn10 on 2017/8/11.
 *
 * @author xu
 */

public class LoginActivity extends BaseActivity<LoginContract.ILoginPresenter> implements LoginContract.ILoginView {
    @BindView(R.id.tv_activity_login_login)
    TextView tvLogin;
    @BindView(R.id.tv_login_register)
    TextView tvRegister;
    @BindView(R.id.tv_user_name)
    EditText tvUserName;
    @BindView(R.id.tv_passWord)
    EditText tvPassWord;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LoginPresenter();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GradientDrawable gdInfo = (GradientDrawable) tvLogin.getBackground();
        gdInfo.setStroke(1, getResources().getColor(R.color.a_login_login_bt));
        gdInfo.setColor(getResources().getColor(R.color.a_login_login_bt));
        gdInfo.setCornerRadius(8);
    }

    @Override
    public void initOthers() {
        mPresenter.start();
    }

    @Override
    public void showError(String msg) {
        ToastUtil.toastShort(this, msg);
    }

    @Override
    public void useNightMode(boolean isNight) {

    }


    @OnClick({R.id.tv_login_register, R.id.tv_activity_login_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_activity_login_login:
                mPresenter.doLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public TextView getUserNameText() {
        return tvUserName;
    }

    @Override
    public TextView getPassWordText() {
        return tvPassWord;
    }

    @Override
    public TextView getLoginText() {
        return tvLogin;
    }


}
