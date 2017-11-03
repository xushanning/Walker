package com.xu.walker.utils.rx;

/**
 * Created by xusn10 on 2017/8/28.
 */

public class RxEvent {
    /**
     * 发送运动信息
     */
    public static final String POST_SPORT_INFO = "sport_info";
    /**
     * 有未完成的运动
     */
    public static final String POST_HAVE_UNCOMPLETE_SPORT = "have_uncomplete_sport";
    /**
     * 运动碎片的ui变为开始状态
     */
    public static final String POST_SPORT_UI_TO_BEGIN = "sport_ui_to_begin";
    private String type;
    private Object message1;
    private Object message2;
    private Object message3;

    public RxEvent(String type) {
        this.type = type;
    }

    public RxEvent(String type, Object message1) {
        this(type);
        this.message1 = message1;
    }

    public RxEvent(String type, Object message1, Object message2) {
        this(type, message1);
        this.message2 = message2;
    }

    public RxEvent(String type, Object message1, Object message2, Object message3) {
        this(type, message1, message2);
        this.message3 = message3;
    }

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
