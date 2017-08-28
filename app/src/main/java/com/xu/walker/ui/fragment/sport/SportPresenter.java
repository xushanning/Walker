package com.xu.walker.ui.fragment.sport;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.orhanobut.logger.Logger;
import com.xu.walker.utils.RxBus;
import com.xu.walker.utils.RxEvent;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class SportPresenter implements SportContract.ISportPresenter {
    private RxBus rxBus;

    @Override
    public void attachView(SportContract.ISportView view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void start() {
        rxBus = RxBus.getInstance();
        Disposable disposable = rxBus.doSubscribe(RxEvent.class, new Consumer<RxEvent>() {
            @Override
            public void accept(RxEvent rxEvent) throws Exception {
                if (rxEvent.getType().equals(RxEvent.POST_LOCATION)) {
                    LatLonPoint latLonPoint = (LatLonPoint) rxEvent.getMessage();
                    Logger.d("从service中获取到的经纬度:" + latLonPoint.getLatitude() + "  " + latLonPoint.getLongitude());
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
