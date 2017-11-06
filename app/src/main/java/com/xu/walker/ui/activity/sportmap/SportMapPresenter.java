package com.xu.walker.ui.activity.sportmap;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.xu.walker.MyApplication;
import com.xu.walker.bean.req.LocationInfoBean;
import com.xu.walker.db.TrajectoryDBBeanDao;
import com.xu.walker.db.bean.TrajectoryDBBean;
import com.xu.walker.utils.rx.RxBus;
import com.xu.walker.utils.rx.RxEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SportMapPresenter implements SportMapContract.ISportMapPresenter {
    private RxBus rxBus;
    private SportMapContract.ISportMapView sportMapView;
    private PolylineOptions polylineOptions;
    //是否需要初始化PolylineOptions
    //private boolean isNeedPolylineOptions = false;
    private LatLng lastLatLng;

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
                if (rxEvent.getType().equals(RxEvent.POST_SPORT_INFO)) {

                    LatLng currentLatLng = (LatLng) rxEvent.getMessage1();
                    //判断，如果当前点不为null，并且和上一个点不等的话
                    if (currentLatLng != null && !currentLatLng.equals(lastLatLng)) {
                        //追加最新的点到轨迹上去
                        sportMapView.addPoint(currentLatLng);
                        lastLatLng = currentLatLng;
                        Logger.d("地图从service中获取到的经纬度:" + currentLatLng.latitude + "  " + currentLatLng.longitude);
                    }
                    HashMap<String, Object> sportData = (HashMap) rxEvent.getMessage2();
                    //里程
                    String totalDistance = (String) sportData.get("totalDistance");
                    String speed = (String) sportData.get("speed");
                    String time = (String) sportData.get("sportTime");
                    sportMapView.setTime(time);
                    sportMapView.setSpeed(speed);
                    sportMapView.setTotalDistance(totalDistance);
                    //addpoint放在setpolyline方法上面，防止加载重复的点


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
        //在下一次接收到数据后，从数据库里读取存储的轨迹点，发送polylineoptions
        // isNeedPolylineOptions = true;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                polylineOptions = new PolylineOptions();
                if (MyApplication.getSportID() != null) {
                    TrajectoryDBBeanDao trajectoryDBBeanDao = MyApplication.getInstances().getDaoSession().getTrajectoryDBBeanDao();
                    TrajectoryDBBean trajectoryDBBean = trajectoryDBBeanDao.queryBuilder().where(TrajectoryDBBeanDao.Properties.TrajectoryID.eq(MyApplication.getSportID())).unique();
                    List<LocationInfoBean> locationInfoBeans = trajectoryDBBean.getLocationInfoBeans();
                    if (locationInfoBeans != null && locationInfoBeans.size() > 0) {
                        for (LocationInfoBean locationInfoBean : locationInfoBeans) {
                            LatLng latLng = new LatLng(locationInfoBean.getLatitude(), locationInfoBean.getLongitude());
                            polylineOptions.add(latLng);
                        }
                    }
                }
                sportMapView.setPolylineOptions(polylineOptions);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
