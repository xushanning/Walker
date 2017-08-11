package com.xu.walker.ui.activity.register;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xusn10 on 2017/8/11.
 */

public class RegisterActivity extends BaseActivity<RegisterContract.IRegisterPresenter> implements RegisterContract.IRegisterView {
    @BindView(R.id.tv_activity_register_validate)
    TextView tvValidate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GradientDrawable gdInfo = (GradientDrawable) tvValidate.getBackground();
        gdInfo.setStroke(1, getResources().getColor(R.color.a_login_login_bt));
        gdInfo.setColor(getResources().getColor(R.color.a_login_login_bt));
        gdInfo.setCornerRadius(5);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_activity_register_validate)
    public void onClick() {
    }
}
