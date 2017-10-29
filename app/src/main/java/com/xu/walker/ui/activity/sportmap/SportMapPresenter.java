package com.xu.walker.ui.activity.sportmap;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.xu.walker.utils.rx.RxBus;
import com.xu.walker.utils.rx.RxEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SportMapPresenter implements SportMapContract.ISportMapPresenter {
    private RxBus rxBus;
    private SportMapContract.ISportMapView sportMapView;
    private PolylineOptions polylineOptions;
    //是否需要初始化PolylineOptions
    private boolean isNeedPolylineOptions = false;

    @Override
    public void attachView(SportMapContract.ISportMapView view) {
        this.sportMapView = view;
    }

    @Override
    public void detachView() {
        sportMapView = null;
    }

    @Override
    public void start() {
        rxBus = RxBus.getInstance();
        Disposable disposable = rxBus.doSubscribe(RxEvent.class, new Consumer<RxEvent>() {
            @Override
            public void accept(RxEvent rxEvent) throws Exception {
                if (rxEvent.getType().equals(RxEvent.POST_LOCATION)) {
                    LatLng latLng = (LatLng) rxEvent.getMessage1();
                    Map<String, Object> sportData = (HashMap) rxEvent.getMessage3();
                    //里程
                    String totalDistance = (String) sportData.get("totalDistance");
                    String speed = (String) sportData.get("speed");
                    sportMapView.setSpeed(speed);
                    sportMapView.setTotalDistance(totalDistance);
                    //addpoint放在setpolyline方法上面，防止加载重复的点
                    sportMapView.addPoint(latLng);
                    if (isNeedPolylineOptions) {
                        polylineOptions = (PolylineOptions) rxEvent.getMessage2();
                        sportMapView.setPolylineOptions(polylineOptions);
                    }

                    Logger.d("地图从service中获取到的经纬度:" + latLng.latitude + "  " + latLng.longitude);
                } else if (rxEvent.getType().equals(RxEvent.POST_SPORT_TIME)) {
                    String time = (String) rxEvent.getMessage1();
                    sportMapView.setTime(time);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.d(throwable.getMessage());
            }
        });
        RxBus.getInstance().addSubscription(this, disposable);
    }

    @Override
    public void onUiDestroy() {

    }

    @Override
    public void unSubscribeRxBus() {
        rxBus.unSubscribe(this);
    }

    @Override
    public void getPolylineOptions() {
        //在下一次接收到数据后，发送polylineoptions
        isNeedPolylineOptions = true;
    }
}
