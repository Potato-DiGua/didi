package com.example.didi.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.didi.R;
import com.example.didi.beans.UserInfoBean;
import com.example.didi.ui.driverdetail.DriverDetailActivity;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    private List<UserInfoBean> mList;
    private Context mContext;
    public DriverAdapter(List<UserInfoBean> list, Context context) {
        mList = list;
        mContext=context;
    }

    public List<UserInfoBean> getList() {
        return mList;
    }

    public void setList(List<UserInfoBean> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public DriverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new DriverAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAdapter.MyViewHolder holder, int position) {
        View view=holder.itemView;
        UserInfoBean user=mList.get(position);
        TextView locationTv=view.findViewById(R.id.tv_location);
        locationTv.setText(user.getNickName()+"-"+user.getPhone());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, DriverDetailActivity.class);
                i.putExtra(DriverDetailActivity.EXTRA_NICK_NAME,user.getNickName());
                i.putExtra(DriverDetailActivity.EXTRA_PHONE,user.getPhone());
                i.putExtra(DriverDetailActivity.EXTRA_ID,user.getId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
