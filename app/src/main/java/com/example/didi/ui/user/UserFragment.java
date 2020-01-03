package com.example.didi.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.didi.R;
import com.example.didi.beans.SendBean;
import com.example.didi.beans.UserInfoBean;
import com.example.didi.data.DataShare;
import com.example.didi.data.LoginRepository;
import com.example.didi.ui.login.LoginActivity;
import com.example.didi.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private Handler mHandler=new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mUserViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        final TextView tvAccount = root.findViewById(R.id.tv_account);
        final TextView tvNickName = root.findViewById(R.id.tv_nick_name);
        final TextView tvPhone = root.findViewById(R.id.tv_phone);
        final TextView tvSex = root.findViewById(R.id.tv_sex);
        Button button=root.findViewById(R.id.btn_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(LoginRepository.getInstance().logout(getActivity()))
                        {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            });
                        }else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"注销失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        mUserViewModel.getUser().observe(this, new Observer<UserInfoBean>() {
            @Override
            public void onChanged(UserInfoBean user) {
                tvAccount.setText(String.valueOf(user.getId()));
                tvNickName.setText(user.getNickName());
                tvPhone.setText(user.getPhone());
                String sex;
                if(user.getSex()==null)
                {
                    sex="未知";
                }else
                {
                    if(user.getSex().equals(String.valueOf(0)))
                        sex="男";
                    else
                        sex="女";
                }
                tvSex.setText(sex);
            }
        });

        updateView();
        return root;
    }
    private void updateFromInternet()
    {
        OkHttpClient okHttpClient = HttpUtils.getOkHttpClient();
        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(HttpUtils.BASE_URL + "/userinfo")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String json = response.body().string();
            Log.d("update",json);
            if (!json.isEmpty()) {
                SendBean<UserInfoBean> result = gson.fromJson(json
                        , new TypeToken<SendBean<UserInfoBean>>() {}.getType());
                if (result.getStatus().equals("ok")) {
                    UserInfoBean user=result.getData();
                    if(user!=null)
                    {
                        DataShare.setUser(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateView()
    {
        mUserViewModel.setUser(DataShare.getUser());
    }
}