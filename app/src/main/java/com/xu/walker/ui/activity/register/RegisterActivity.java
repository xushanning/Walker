package com.xu.walker.ui.activity.register;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xusn10 on 2017/8/11.
 */

public class RegisterActivity extends BaseActivity<RegisterContract.IRegisterPresenter> implements RegisterContract.IRegisterView {

    @BindView(R.id.tv_activity_register_register)
    TextView tvRegister;
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
        GradientDrawable gdValidate = (GradientDrawable) tvValidate.getBackground();
        gdValidate.setStroke(1, getResources().getColor(R.color.a_login_login_bt));
        gdValidate.setColor(getResources().getColor(R.color.a_login_login_bt));
        gdValidate.setCornerRadius(5);

        GradientDrawable gdRegister = (GradientDrawable) tvRegister.getBackground();
        gdRegister.setStroke(1, getResources().getColor(R.color.a_login_login_bt));
        gdRegister.setColor(getResources().getColor(R.color.a_login_login_bt));
        gdRegister.setCornerRadius(5);
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


    @OnClick({R.id.tv_activity_register_validate, R.id.tv_activity_register_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_register_validate:
                break;
            case R.id.tv_activity_register_register:
                break;
        }
    }
}
