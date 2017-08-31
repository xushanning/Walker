package com.xu.walker.ui.activity.sportmap;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.xu.walker.R;
import com.xu.walker.base.BaseMapActivity;

import butterknife.BindView;

public class SportMapActivity extends BaseMapActivity<SportMapContract.ISportMapPresenter> implements SportMapContract.ISportMapView {


    @BindView(R.id.map_sport_map)
    MapView mMapView;
    @BindView(R.id.tv_total_distance)
    TextView tvTotalDistance;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_sport_time)
    TextView tvSportTime;
    private AMap aMap;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private boolean isMoveCamera = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_map;
    }

    @Override
    public void initPresenter() {
        mPresenter = new SportMapPresenter();
        //开始接收经纬度信息
        mPresenter.start();
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
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
    }


    @Override
    public void addPoint(LatLng latLng) {
        if (polylineOptions != null) {
            polylineOptions.width(15).color(Color.parseColor("#39ABE9"));
            polylineOptions.add(latLng);
            polyline = aMap.addPolyline(polylineOptions);
            if (isMoveCamera) {
                //只有第一次的时候移动镜头，后面不移动
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f), 1000, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                isMoveCamera = false;
            }

        }
    }

    @Override
    public void setPolylineOptions(PolylineOptions polylineOptions) {
        this.polylineOptions = polylineOptions;
    }

    @Override
    public void setTotalDistance(String distance) {
        tvTotalDistance.setText(distance);
    }

    @Override
    public void setSpeed(String speed) {
        tvSpeed.setText(speed);
    }

    @Override
    public void setTime(String time) {
        tvSportTime.setText(time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (polyline != null) {
            polyline.remove();
        }
        //告诉presenter，要给一下polylineoptions对象了
        mPresenter.getPolylineOptions();
    }


}
