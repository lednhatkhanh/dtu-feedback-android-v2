package com.laluna_team.dtufeedbackv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

/**
 * Created by lednh on 3/27/2017.
 */

public class PreferenceUtils {
    public static final String AUTHENTICATION_SHARED_PREFS_NAME = "authentication";
    public static final String TOKEN_SHARED_PREFS_NAME = "token";
    public static final String USER_ID_SHARED_PREFS_NAME = "user_id";

    public static String getAuthenticationToken(Context mContext) {
        SharedPreferences sharedPreferences = mContext
                .getSharedPreferences(AUTHENTICATION_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_SHARED_PREFS_NAME, null);
    }

    public static void saveAuthenticationToken(Context mContext, String token) {
        SharedPreferences sharedPrefs = mContext
                .getSharedPreferences(AUTHENTICATION_SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TOKEN_SHARED_PREFS_NAME, token);
        editor.apply();
    }

    public static void displayMessageAlert(Context mContext, String message) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .create().show();
    }
}
