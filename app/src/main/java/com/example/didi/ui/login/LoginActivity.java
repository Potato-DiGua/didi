package com.example.didi.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.didi.MainActivity;
import com.example.didi.R;
import com.example.didi.beans.LoginBean;
import com.example.didi.data.LoginRepository;
import com.example.didi.ui.register.RegisterActivity;
import com.example.didi.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private RadioGroup mRadioGroup;
    private EditText accountEditText;
    private EditText passwordEditText;

    private Handler mHandler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        accountEditText = findViewById(R.id.account);
        passwordEditText = findViewById(R.id.password);



        final Button loginButton = findViewById(R.id.btn_login);
        final Button registerButton = findViewById(R.id.btn_register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getAccountError() != null) {
                    accountEditText.setError(getString(loginFormState.getAccountError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (result == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);

                if (result) {
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    if(!TextUtils.isEmpty(accountEditText.getText().toString()))
                    {
                        Utils.saveUser(LoginActivity.this,new LoginBean(accountEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                getCheckedIndex()));
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }


            }
        });

        //监听文本变化
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(accountEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        mRadioGroup = findViewById(R.id.radio_group);
        accountEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //软键盘点击回车登录
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(accountEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            getCheckedIndex());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(accountEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        getCheckedIndex());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        quickLogin();
    }
    private void quickLogin()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(LoginRepository.getInstance().isLoggedIn())
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            LoginBean loginBean=Utils.loadUser(LoginActivity.this);
                            if(loginBean!=null)
                            {
                                accountEditText.setText(loginBean.getPhone());
                                passwordEditText.setText(loginBean.getPwd());
                                mRadioGroup.check(loginBean.getType()==0?R.id.radio_btn_owner:R.id.radio_btn_driver);
                            }
                        }
                    });

                }
            }
        }).start();

    }

    /**
     * 获取选中的单选按钮的序号 0为货主，1为司机
     *
     * @return
     */
    private int getCheckedIndex() {
        return mRadioGroup.getCheckedRadioButtonId() == R.id.radio_btn_owner ? 0 : 1;
    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
