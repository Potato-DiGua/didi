package com.example.didi.ui.home;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.didi.beans.SearchBean;
import com.example.didi.beans.SendBean;
import com.example.didi.beans.UserInfoBean;
import com.example.didi.data.DataShare;
import com.example.didi.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<String>> mData;
    private MutableLiveData<List<UserInfoBean>> mSearchDrivers;

    public HomeViewModel() {
        mSearchDrivers = new MutableLiveData<>();
        mData = new MutableLiveData<>();
    }

    public LiveData<List<String>> getData() {
        return mData;
    }

    public LiveData<List<UserInfoBean>> getDrviers() {
        return mSearchDrivers;
    }

    public void updatePath() {
        Request request = new Request.Builder()
                .url(HttpUtils.BASE_URL + "/path")
                .build();
        HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                SendBean<List<String>> sendBean = gson.fromJson(json
                        , new TypeToken<SendBean<List<String>>>() {
                        }.getType());
                if (sendBean.getStatus().equals("ok")) {
                    mData.postValue(sendBean.getData());
                }
            }
        });
    }

    public void updateDriver(SearchBean searchBean) {

        if(!TextUtils.isEmpty(searchBean.getStart())&&!TextUtils.isEmpty(searchBean.getEnd()))
        {
            Gson gson = new Gson();
            String json = gson.toJson(searchBean);
            Request request = new Request.Builder()
                    .url(HttpUtils.BASE_URL + "/search")
                    .post(RequestBody.create(json, HttpUtils.JSON))
                    .build();
            HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String json = response.body().string();
                    Log.d("search",json);
                    Gson gson = new Gson();
                    SendBean<List<UserInfoBean>> sendBean = gson.fromJson(json
                            , new TypeToken<SendBean<List<UserInfoBean>>>() {
                            }.getType());
                    if (sendBean.getStatus().equals("ok")) {
                        mSearchDrivers.postValue(sendBean.getData());
                        DataShare.setSearchBean(searchBean);
                    }
                }
            });
        }
    }
}