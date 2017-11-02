package com.xu.walker.db.bean;

import com.xu.walker.bean.req.LocationInfoBean;
import com.xu.walker.db.helper.LocationInfoListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by xusn10 on 2017/10/20.
 *
 * @author 许善宁
 */
@Entity
public class TrajectoryDBBean {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 轨迹的唯一id
     */
    private String trajectoryID;
    //运动是否完成
    private boolean isSportsComplete;
    //运动开始时间
    private int sportsBeginTime;
    //运动结束时间
    private int sportsEndTime;
    //运动时间
    private int sportsTime;
    /**
     * 运动距离
     */
    private int totalDistance;
    private String sportsType;
    //定位点信息
    @Convert(columnType = String.class, converter = LocationInfoListConverter.class)
    private List<LocationInfoBean> locationInfoBeans;
    @Generated(hash = 1173814084)
    public TrajectoryDBBean(Long id, String trajectoryID, boolean isSportsComplete,
            int sportsBeginTime, int sportsEndTime, int sportsTime,
            int totalDistance, String sportsType,
            List<LocationInfoBean> locationInfoBeans) {
        this.id = id;
        this.trajectoryID = trajectoryID;
        this.isSportsComplete = isSportsComplete;
        this.sportsBeginTime = sportsBeginTime;
        this.sportsEndTime = sportsEndTime;
        this.sportsTime = sportsTime;
        this.totalDistance = totalDistance;
        this.sportsType = sportsType;
        this.locationInfoBeans = locationInfoBeans;
    }
    @Generated(hash = 2124743559)
    public TrajectoryDBBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTrajectoryID() {
        return this.trajectoryID;
    }
    public void setTrajectoryID(String trajectoryID) {
        this.trajectoryID = trajectoryID;
    }
    public boolean getIsSportsComplete() {
        return this.isSportsComplete;
    }
    public void setIsSportsComplete(boolean isSportsComplete) {
        this.isSportsComplete = isSportsComplete;
    }
    public int getSportsBeginTime() {
        return this.sportsBeginTime;
    }
    public void setSportsBeginTime(int sportsBeginTime) {
        this.sportsBeginTime = sportsBeginTime;
    }
    public int getSportsEndTime() {
        return this.sportsEndTime;
    }
    public void setSportsEndTime(int sportsEndTime) {
        this.sportsEndTime = sportsEndTime;
    }
    public int getSportsTime() {
        return this.sportsTime;
    }
    public void setSportsTime(int sportsTime) {
        this.sportsTime = sportsTime;
    }
    public int getTotalDistance() {
        return this.totalDistance;
    }
    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }
    public String getSportsType() {
        return this.sportsType;
    }
    public void setSportsType(String sportsType) {
        this.sportsType = sportsType;
    }
    public List<LocationInfoBean> getLocationInfoBeans() {
        return this.locationInfoBeans;
    }
    public void setLocationInfoBeans(List<LocationInfoBean> locationInfoBeans) {
        this.locationInfoBeans = locationInfoBeans;
    }
    
 

}
