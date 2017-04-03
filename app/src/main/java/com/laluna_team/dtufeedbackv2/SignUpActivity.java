package com.laluna_team.dtufeedbackv2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.laluna_team.dtufeedbackv2.databinding.ActivitySignUpBinding;
import com.laluna_team.dtufeedbackv2.service.authentication.AuthService;
import com.laluna_team.dtufeedbackv2.service.authentication.IAuthService;
import com.laluna_team.dtufeedbackv2.utils.DataUtils;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpBinding mBinding;
    String mCurrentPhotoPath;
    private static final int REQUEST_PHOTO_PICKER = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mBinding.signUpButton.setOnClickListener(this);
        mBinding.avatarImageView.setOnClickListener(this);
        mBinding.loginTextView.setOnClickListener(this);

        NetworkUtils.isOnline(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_up_button:
                AuthService.signup(this, mBinding.nameEditText.getText().toString(),
                        mBinding.emailEditText.getText().toString(),
                        mBinding.passwordEditText.getText().toString(),
                        mBinding.passwordConfirmEditText.getText().toString(),
                        mCurrentPhotoPath);
                break;
            case R.id.avatar_imageView:
                ImagePicker.create(this)
                        .returnAfterFirst(true)
                        .imageTitle(getString(R.string.select_photo_title))
                        .single()
                        .start(REQUEST_PHOTO_PICKER);
                break;
            case R.id.login_textView:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PHOTO_PICKER && resultCode == RESULT_OK && data != null) {
            if(ImagePicker.getImages(data).size() > 0) {
                Image image = ImagePicker.getImages(data).get(0);
                mCurrentPhotoPath = image.getPath();
                File file = new File(mCurrentPhotoPath);
                Picasso.with(SignUpActivity.this)
                        .load(file)
                        .fit()
                        .into(mBinding.avatarImageView);
            }
        }
    }
}
