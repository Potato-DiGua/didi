package com.example.didi;

import com.example.didi.beans.RegisterBean;
import com.example.didi.utils.HttpUtils;
import com.google.gson.Gson;

import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Gson gson=new Gson();
        RegisterBean registerBeans=new RegisterBean();
        registerBeans.setPhone("15227591135");
        registerBeans.setNickName("zzl");
        registerBeans.setPwd("zzl1234");
        registerBeans.setType(1);
        OkHttpClient okHttpClient=HttpUtils.getOkHttpClient();
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = gson.toJson(registerBeans);
        Request request = new Request.Builder()
                .url("http://localhost:8080/register")
                .post(RequestBody.create(requestBody,HttpUtils.JSON))
                .build();
        System.out.println(requestBody);

        try (Response response = okHttpClient.newCall(request).execute()) {
            System.out.println(response.body().string());
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        assertEquals(4, 2 + 2);
    }
}