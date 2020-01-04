package com.example.didi.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.didi.R;
import com.example.didi.data.DataShare;
import com.example.didi.ui.edit.EditActivity;

import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG="HomeFragment";
    private static final int REQUEST_EDIT=0x00;

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private LocationAdapter mLocationAdapter;
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

        }else if(mType==1){//司机页面
            root = inflater.inflate(R.layout.fragment_home_driver, container, false);
            mRecyclerView=root.findViewById(R.id.recycler_view);

            homeViewModel.getText().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> list) {
                    mLocationAdapter.setList(list);
                    mLocationAdapter.notifyDataSetChanged();
                }
            });
        }

        return root;
    }
    private void setActionBar()
    {
        ActionBar actionBar=getActivity().getActionBar();
        //设置标题
        if(actionBar!=null)
        {

            if(mType==0)//货主
            {
                actionBar.setTitle("路线");
            }else if(mType==1)//司机
            {
                actionBar.setTitle("滴滴物流");
            }
        }

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
                homeViewModel.update();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        if(mType==0)//货主
        {

        }else if(mType==1){//司机
            mLocationAdapter=new LocationAdapter(null);
            mRecyclerView.setAdapter(mLocationAdapter);
            // 设置布局
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
        homeViewModel.update();
        setActionBar();

    }
}