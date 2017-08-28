package com.xu.walker.utils;

/**
 * Created by xusn10 on 2017/8/28.
 */

public class RxEvent {
    public static final String POST_LOCATION = "location";
    String type;
    Object message;

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public RxEvent(String type, Object message) {
        this.type = type;
        this.message = message;
    }
}
