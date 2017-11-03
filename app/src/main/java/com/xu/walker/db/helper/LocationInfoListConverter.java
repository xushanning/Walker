package com.xu.walker.db.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xu.walker.bean.req.LocationInfoBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by xusn10 on 2017/10/20.
 * LocationInfoList与string之间的相互转换
 */

public class LocationInfoListConverter implements PropertyConverter<List<LocationInfoBean>, String> {
    private Gson gson = new Gson();

    @Override
    public List<LocationInfoBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            return gson.fromJson(databaseValue, new TypeToken<List<LocationInfoBean>>() {
            }.getType());
        }

    }

    @Override
    public String convertToDatabaseValue(List<LocationInfoBean> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {

            return gson.toJson(entityProperty);
        }

    }
}
