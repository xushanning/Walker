package com.xu.walker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xu.walker.db.DaoMaster;
import com.xu.walker.db.DaoSession;

/**
 * Created by xusn10 on 2017/8/7.
 */

public class MyApplication extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //logger开关
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
        mInstance = this;

        initDatabase();
    }

    public static MyApplication getInstances() {
        return mInstance;
    }



    //初始化数据库
    private void initDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "walker-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
