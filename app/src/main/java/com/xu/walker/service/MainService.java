package com.xu.walker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;
import com.xu.walker.MyApplication;
import com.xu.walker.R;
import com.xu.walker.bean.LocationBean;
import com.xu.walker.db.TrajectoryDBBeanDao;
import com.xu.walker.db.bean.TrajectoryDBBean;
import com.xu.walker.ui.activity.main.MainActivity;
import com.xu.walker.utils.ToastUtil;
import com.xu.walker.utils.rx.RxBus;
import com.xu.walker.utils.rx.RxEvent;
import com.xu.walker.utils.rx.TransformUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xusn10 on 2017/8/25.
 *
 * @author 许善宁
 */

public class MainService extends Service implements AMapLocationListener {
    private MyBinder myBinder = new MyBinder();
    public AMapLocationClientOption mLocationOption;
    public AMapLocationClient mLocationClient;
    private List<LocationBean.DataBean> dataBeanList;
    private int count = 0;
    //存点专用
    private PolylineOptions polylineOptions;
    private static final int MINUTE = 60;

    /**
     * 小时的上限
     */
    private static final int HOUR_LIMIT = 99;
    private Disposable mDisposable;
    /**
     * 存放运动参数，比如说一些，时速，里程，时间，均速等
     */
    private Map<String, Object> sportData = new HashMap<>();
    /**
     * 上一个点
     */
    private LatLng lastLatLng;
    /**
     * 运动距离
     */
    private float totalDistance;
    /**
     * 最快速度
     */
    private float maxSpeed;
    /**
     * 爬升
     */
    private float totalClimb;
    /**
     * 上一个记录的海拔
     */
    private double lastAltitude = -1;
    /**
     * 记录从运动了多少秒
     */
    private int recordSecond = 0;
    /**
     * 写入数据库的时间间隔（5s写入一次）
     */
    private int insertDBInterval = 5;
    private TrajectoryDBBeanDao trajectoryDBBeanDao;
    /**
     * 当前运动的轨迹的id
     */
    private String trajectoryID;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentTitle("行者")
                .setTicker("虽千万里 吾往矣")
                .setSmallIcon(R.mipmap.app_icon_notice)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("service被销毁了");
    }

    public class MyBinder extends Binder {
        /**
         * 检查数据库中是否有未完成的运动轨迹
         *
         * @param locationInterval 定位间隔
         */

        public void checkSportsFrDB(final int locationInterval) {
            Observable.create(new ObservableOnSubscribe<List<TrajectoryDBBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<TrajectoryDBBean>> e) throws Exception {
                    trajectoryDBBeanDao = MyApplication.getInstances().getDaoSession().getTrajectoryDBBeanDao();
                    //找出是否有未完成的数据
                    List<TrajectoryDBBean> sportsHistoryList = trajectoryDBBeanDao.queryBuilder().where(TrajectoryDBBeanDao.Properties.IsSportsComplete.eq(false)).list();
                    e.onNext(sportsHistoryList);
                    e.onComplete();
                }
            }).filter(new Predicate<List<TrajectoryDBBean>>() {
                @Override
                public boolean test(List<TrajectoryDBBean> trajectoryDBBeans) throws Exception {
                    return trajectoryDBBeans != null && trajectoryDBBeans.size() > 0;
                }
            }).subscribeOn(AndroidSchedulers.mainThread())
                    .switchIfEmpty(new Observable<List<TrajectoryDBBean>>() {
                        @Override
                        protected void subscribeActual(Observer<? super List<TrajectoryDBBean>> observer) {
                            //数据库里没有数据，那么切换到主线程开始运动
                            startSport(locationInterval);
                        }
                    })
                    .compose(TransformUtils.<List<TrajectoryDBBean>>defaultSchedulers())
                    .subscribe(new Consumer<List<TrajectoryDBBean>>() {
                        @Override
                        public void accept(List<TrajectoryDBBean> trajectoryDBBeen) throws Exception {
                            //数据库里有数据，那么通知fragment
                        }
                    });

        }

        /**
         * 停止运动
         */
        public void stopSport() {
            //进行数据库操作
            updateTrajectoryData(true);
            //停止计时
            mDisposable.dispose();
            mLocationClient.stopLocation();
            trajectoryID = "";
        }
    }

    /**
     * 开始运动
     *
     * @param locationInterval 定位的间隔
     */
    public void startSport(long locationInterval) {
//        Gson gson = new Gson();
//        LocationBean locationBean = gson.fromJson(SportData.sportData, LocationBean.class);
//        dataBeanList = locationBean.getData();
        polylineOptions = new PolylineOptions();
        //生成轨迹ID
        this.trajectoryID = UUID.randomUUID().toString().replaceAll("-", "");
        createTrajectory();
        initLocation(locationInterval);
        startSendData();
    }


    /**
     * 开始发送数据
     */
    private void startSendData() {
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //这里进行写数据库操作(五秒钟写入一次)
                        if (aLong % insertDBInterval == 0) {
                            updateTrajectoryData(false);
                        }
                    }
                })
                .compose(TransformUtils.<Long>defaultSchedulers())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        recordSecond++;
                        Logger.d(secToTime(recordSecond));
                        RxEvent rxEvent = new RxEvent();
                        rxEvent.setType(RxEvent.POST_SPORT_TIME);
                        rxEvent.setMessage1(secToTime(recordSecond));
                        //开始发送时间
                        RxBus.getInstance().post(rxEvent);
                    }
                });
    }


    /**
     * 更新轨迹数据库
     */
    private void updateTrajectoryData(final boolean isComplete) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                //切换子线程进行数据库操作
                //查询当前轨迹id的实例
                TrajectoryDBBean trajectoryDBBean = trajectoryDBBeanDao.queryBuilder().where(TrajectoryDBBeanDao.Properties.TrajectoryID.eq(trajectoryID)).unique();
                trajectoryDBBean.setIsSportsComplete(isComplete);


                trajectoryDBBeanDao.update(trajectoryDBBean);
                //什么都不发射，完成数据库操作就结束了
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 用于巡河的时候，生成一条轨迹
     */
    private void createTrajectory() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                //切换子线程进行数据库操作
                TrajectoryDBBean trajectoryDBBean = new TrajectoryDBBean();
                trajectoryDBBean.setIsSportsComplete(false);
                trajectoryDBBean.setSportsBeginTime((int) System.currentTimeMillis());
                trajectoryDBBean.setTrajectoryID(trajectoryID);
                trajectoryDBBean.setLocationInfoBeans(null);
                trajectoryDBBeanDao.insert(trajectoryDBBean);
                //什么都不发射，完成数据库操作就结束了
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe();


    }

    public String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < MINUTE) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > HOUR_LIMIT) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }


    private void initLocation(long locationInterval) {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setInterval(locationInterval);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                double longitude = aMapLocation.getLongitude();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = aMapLocation.getLatitude();//获取纬度
                int satelliteCount = aMapLocation.getSatellites();//获取卫星的个数，用于控制gps信号的强弱
                double altitude = aMapLocation.getAltitude();//获取海拔高度
                float bearing = aMapLocation.getBearing();//获取方向角
                float speed = aMapLocation.getSpeed();//获取速度
                ToastUtil.toastShort(this, "经纬度:" + latitude + " " + longitude);
                if (maxSpeed != 0 && speed > maxSpeed) {
                    maxSpeed = speed;//获取最快速度
                }
                if (lastAltitude != -1) {
                    double climb = altitude - lastAltitude;
                    lastAltitude = altitude;
                    totalClimb += climb;
                }
                LatLng currentLatLng = new LatLng(latitude, longitude);
                if (lastLatLng != null) {
                    float distance = AMapUtils.calculateLineDistance(lastLatLng, currentLatLng);
                    totalDistance += distance;
                }
                //如果新的经纬度和上一个经纬度不同的话，进行增加点，如果相同，就不增加
                if (!currentLatLng.equals(lastLatLng)) {
                    polylineOptions.add(currentLatLng);
                }
                lastLatLng = currentLatLng;
                DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                //换算成带两位小数的公里数
                String kmDistance = decimalFormat.format(totalDistance / 1000);
                sportData.put("totalDistance", kmDistance);
                sportData.put("altitude", new DecimalFormat("##0.0").format(altitude));
                sportData.put("speed", new DecimalFormat("##0.00").format(speed));
                sportData.put("maxSpeed", new DecimalFormat("##0.00").format(maxSpeed));
                sportData.put("totalClimb", new DecimalFormat("##0.0").format(totalClimb));


                RxEvent rxEvent = new RxEvent();
                rxEvent.setType(RxEvent.POST_LOCATION);
                rxEvent.setMessage1(currentLatLng);
                rxEvent.setMessage2(polylineOptions);
                rxEvent.setMessage3(sportData);
                RxBus.getInstance().post(rxEvent);
                count++;
            } else {
                Logger.d(aMapLocation.getErrorCode());
            }
        }
    }

}
