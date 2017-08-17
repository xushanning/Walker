package com.xu.walker.ui.activity.sportmap;

import android.os.Bundle;

import com.amap.api.maps.MapView;
import com.xu.walker.R;
import com.xu.walker.base.BaseMapActivity;

import butterknife.BindView;

public class SportMapActivity extends BaseMapActivity<SportMapContract.ISportMapPresenter> implements SportMapContract.ISportMapView {


    @BindView(R.id.map_sport_map)
    MapView mapSport;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_map;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mapSport.onCreate(savedInstanceState);
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


}
