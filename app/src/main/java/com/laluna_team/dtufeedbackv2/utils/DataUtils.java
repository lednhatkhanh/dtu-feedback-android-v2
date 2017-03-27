package com.laluna_team.dtufeedbackv2.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by lednh on 3/27/2017.
 */

public class DataUtils {
    public static CharSequence formatTimeToDisplay(Context mContext, Date time) {
        return DateUtils.getRelativeDateTimeString(
                mContext, time.getTime(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_SHOW_TIME);
    }
}
