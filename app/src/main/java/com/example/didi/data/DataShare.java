package com.example.didi.data;

import com.example.didi.beans.UserInfoBean;

public class DataShare {
    private static UserInfoBean user;

    public static UserInfoBean getUser() {
        return user;
    }

    public static void setUser(UserInfoBean user) {
        DataShare.user = user;
    }
}
