package com.xu.walker.ui.fragment.sport;

import com.xu.walker.base.IBasePresenter;
import com.xu.walker.base.IBaseView;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class SportContract {
    interface ISportView extends IBaseView {
        //设置速度
        void setSpeed(float speed);

        //设置里程
        void setMileage(float mileage);

        //设置时间
        void setTime();

        //设置均速
        void setAverageSpeed(float averageSpeed);

        //设置极速
        void setMaxSpeed(float maxSpeed);

        //设置海拔
        void setAltitude(float altitude);

        //设置爬升高度
        void setClimb(float climb);
    }

    interface ISportPresenter extends IBasePresenter<ISportView> {
        //注销rxbus
        void unSubscribeRxBus();
    }
}
