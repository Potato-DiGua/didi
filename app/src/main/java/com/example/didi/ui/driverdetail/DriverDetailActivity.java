package com.example.didi.ui.driverdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.didi.R;
import com.example.didi.beans.SearchBean;
import com.example.didi.beans.SendBean;
import com.example.didi.data.DataShare;
import com.example.didi.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DriverDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_NICK_NAME = "nick_name";
    public static final String EXTRA_PHONE = "phone";
    private int mID;
    private Handler mHandler=new Handler();
    private TextView mPathTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        TextView nickNameTv = findViewById(R.id.tv_nick_name);
        TextView phoneTv = findViewById(R.id.tv_phone);
        mPathTv = findViewById(R.id.tv_path);
        Button payBtn = findViewById(R.id.btn_pay);

        //从intent读取传入数据
        Intent intent = getIntent();
        nickNameTv.setText(intent.getStringExtra(EXTRA_NICK_NAME));
        phoneTv.setText(intent.getStringExtra(EXTRA_PHONE));
        mID = intent.getIntExtra(EXTRA_ID, 0);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setActionBar();
        getPath();
    }

    private void getPath()
    {
        Request request=new Request.Builder()
                .url(HttpUtils.BASE_URL+"/path?id="+mID)
                .build();
        HttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                SendBean<String[]> sendBean = gson.fromJson(json
                        , new TypeToken<SendBean<String[]>>() {
                        }.getType());
                String[] strings=sendBean.getData();
                if(strings!=null)
                {
                    String s= TextUtils.join("-",strings);
                    SearchBean searchBean=DataShare.getSearchBean();
                    String[] strings1=s.split(searchBean.getStart());
                    String text;
                    if(strings1[0].contains(searchBean.getEnd()))
                    {
                        String[] strings2=strings1[0].split(searchBean.getEnd());
                        text=strings2[0]+"<font color=\"#D81B60\">"+searchBean.getEnd()+strings2[1]+searchBean.getStart()+"</font>"+strings1[1];
                    }else {
                        String[] strings2=strings1[1].split(searchBean.getEnd());
                        text=strings1[0]+"<font color=\"#D81B60\">"+searchBean.getStart()+strings2[0]+searchBean.getEnd()+"</font>"+strings2[1];
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPathTv.setText(Html.fromHtml(text));
                        }
                    });
                }
            }
        });
    }
    private void setActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
