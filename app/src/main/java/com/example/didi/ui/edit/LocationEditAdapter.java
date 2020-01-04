package com.example.didi.ui.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.didi.R;

import java.util.List;

public class LocationEditAdapter extends RecyclerView.Adapter<LocationEditAdapter.MyViewHolder> {
    public List<String> mList;
    public LocationEditAdapter(List<String> list) {
        mList = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_edit_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        View view=holder.itemView;
        EditText editText=view.findViewById(R.id.edit_text);
        editText.setText(mList.get(position));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                mList.set(position,editText.getText().toString());
            }
        });
        ImageButton addBtn=view.findViewById(R.id.imgBtn_add);
        ImageButton removeBtn=view.findViewById(R.id.imgBtn_remove);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>=mList.size()-1)
                {
                    mList.add("");
                }else{
                    mList.add(position+1,"");
                }
                notifyDataSetChanged();
            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });

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
