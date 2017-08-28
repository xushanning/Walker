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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.orhanobut.logger.Logger;
import com.xu.walker.R;
import com.xu.walker.ui.activity.main.MainActivity;
import com.xu.walker.utils.RxBus;
import com.xu.walker.utils.RxEvent;

/**
 * Created by xusn10 on 2017/8/25.
 */

public class MainService extends Service implements AMapLocationListener {
    private MyBinder myBinder = new MyBinder();
    public AMapLocationClientOption mLocationOption;
    public AMapLocationClient mLocationClient;

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
        public void startSport() {
            initLocation();
        }
    }


    private void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                double longitude = aMapLocation.getLongitude();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = aMapLocation.getLatitude();//获取纬度
                // Logger.d(latitude + "   " + longitude);
                LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                RxBus.getInstance().post(new RxEvent(RxEvent.POST_LOCATION, latLonPoint));
            } else {
                Logger.d(aMapLocation.getErrorCode());
            }
        }
    }

}
