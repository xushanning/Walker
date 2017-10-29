package com.xu.walker.ui.fragment.sport;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.amap.api.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.xu.walker.base.BasePresenter;
import com.xu.walker.service.MainService;
import com.xu.walker.utils.rx.RxBus;
import com.xu.walker.utils.rx.RxEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xusn10 on 2017/8/7.
 *
 * @author xu
 */

public class SportPresenter extends BasePresenter implements SportContract.ISportPresenter {
    private RxBus rxBus;
    private SportContract.ISportView sportView;
    private MainService.MyBinder myBinder;

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
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onUiDestroy() {

    }

    @Override
    public void unSubscribeRxBus() {
        rxBus.unSubscribe(this);
    }


    @Override
    public void stopSport() {
        myBinder.stopSport();
    }

    @Override
    public void checkSportsFrDB(int locationInterval) {
        myBinder.checkSportsFrDB(locationInterval);
    }

    @Override
    public void bindService(Context context) {
        Intent bindIntent = new Intent(context, MainService.class);
        context.startService(bindIntent);
        context.bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MainService.MyBinder) iBinder;
//            myBinder.startSport(locationInterval);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void unBindService(Context context) {
        context.unbindService(connection);
    }


}
