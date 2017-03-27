package com.laluna_team.dtufeedbackv2.service.feedback;

import android.content.Context;
import android.util.Log;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackList;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lednh on 3/27/2017.
 */

public class FeedbackService {

    private static final String LOG_TAG = FeedbackService.class.getSimpleName();

    public interface FeedbackListLoadedCallback {
        public void onFeedbackListLoaded(List<Feedback> feedbackList);
    }

    public static void getAllFeedback(
            final Context mContext,
            Integer limit,
            String solved,
            Integer categoryId,
            Integer campusId,
            Integer userId,
            final FeedbackListLoadedCallback feedbackListLoadedCallback) {

        if(!NetworkUtils.isOnline(mContext)) {
            feedbackListLoadedCallback.onFeedbackListLoaded(null);
            return;
        }

        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        IFeedbackService feedbackService = retrofit.create(IFeedbackService.class);

        Call<FeedbackList> call = feedbackService
                .getAllFeedback(limit, solved, campusId, userId, categoryId);
        call.enqueue(new Callback<FeedbackList>() {
            @Override
            public void onResponse(Call<FeedbackList> call, Response<FeedbackList> response) {
                if(response.isSuccessful()) {
                    List<Feedback> feedbackList = response.body().getFeedbackList();
                    feedbackListLoadedCallback.onFeedbackListLoaded(feedbackList);
                } else {
                    try {
                        Log.e(LOG_TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    feedbackListLoadedCallback.onFeedbackListLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<FeedbackList> call, Throwable t) {
                t.printStackTrace();
                feedbackListLoadedCallback.onFeedbackListLoaded(null);
            }
        });
    }
}
