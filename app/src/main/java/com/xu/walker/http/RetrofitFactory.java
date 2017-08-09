package com.xu.walker.http;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xusn10 on 2017/6/22.
 */

public class RetrofitFactory {
    private static final String BASE_URL = "http://10.192.25.123:8888/api/";
    // private static final String BASE_URL = "http://112.90.177.71:8001/api/";
    private static final long TIME_OUT = 30;


    private static OkHttpClient getOkHttpClient() {
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InZpbGxhZ2UiLCJpYXQiOjE0OTc4NTg3NjEsImV4cCI6MTQ5OTk2MTQxOTc4N30.MleJUPtEj2_-tfxYJB9qGm9n7BCCt6f70T2wumA6EHQ";
        //缓存容量
//        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
//        //缓存路径
//        String cacheFile = context.getCacheDir() + "/http";
//        Cache cache = new Cache(new File(cacheFile), SIZE_OF_CACHE);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                //可以继续扩展
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS);
        //    .cache(cache)
        //添加统一的header
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.header("x-hzz-token", token);
                return chain.proceed(builder.build());
            }
        };
        //日志logger拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                if (s != null && s.length() >= 1) {
                    // Logger.d(s);
                    String begin = s.substring(0, 1);
                    if ("{".equals(begin)) {
                        //只打印json
                        Logger.d(s);
                    }
                }

            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(httpLoggingInterceptor);
        client.addInterceptor(headerInterceptor);
        return client.build();
    }

    private static String getToken() {

        return "";
    }

    public static WaterControlApi getApi() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build()
                .create(WaterControlApi.class);
    }


}
