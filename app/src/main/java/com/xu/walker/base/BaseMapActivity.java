package com.xu.walker.base;

import android.os.Bundle;

import com.amap.api.maps.MapView;

/**
 * Created by Administrator on 2017/8/17.
 */

public class BaseMapActivity<T extends IBasePresenter> extends BaseActivity {
    MapView mapSport;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapSport.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，
        if (mapSport != null) {
            mapSport.onResume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapSport != null) {
            mapSport.onPause();
        }
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mapSport != null) {
            mapSport.onSaveInstanceState(outState);
        }

    }
}