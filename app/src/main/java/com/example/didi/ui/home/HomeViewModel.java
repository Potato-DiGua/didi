package com.example.didi.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.didi.beans.SendBean;
import com.example.didi.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<String>> mData;

    public HomeViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<List<String>> getText() {
        return mData;
    }
    public void update()
    {
        Request request=new Request.Builder()
                .url(HttpUtils.BASE_URL+"/path")
                .build();

        HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json=response.body().string();
                Gson gson=new Gson();
                Log.d("path",json);
                SendBean<List<String>> sendBean=gson.fromJson(json
                        ,new TypeToken<SendBean<List<String>>>(){}.getType());
                if(sendBean.getStatus().equals("ok"))
                {
                    mData.postValue(sendBean.getData());
                }

            }
        });
    }
}