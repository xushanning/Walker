package com.xu.walker.ui.activity.sportmap;

import android.os.Bundle;

import com.amap.api.maps.MapView;
import com.xu.walker.R;
import com.xu.walker.base.BaseActivity;
import com.xu.walker.base.BaseMapActivity;
import com.xu.walker.base.IBaseView;

import butterknife.BindView;

public class SportMapActivity extends BaseMapActivity<SportMapContract.ISportMapPresenter> implements SportMapContract.ISportMapView {


    @BindView(R.id.map_sport_map)
    MapView mMapView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_map;
    }

    @Override
    public void initPresenter() {
        mPresenter = new SportMapPresenter();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initMap(savedInstanceState);
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

    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);

    }


}
