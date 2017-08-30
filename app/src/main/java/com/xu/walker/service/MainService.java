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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.bean.LocationBean;
import com.xu.walker.simulationdata.SportData;
import com.xu.walker.ui.activity.main.MainActivity;
import com.xu.walker.utils.RxBus;
import com.xu.walker.utils.RxEvent;
import com.xu.walker.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xusn10 on 2017/8/25.
 */

public class MainService extends Service implements AMapLocationListener {
    private MyBinder myBinder = new MyBinder();
    public AMapLocationClientOption mLocationOption;
    public AMapLocationClient mLocationClient;
    private List<LocationBean.DataBean> dataBeanList;
    private int count = 0;
    //存点专用
    private PolylineOptions polylineOptions;
    //存放运动参数，比如说一些，时速，里程，时间，均速等
    private Map<String, Object> sportData = new HashMap<>();
    //上一个点
    private LatLng lastLatLng;
    //运动距离
    private float totalDistance;
    //最快速度
    private float maxSpeed;
    //爬升
    private float totalClimb;
    //上一个记录的海拔
    private double lastAltitude = -1;

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
        public void startSport(long locationInterval) {
            Gson gson = new Gson();
            LocationBean locationBean = gson.fromJson(SportData.sportData, LocationBean.class);
            dataBeanList = locationBean.getData();
            polylineOptions = new PolylineOptions();
            initLocation(locationInterval);
            startTime();
        }

        public void stopSport() {

        }
    }

    private void startTime() {
        //开始发送时间
        // RxBus.getInstance().post();
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
                //模拟回家路线
                LocationBean.DataBean dataBean = dataBeanList.get(count);
//                double latitude = dataBean.getLatitude();
//                double longitude = dataBean.getLongitude();
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
