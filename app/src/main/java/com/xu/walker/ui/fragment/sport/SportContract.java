package com.xu.walker.ui.fragment.sport;

import android.content.Context;

import com.xu.walker.base.IBasePresenter;
import com.xu.walker.base.IBaseView;

/**
 * Created by xusn10 on 2017/8/7.
 *
 * @author xu
 */

public class SportContract {
    interface ISportView extends IBaseView {
        /**
         * 设置速度
         *
         * @param speed 速度
         */
        void setSpeed(String speed);

        /**
         * 设置里程
         *
         * @param mileage 里程
         */
        void setMileage(String mileage);

        /**
         * 设置时间
         *
         * @param time 运动时间
         */
        void setTime(String time);

        /**
         * 设置均速
         *
         * @param averageSpeed 均速
         */
        void setAverageSpeed(float averageSpeed);

        /**
         * 设置极速
         *
         * @param maxSpeed 极速
         */

        void setMaxSpeed(String maxSpeed);

        /**
         * 设置海拔
         *
         * @param altitude 海拔
         */
        void setAltitude(String altitude);

        /**
         * 设置爬升高度
         *
         * @param climb 爬升高度
         */
        void setClimb(String climb);

        /**
         * 设置title
         *
         * @param title 标题
         */
        void setTitle(String title);

        /**
         * 变更继续运动的UI
         *
         * @param sportDistance 运动距离
         */
        void showContinueSportUI(String sportDistance, String sportTime);
    }

    interface ISportPresenter extends IBasePresenter<ISportView> {
        /**
         * 注销rxbus
         */
        void unSubscribeRxBus();


        /**
         * 结束运动
         */
        void stopSport();

        /**
         * 从数据库查看是否有未完成的运动
         *
         * @param locationInterval 定位间隔
         * @param sportType        运动类型
         */
        void checkSportsFrDB(int locationInterval, String sportType);

        /**
         * 开始新的运动
         *
         * @param sportType        运动类型
         * @param locationInterval 定位间隔
         */
        void reStartSports(int locationInterval, String sportType);

        /**
         * 继续上一次
         */
        void continueSports();

        /**
         * 绑定服务
         *
         * @param context 上下文
         */
        void bindService(Context context);

        /**
         * 解绑服务
         *
         * @param context 上下文
         */
        void unBindService(Context context);
    }
}
