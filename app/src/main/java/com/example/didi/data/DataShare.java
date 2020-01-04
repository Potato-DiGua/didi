package com.example.didi.data;

import com.example.didi.beans.SearchBean;
import com.example.didi.beans.UserInfoBean;

public class DataShare {
    private static UserInfoBean user;
    private static SearchBean sSearchBean;

    public static SearchBean getSearchBean() {
        return sSearchBean;
    }

    public static void setSearchBean(SearchBean searchBean) {
        sSearchBean = searchBean;
    }

    public static UserInfoBean getUser() {
        return user;
    }

    public static void setUser(UserInfoBean user) {
        DataShare.user = user;
    }
}
