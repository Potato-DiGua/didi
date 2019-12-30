package com.example.didi.ui.notifications;

import android.app.NotificationManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {
    private MutableLiveData<String> text;
    public NotificationsViewModel()
    {
        text=new MutableLiveData<>();
        text.setValue("test 123");
    }
    public LiveData<String> getText(){
        return text;
    }
}
