package com.laluna_team.dtufeedbackv2;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lednh on 3/27/2017.
 */

public class AppRetrofit {
    public static String BASE_URL = "http://hiepxun.xyz/api/";

    public static Retrofit getRetrofitInstance(final Context mContext) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest
                                .newBuilder()
                                .header("Accept", "application/json");
                        String token = PreferenceUtils.getAuthenticationToken(mContext);
                        setAuthHeader(builder, token);
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                        // TODO Find a way to refresh the token here....
                    }
                });

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build();
    }

    private static void setAuthHeader(Request.Builder builder, String token) {
        if(token != null) {
            builder.header("Authorization", String.format("Bearer %s", token));
        }
    }

    private static int refreshToken() {
        //Refresh token, synchronously, save it, and return result code
        //you might use retrofit here
        return 0;
    }

    private static int logout() {
        //logout your user
        return 0;
    }
}
