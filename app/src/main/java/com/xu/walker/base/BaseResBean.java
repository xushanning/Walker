package com.xu.walker.base;

import java.io.Serializable;

/**
 * Created by xusn10 on 2017/6/8.
 */

public class BaseResBean<T> implements Serializable {
    /**
     * success : true
     * message : get feedback info success
     * data : 数据
     */

    private boolean success;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
