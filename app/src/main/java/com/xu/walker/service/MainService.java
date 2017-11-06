package com.xu.walker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
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
import com.xu.walker.bean.req.LocationBean;
import com.xu.walker.bean.req.LocationInfoBean;
import com.xu.walker.db.TrajectoryDBBeanDao;
import com.xu.walker.db.bean.TrajectoryDBBean;
import com.xu.walker.ui.activity.main.MainActivity;
import com.xu.walker.utils.ToastUtil;
import com.xu.walker.utils.rx.RxBus;
import com.xu.walker.utils.rx.RxEvent;
import com.xu.walker.utils.rx.TransformUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import io.reactivex.disposables.CompositeDisposable;
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


    private static final int MINUTE = 60;

    /**
     * 小时的上限
     */
    private static final int HOUR_LIMIT = 99;
    /**
     * 发送数据的disposable
     */
    private Disposable sendDataDisposable;

    /**
     * 运动距离
     */
    private float totalDistance;
    /**
     * 最快速度
     */
    private float maxSpeed;
    /**
     * 当前速度
     */
    private float currentSpeed;
    /**
     * 当前海拔
     */
    private double currentAltitude;
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
    private static final int INSERT_DB_INTERVAL = 5;
    private TrajectoryDBBeanDao trajectoryDBBeanDao;
    /**
     * 当前运动的轨迹的id
     */
    private String trajectoryID;
    private CompositeDisposable compositeDisposable;
    private PowerManager.WakeLock wakeLock;
    /**
     * 轨迹有效的定位点最少数量为2
     */
    private static final int AVAILABLE_LOCATION_COUNT = 2;

    /**
     * 当前位置的经度
     */
    private double lastLongitude;

    /**
     * 当前位置的纬度
     */
    private double lastLatitude;
    /**
     * 存放定位点信息的list
     */
    List<LocationInfoBean> locationInfoBeans;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        compositeDisposable = new CompositeDisposable();
        wakeUpCPU();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private void setForefround() {
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

    /**
     * 保持cpu在锁屏的情况下，依旧处于工作状态
     */
    private void wakeUpCPU() {
        Logger.d("保持cpu唤醒");
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainService.class.getName());
        wakeLock.acquire();
    }

    public class MyBinder extends Binder {
        /**
         * 检查数据库中是否有未完成的运动轨迹
         *
         * @param locationInterval 定位间隔
         */

        public void checkSportsFrDB(final int locationInterval, final String sportType) {
            Disposable checkDbDis =
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
                    }).observeOn(AndroidSchedulers.mainThread())
                            .switchIfEmpty(new Observable<List<TrajectoryDBBean>>() {
                                @Override
                                protected void subscribeActual(Observer<? super List<TrajectoryDBBean>> observer) {
                                    //数据库里没有数据，那么切换到主线程开始运动
                                    startSport(locationInterval, sportType);
                                    //结束发送
                                    observer.onComplete();
                                }
                            })
                            .compose(TransformUtils.<List<TrajectoryDBBean>>defaultSchedulers())
                            .subscribe(new Consumer<List<TrajectoryDBBean>>() {
                                @Override
                                public void accept(List<TrajectoryDBBean> trajectoryDBBeen) throws Exception {
                                    //数据库里有数据，那么通知fragment
                                    TrajectoryDBBean trajectoryDBBean = trajectoryDBBeen.get(0);
                                    int sportTime = trajectoryDBBean.getSportsTime();
                                    int distance = trajectoryDBBean.getTotalDistance();
                                    //int endTime = trajectoryDBBean.getSportsEndTime();
                                    RxEvent rxEvent = new RxEvent(RxEvent.POST_HAVE_UNCOMPLETE_SPORT
                                            , secToTime(sportTime)
                                            , new DecimalFormat("##0.00").format(distance / 1000));
                                    //rxEvent.setMessage3(secToTime(endTime));
                                    RxBus.getInstance().post(rxEvent);
                                }
                            });
            compositeDisposable.add(checkDbDis);

        }

        /**
         * 停止运动
         */
        public void stopSport() {
            //进行数据库操作
            updateTrajectoryData(true);
            //停止计时
            sendDataDisposable.dispose();
            mLocationClient.stopLocation();
            trajectoryID = "";
            totalDistance = 0;
            maxSpeed = 0;
            currentSpeed = 0;
            currentAltitude = 0;
            totalClimb = 0;
            lastAltitude = -1;
            recordSecond = 0;
            lastLongitude = 0;
            lastLatitude = 0;
        }

        /**
         * 重新开始
         *
         * @param locationInterval 定位间隔
         */
        public void restartSport(int locationInterval, String sportType) {
            startSport(locationInterval, sportType);
        }

        /**
         * 继续上一次
         */
        public void continueSport() {
            //设置全局的运动id

        }
    }

    /**
     * 开始运动
     *
     * @param locationInterval 定位的间隔
     */
    public void startSport(long locationInterval, String sportType) {
        //设置前台服务
        setForefround();
        //生成轨迹ID
        this.trajectoryID = UUID.randomUUID().toString().replaceAll("-", "");
        //设置运动id
        MyApplication.setSportID(trajectoryID);
        createTrajectory(sportType);
        initLocation(locationInterval);
        RxEvent rxEvent = new RxEvent(RxEvent.POST_SPORT_UI_TO_BEGIN);
        RxBus.getInstance().post(rxEvent);
    }


    /**
     * 开始发送数据
     */
    private void startSendData() {
        sendDataDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //这里进行写数据库操作(五秒钟写入一次)
                        if (aLong % INSERT_DB_INTERVAL == 0) {
                            updateTrajectoryData(false);
                        }
                    }
                })
                .compose(TransformUtils.<Long>allIo())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        recordSecond++;
                        //存放运动参数，比如说一些，时速，里程，时间，均速等
                        Map<String, Object> sportData = new HashMap<>(6);
                        //保留小数点
                        sportData.put("totalDistance", new DecimalFormat("##0.00").format(totalDistance / 1000));
                        sportData.put("altitude", new DecimalFormat("##0.0").format(currentAltitude));
                        sportData.put("speed", new DecimalFormat("##0.00").format(currentSpeed));
                        sportData.put("maxSpeed", new DecimalFormat("##0.00").format(maxSpeed));
                        sportData.put("totalClimb", new DecimalFormat("##0.0").format(totalClimb));
                        sportData.put("sportTime", secToTime(recordSecond));
                        LatLng lastLatLng = new LatLng(lastLatitude, lastLongitude);
                        RxEvent rxEvent = new RxEvent(RxEvent.POST_SPORT_INFO, lastLatLng, sportData);
                        //rxEvent.setMessage1(currentLatLng);
                        RxBus.getInstance().post(rxEvent);
                    }
                });
        compositeDisposable.add(sendDataDisposable);
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
                //刚开始就结束了会崩溃
                // Logger.d("是否已经完成" + isComplete);
                TrajectoryDBBean trajectoryDBBean = trajectoryDBBeanDao.queryBuilder().where(TrajectoryDBBeanDao.Properties.TrajectoryID.eq(trajectoryID)).unique();
                trajectoryDBBean.setIsSportsComplete(isComplete);
                trajectoryDBBean.setTotalDistance((int) totalDistance);
                trajectoryDBBean.setSportsEndTime((int) (System.currentTimeMillis() / 1000));
                trajectoryDBBean.setSportsTime(recordSecond);
                trajectoryDBBean.setLocationInfoBeans(locationInfoBeans);
                trajectoryDBBeanDao.update(trajectoryDBBean);
                if (isComplete && locationInfoBeans.size() < AVAILABLE_LOCATION_COUNT) {
                    trajectoryDBBeanDao.delete(trajectoryDBBean);
                    ToastUtil.toastShort(MainService.this, "距离太短，不记录本次轨迹！");
                }
                //什么都不发射，完成数据库操作就结束了
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 用于巡河的时候，生成一条轨迹
     */
    private void createTrajectory(final String sportType) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                //切换子线程进行数据库操作
                TrajectoryDBBean trajectoryDBBean = new TrajectoryDBBean();
                trajectoryDBBean.setIsSportsComplete(false);
                trajectoryDBBean.setSportsBeginTime((int) (System.currentTimeMillis() / 1000));
                trajectoryDBBean.setTrajectoryID(trajectoryID);
                trajectoryDBBean.setSportsEndTime(0);
                //初始化s
                locationInfoBeans = new ArrayList<>();
                trajectoryDBBean.setLocationInfoBeans(locationInfoBeans);
                trajectoryDBBean.setSportsTime(0);
                trajectoryDBBean.setSportsType(sportType);
                trajectoryDBBeanDao.insert(trajectoryDBBean);
                // 初始化完了轨迹数据后，进行发送数据
                startSendData();
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
                //获取当前定位结果来源，如网络定位结果，详见定位类型表
                double currentLongitude = aMapLocation.getLongitude();
                //获取纬度
                double currentLatitude = aMapLocation.getLatitude();
                //获取海拔高度
                currentAltitude = aMapLocation.getAltitude();
                LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                //判断是跟上一次的定位一样
                if (!(currentLongitude == lastLongitude && currentLatitude == lastLatitude)) {
                    //不一样，说明位置移动了，更新beans
                    LocationInfoBean locationInfoBean = new LocationInfoBean();
                    locationInfoBean.setLatitude(currentLatitude);
                    locationInfoBean.setLongitude(currentLongitude);
                    locationInfoBean.setTime((int) (System.currentTimeMillis() / 1000));
                    locationInfoBean.setAltitude(currentAltitude);
                    locationInfoBeans.add(locationInfoBean);

                    //获取速度
                    currentSpeed = aMapLocation.getSpeed();
                    if (currentSpeed > maxSpeed) {
                        //获取最快速度
                        maxSpeed = currentSpeed;
                    }
                    if (lastAltitude != -1) {
                        double climb = currentAltitude - lastAltitude;
                        lastAltitude = currentAltitude;
                        totalClimb += climb;
                    }
                    //防止第一个点的时候，默认值为0
                    if (lastLatitude != 0 && lastLongitude != 0) {
                        LatLng lastLatLng = new LatLng(lastLatitude, lastLongitude);
                        float distance = AMapUtils.calculateLineDistance(lastLatLng, currentLatLng);
                        totalDistance += distance;
                    }
                    lastLatitude = currentLatitude;
                    lastLongitude = currentLongitude;
                }


                int satelliteCount = aMapLocation.getSatellites();//获取卫星的个数，用于控制gps信号的强弱

                float bearing = aMapLocation.getBearing();//获取方向角


            } else {
                Logger.d(aMapLocation.getErrorCode());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }
        stopForeground(true);
        compositeDisposable.clear();
        Logger.d("service被销毁了");
    }

}
