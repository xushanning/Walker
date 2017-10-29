package com.xu.walker.bean.req;

import java.util.List;

/**
 * Created by xusn10 on 2017/8/29.
 */

public class LocationBean {

    /**
     * longitude : 116.372987
     * latitude : 39.909083
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private double longitude;
        private double latitude;

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
    }
}
