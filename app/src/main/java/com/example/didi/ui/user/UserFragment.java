package com.example.didi.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.didi.R;

public class UserFragment extends Fragment {

    private UserViewModel mUserViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mUserViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        mUserViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}