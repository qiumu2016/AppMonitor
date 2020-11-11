package com.demo.appmonitor.http;

import okhttp3.Request;


public class BaseRequestBuilder {
    private static Request.Builder builder;
    public static Request.Builder getBuilder(String token_type, String access_token){
        if (builder == null) {
            synchronized (BaseRequestBuilder.class) {
                if (builder == null) {
                    builder = new Request.Builder()
                            .url("https://api.github.com/graphql")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", token_type + " " + access_token);
                }
            }
        }
        return builder;
    }
    public static Request.Builder getBuilder(){
        return builder;
    }
}


