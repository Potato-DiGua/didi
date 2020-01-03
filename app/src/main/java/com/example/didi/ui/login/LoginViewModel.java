package com.example.didi.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.didi.R;
import com.example.didi.utils.Utils;
import com.example.didi.data.LoginRepository;
import com.example.didi.data.Result;
import com.example.didi.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String account, final String password,final int type) {
        // can be launched in a separate asynchronous job
        new Thread(new Runnable() {
            @Override
            public void run() {
                Result<LoggedInUser> result = loginRepository.login(account, password,type);

                if (result instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                    loginResult.postValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.postValue(new LoginResult(R.string.login_failed));
                }
            }
        }).start();
    }

    public void loginDataChanged(String account, String password) {
        if (!Utils.isAccountValid(account)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_account, null));
        } else if (!Utils.isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }



}
