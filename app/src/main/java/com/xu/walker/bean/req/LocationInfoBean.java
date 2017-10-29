package com.xu.walker.bean.req;

/**
 * Created by xusn10 on 2017/10/20.
 *
 * @author 许善宁
 */

public class LocationInfoBean {

    /**
     * longitude : 111.111
     * latitude : 111.111
     * time : 111111
     * altitude : 111.11
     */

    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 坐标点的时间（时间戳的形式）
     */
    private int time;

    /**
     * 海拔
     */
    private double altitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
