package com.laluna_team.dtufeedbackv2.service.authentication;

import android.content.Context;
import android.content.Intent;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.LoginActivity;
import com.laluna_team.dtufeedbackv2.MainActivity;
import com.laluna_team.dtufeedbackv2.R;
import com.laluna_team.dtufeedbackv2.model.Auth;
import com.laluna_team.dtufeedbackv2.model.user.UserWrapper;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;

import java.io.File;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lednh on 4/3/2017.
 */

public class AuthService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static void login(final Context mContext, String email, String password) {
        if(email == null|| !VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.bad_email));
        } else if(password == null || password.length() < 6) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.bad_password));
        } else {
            Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
            IAuthService authService = retrofit.create(IAuthService.class);

            Call<Auth> call = authService.login(email, password);
            call.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    if(response.isSuccessful()) {
                        String token = response.body().getToken();

                        PreferenceUtils.saveAuthenticationToken(mContext, token);

                        getMe(mContext);
                    }
                    // TODO Implement error message here
                }

                @Override
                public void onFailure(Call<Auth> call, Throwable t) {
                    t.printStackTrace();
                    PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.something_went_wrong));
                }
            });
        }
    }

    public static void signup(final Context mContext,
                              String name, String email,
                              String password, String passwordConfirm, String avatarPath) {
        if(name == null || name.isEmpty()) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.bad_name));
        } else if(email == null
                || !VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.bad_email));
        } else if(password == null
                || password.length() < 6) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.bad_password));
        } else if(passwordConfirm == null
                || !passwordConfirm.equals(password)) {
            PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.password_mismatch));
        } else {
            Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
            IAuthService authService = retrofit.create(IAuthService.class);
            String plainTextType = mContext.getString(R.string.text_plain_type);
            String imageType = mContext.getString(R.string.image_type);

            RequestBody nameBody = RequestBody.create(
                    MediaType.parse(plainTextType), name);
            RequestBody emailBody = RequestBody.create(
                    MediaType.parse(plainTextType), email);
            RequestBody passwordBody = RequestBody.create(
                    MediaType.parse(plainTextType), password);
            MultipartBody.Part avatarMultipart = null;

            if(avatarPath != null) {
                File avatarFile = new File(avatarPath);
                if(avatarFile.exists()) {
                    RequestBody avatarBody = RequestBody.create(MediaType.parse(imageType), avatarFile);
                    avatarMultipart = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), avatarBody);
                }
            }

            Call<ResponseBody> call = authService.signup(nameBody, emailBody, passwordBody, avatarMultipart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        // TODO Implement error message here
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    PreferenceUtils.displayMessageAlert(mContext, mContext.getString(R.string.something_went_wrong));
                }
            });
        }
    }

    public static void getMe(final Context mContext) {
        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        IAuthService authService = retrofit.create(IAuthService.class);
        Call<UserWrapper> call = authService.getMe();
        call.enqueue(new Callback<UserWrapper>() {
            @Override
            public void onResponse(Call<UserWrapper> call, Response<UserWrapper> response) {
                if (response.isSuccessful()) {
                    PreferenceUtils.saveCurrentUser(mContext, response.body().getUser());
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    // TODO Display error message here
                }
            }

            @Override
            public void onFailure(Call<UserWrapper> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
