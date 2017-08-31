package com.xu.walker.ui.activity.sportmap;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.xu.walker.base.IBasePresenter;
import com.xu.walker.base.IBaseView;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SportMapContract {
    interface ISportMapView extends IBaseView {
        //增加点
        void addPoint(LatLng latLng);

        //设置PolylineOptions
        void setPolylineOptions(PolylineOptions polylineOptions);

        //总里程
        void setTotalDistance(String distance);

        //当前速度
        void setSpeed(String speed);

        //显示运动时间
        void setTime(String time);
    }

    interface ISportMapPresenter extends IBasePresenter<ISportMapView> {
        //注销rxbus
        void unSubscribeRxBus();

        //告诉presenter需要获取一下PolylineOptions，每次创建activity或者resume之类的时候，重新从service中接收一下，可以实现直接显示没有描绘的轨迹
        void getPolylineOptions();
    }
}
