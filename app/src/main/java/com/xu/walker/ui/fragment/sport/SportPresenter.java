package com.xu.walker.ui.fragment.sport;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.utils.RxBus;
import com.xu.walker.utils.RxEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class SportPresenter implements SportContract.ISportPresenter {
    private RxBus rxBus;
    private SportContract.ISportView sportView;

    @Override
    public void attachView(SportContract.ISportView view) {
        this.sportView = view;
    }

    @Override
    public void detachView() {
        sportView = null;
    }

    @Override
    public void start() {
        rxBus = RxBus.getInstance();
        Disposable disposable = rxBus.doSubscribe(RxEvent.class, new Consumer<RxEvent>() {
            @Override
            public void accept(RxEvent rxEvent) throws Exception {
                if (rxEvent.getType().equals(RxEvent.POST_LOCATION)) {
                    LatLng latLonPoint = (LatLng) rxEvent.getMessage1();
                    Map<String, Object> sportData = (HashMap) rxEvent.getMessage3();
                    //里程
                    String totalDistance = (String) sportData.get("totalDistance");
                    String altitude = (String) sportData.get("altitude");
                    String speed = (String) sportData.get("speed");
                    String maxSpeed = (String) sportData.get("maxSpeed");
                    String totalClimb = (String) sportData.get("totalClimb");

                    sportView.setMileage(totalDistance);
                    sportView.setSpeed(speed);
                    sportView.setAltitude(altitude);
                    sportView.setMaxSpeed(maxSpeed);
                    sportView.setClimb(totalClimb);
                    if (Float.valueOf(speed) == 0) {
                        sportView.setTitle("自动暂停");
                    } else {
                        sportView.setTitle("正在运动");
                    }

                    // Logger.d("运动碎片从service中获取到的经纬度:" + latLonPoint.getLatitude() + "  " + latLonPoint.getLongitude());
                } else if (rxEvent.getType().equals(RxEvent.POST_SPORT_TIME)) {
                    String time = (String) rxEvent.getMessage1();
                    sportView.setTime(time);
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
    public void unSubscribeRxBus() {
        rxBus.unSubscribe(this);
    }
}
