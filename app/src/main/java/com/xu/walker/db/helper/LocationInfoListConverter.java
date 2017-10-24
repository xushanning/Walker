package com.xu.walker.db.helper;

import com.xu.walker.bean.LocationInfoBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by xusn10 on 2017/10/20.
 * LocationInfoList与string之间的相互转换
 */

public class LocationInfoListConverter implements PropertyConverter<List<LocationInfoBean>, String> {
    @Override
    public List<LocationInfoBean> convertToEntityProperty(String databaseValue) {
        return null;
    }

    @Override
    public String convertToDatabaseValue(List<LocationInfoBean> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {

            return null;
        }

    }
}
