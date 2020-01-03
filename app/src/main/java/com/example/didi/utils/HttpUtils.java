package com.example.didi.utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HttpUtils {
    private static OkHttpClient okHttpClient=new OkHttpClient();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static final String BASE_URL="http://192.168.43.251:8080";
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
