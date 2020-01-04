package com.example.didi.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.didi.R;
import com.example.didi.beans.SearchBean;
import com.example.didi.beans.UserInfoBean;
import com.example.didi.data.DataShare;
import com.example.didi.ui.edit.EditActivity;

import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG="HomeFragment";
    private static final int REQUEST_EDIT=0x00;

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private LocationAdapter mLocationAdapter;
    private DriverAdapter mDriverAdapter;
    private int mType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mType=DataShare.getUser().getType();
        setHasOptionsMenu(true);
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root=null;
        if(mType==0)//货主页面
        {
            root=inflater.inflate(R.layout.fragment_home_owner,container,false);
            mRecyclerView=root.findViewById(R.id.recycler_view);
            EditText startEv=root.findViewById(R.id.et_start);
            EditText endEv=root.findViewById(R.id.et_end);
            Button button=root.findViewById(R.id.btn_search);
            //搜索按钮
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchBean searchBean=new SearchBean();
                    searchBean.setStart(startEv.getText().toString());
                    searchBean.setEnd(endEv.getText().toString());
                    homeViewModel.updateDriver(searchBean);
                }
            });
            //司机数据更新
            homeViewModel.getDrviers().observe(this, new Observer<List<UserInfoBean>>() {
                @Override
                public void onChanged(List<UserInfoBean> userInfoBeans) {
                    mDriverAdapter.setList(userInfoBeans);
                    mDriverAdapter.notifyDataSetChanged();
                }
            });

        }else if(mType==1){//司机页面
            root = inflater.inflate(R.layout.fragment_home_driver, container, false);
            mRecyclerView=root.findViewById(R.id.recycler_view);

            homeViewModel.getData().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> list) {
                    mLocationAdapter.setList(list);
                    mLocationAdapter.notifyDataSetChanged();
                }
            });
        }

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(mType==1)//司机
        {
            inflater.inflate(R.menu.menu_driver_home,menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(mType==1)//司机
        {
            if(id==R.id.menu_edit)
            {
                Intent intent=new Intent(getActivity(),EditActivity.class);
                if(mLocationAdapter.getList()!=null&&mLocationAdapter.getList().size()>0)
                {
                    Log.d(TAG,"list不为空");
                    String[] strings=new String[mLocationAdapter.getList().size()];
                    mLocationAdapter.getList().toArray(strings);
                    intent.putExtra(EditActivity.EXTRA_DATA,strings);
                }
                startActivityForResult(intent,REQUEST_EDIT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== FragmentActivity.RESULT_OK)
        {
            if(requestCode==REQUEST_EDIT)
            {
                homeViewModel.updatePath();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // 设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if(mType==0)//货主
        {
            mDriverAdapter=new DriverAdapter(null,getActivity());
            mRecyclerView.setAdapter(mDriverAdapter);

        }else if(mType==1){//司机
            mLocationAdapter=new LocationAdapter(null);
            mRecyclerView.setAdapter(mLocationAdapter);

        }
        homeViewModel.updatePath();

    }
}