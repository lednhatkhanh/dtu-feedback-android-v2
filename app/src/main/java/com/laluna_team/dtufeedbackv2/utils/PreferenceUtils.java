package com.laluna_team.dtufeedbackv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.laluna_team.dtufeedbackv2.model.user.User;

/**
 * Created by lednh on 3/27/2017.
 */

public class PreferenceUtils {
    public static final String AUTHENTICATION_SHARED_PREFS_NAME = "authentication";
    public static final String TOKEN_SHARED_PREFS_NAME = "token";
    public static final String USER_ID_SHARED_PREFS_NAME = "user_id";

    public static final String CURRENT_USER_SHARED_PREFS_NAME = "currentUser";
    public static final String CURRENT_USER_ID_PREFS_NAME = "currentUserId";
    public static final String CURRENT_USER_NAME_PREFS_NAME = "currentUserName";
    public static final String CURRENT_USER_EMAIL_PREFS_NAME = "currentUserEmail";
    public static final String CURRENT_USER_AVATAR_PREFS_NAME = "currentUserAvatar";

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

    public static void saveCurrentUser(Context mContext, User user) {
        SharedPreferences sharedPreferences = mContext
                .getSharedPreferences(CURRENT_USER_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(CURRENT_USER_ID_PREFS_NAME, user.getId());
        editor.putString(CURRENT_USER_NAME_PREFS_NAME, user.getName());
        editor.putString(CURRENT_USER_EMAIL_PREFS_NAME, user.getEmail());
        editor.putString(CURRENT_USER_AVATAR_PREFS_NAME, user.getAvatar());

        editor.apply();
    }

    public static User getCurrentUser(Context mContext) {
        SharedPreferences sharedPreferences = mContext
                .getSharedPreferences(CURRENT_USER_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        User currentUser = new User();
        currentUser.setName(sharedPreferences.getString(CURRENT_USER_NAME_PREFS_NAME, null));
        currentUser.setId(sharedPreferences.getInt(CURRENT_USER_ID_PREFS_NAME, 0));
        currentUser.setEmail(sharedPreferences.getString(CURRENT_USER_EMAIL_PREFS_NAME, null));
        currentUser.setAvatar(sharedPreferences.getString(CURRENT_USER_AVATAR_PREFS_NAME, null));
        return currentUser;
    }

    public static void displayMessageAlert(Context mContext, String message) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .create().show();
    }
}
