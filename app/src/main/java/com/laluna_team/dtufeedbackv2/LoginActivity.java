package com.laluna_team.dtufeedbackv2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.laluna_team.dtufeedbackv2.databinding.ActivityLoginBinding;
import com.laluna_team.dtufeedbackv2.service.authentication.AuthService;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getSupportActionBar().hide();

        mBinding.loginButton.setOnClickListener(this);
        mBinding.signUpTextView.setOnClickListener(this);

        NetworkUtils.isOnline(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.login_button:
                AuthService.login(this, mBinding.emailEditText.getText().toString(),
                        mBinding.passwordEditText.getText().toString());
                break;
            case R.id.sign_up_textView:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}
