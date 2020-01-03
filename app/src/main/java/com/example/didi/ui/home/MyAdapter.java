package com.example.didi.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.didi.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> mList;
    private Button clickButton=null;
    public MyAdapter(List<String> list) {
        mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        View view=holder.itemView;
        TextView textView=view.findViewById(R.id.textView);
        textView.setText(mList.get(position));
        Button button=view.findViewById(R.id.btn_add);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickButton!=null)
                {
                    clickButton.setVisibility(View.INVISIBLE);
                    if(clickButton==button)
                    {
                        clickButton=null;
                        return;
                    }

                }
                button.setVisibility(View.VISIBLE);
                clickButton=button;
            }
        });

    }
    public void removeBtn()
    {
        if(clickButton!=null)
        {
            clickButton.setVisibility(View.INVISIBLE);
        }
        clickButton=null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
