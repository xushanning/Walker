package com.xu.walker.ui.fragment.club;

import android.os.Bundle;

import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class ClubFragment extends BaseFragment<ClubContract.IClubPresenter> implements ClubContract.IClubContractView {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_club;
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
