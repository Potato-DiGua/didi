package com.example.didi.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.didi.data.LoginRepository;
import com.example.didi.data.Result;
import com.example.didi.data.model.LoggedInUser;
import com.example.didi.R;

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

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // 检查邮箱是否有效
    private boolean isUserNameValid(String username) {
        if (username == null||username.trim().isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // 检查密码是否有效
    private boolean isPasswordValid(String password) {

        if(password==null)
            return false;
        int length=password.length();
        if(length>=8&&length<=16)
        {
            boolean hasNum=false,hasChar=false;
            for(int i=0;i<length;i++)
            {
                char ch=password.charAt(i);
                if(Character.isLetter(ch))
                {
                    hasChar=true;
                }
                else if(Character.isDigit(ch))
                {
                    hasNum=true;
                }
                if(hasChar&&hasNum)
                    return true;
            }
            return false;
        }
        return false;
    }
}
