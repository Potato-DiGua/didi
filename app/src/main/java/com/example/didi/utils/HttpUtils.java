package com.example.didi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HttpUtils {
    public static final MediaType JSON
        = MediaType.get("application/json; charset=utf-8");
    public static final String BASE_URL = "http://192.168.50.78:8080";

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
            cookieStore.put(httpUrl.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }).connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();


    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
