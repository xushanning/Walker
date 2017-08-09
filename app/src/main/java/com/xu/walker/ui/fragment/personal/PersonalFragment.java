package com.xu.walker.ui.fragment.personal;

import android.os.Bundle;

import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class PersonalFragment extends BaseFragment<PersonalContract.IPersonalPresenter> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public int getScreenMode() {
        //现在全屏模式有点问题,应该不能全屏模式
        return DYEING_MODE;
    }

    @Override
    public void initOthers() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void useNightMode(boolean isNight) {

    }
}
