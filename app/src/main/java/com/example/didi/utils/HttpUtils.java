package com.example.didi.utils;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HttpUtils {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static final String BASE_URL = "http://192.168.43.251:8080";
    private static CookieJar cookieJar = new CookieJar() {
        public final Map<String, List<Cookie>> cookieStore = new HashMap<>();


        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
            cookieStore.put(httpUrl.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().cookieJar(cookieJar).connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();

    public static void init(Context context) {
        cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(context.getApplicationContext()));
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
