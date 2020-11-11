package com.demo.appmonitor.http;

import okhttp3.OkHttpClient;

public class OkhttpUtil {
    private static OkHttpClient singleton;
    private OkhttpUtil(){

    }
    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkhttpUtil.class) {
                if (singleton == null){
                    singleton = new OkHttpClient();
                }
            }
        }
        return singleton;
    }
}
