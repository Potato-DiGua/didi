package com.example.didi.data;

import com.example.didi.data.model.LoggedInUser;

/**
 * 该类从服务器请求身份验证和用户信息，并维护登录状态和用户凭据信息的内存缓存。
 *
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    //单例 懒汉模式
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String account, String password,int type) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(account, password,type);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
