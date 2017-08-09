package com.xu.walker.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xusn10 on 2017/6/19.
 */
@Entity
public class LoginInfo {
    private String userName;
    private String passWord;
    private String token;
    @Generated(hash = 345610398)
    public LoginInfo(String userName, String passWord, String token) {
        this.userName = userName;
        this.passWord = passWord;
        this.token = token;
    }
    @Generated(hash = 1911824992)
    public LoginInfo() {
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return this.passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
