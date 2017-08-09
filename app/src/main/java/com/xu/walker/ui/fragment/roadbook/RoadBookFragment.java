package com.xu.walker.ui.fragment.roadbook;

import android.os.Bundle;

import com.xu.walker.R;
import com.xu.walker.base.BaseFragment;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class RoadBookFragment extends BaseFragment<RoadBookContract.IRoadBookPresenter>implements RoadBookContract.IRoadBookView{
    @Override
    public int getLayoutId() {
        return R.layout.fragment_road_book;
    }
    @Override
    public int getScreenMode() {
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
