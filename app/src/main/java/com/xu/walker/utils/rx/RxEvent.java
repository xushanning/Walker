package com.xu.walker.utils.rx;

/**
 * Created by xusn10 on 2017/8/28.
 */

public class RxEvent {
    //发送位置信息
    public static final String POST_LOCATION = "location";
    //发送运动时间
    public static final String POST_SPORT_TIME = "sport_time";
    private String type;
    private Object message1;
    private Object message2;
    private Object message3;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMessage1() {
        return message1;
    }

    public void setMessage1(Object message1) {
        this.message1 = message1;
    }

    public Object getMessage2() {
        return message2;
    }

    public void setMessage2(Object message2) {
        this.message2 = message2;
    }

    public Object getMessage3() {
        return message3;
    }

    public void setMessage3(Object message3) {
        this.message3 = message3;
    }
}
