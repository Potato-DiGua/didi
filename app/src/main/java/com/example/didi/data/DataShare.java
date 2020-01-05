package com.example.didi.data;

import com.example.didi.beans.PathBean;
import com.example.didi.beans.SearchBean;
import com.example.didi.beans.UserInfoBean;

import java.util.List;

public class DataShare {
    private static UserInfoBean user;
    private static SearchBean sSearchBean;
    private static List<PathBean> sPathBeans;

    public static List<PathBean> getPathBeans() {
        return sPathBeans;
    }

    public static void setPathBeans(List<PathBean> pathBeans) {
        sPathBeans = pathBeans;
    }

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
