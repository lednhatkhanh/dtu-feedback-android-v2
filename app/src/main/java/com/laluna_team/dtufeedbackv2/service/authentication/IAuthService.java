package com.laluna_team.dtufeedbackv2.service.authentication;

import com.laluna_team.dtufeedbackv2.model.Auth;
import com.laluna_team.dtufeedbackv2.model.user.User;
import com.laluna_team.dtufeedbackv2.model.user.UserWrapper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lednh on 4/3/2017.
 */

public interface IAuthService {
    @FormUrlEncoded
    @POST("auth/login")
    Call<Auth> login(@Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("auth/signup")
    Call<ResponseBody> signup(@Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part MultipartBody.Part avatar);

    @GET("me")
    Call<UserWrapper> getMe();
}
