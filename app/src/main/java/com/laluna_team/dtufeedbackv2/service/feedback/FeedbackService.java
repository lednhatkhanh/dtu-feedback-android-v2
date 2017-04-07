package com.laluna_team.dtufeedbackv2.service.feedback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.MainActivity;
import com.laluna_team.dtufeedbackv2.R;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackList;
import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackWrapper;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public static void addNewFeedback(final Context mContext,
                                      String title,
                                      String location,
                                      int campusId,
                                      int categoryId,
                                      String filePath,
                                      String description) {
        if (title == null || title.isEmpty()) {
            PreferenceUtils.displayMessageAlert(mContext,
                    mContext.getString(R.string.title_required));
        } else if (location == null || location.isEmpty()) {
            PreferenceUtils.displayMessageAlert(mContext,
                    mContext.getString(R.string.location_required));
        } else if (description == null || description.isEmpty()) {
            PreferenceUtils.displayMessageAlert(mContext,
                    mContext.getString(R.string.description_required));
        } else if(description.length() < 10) {
            PreferenceUtils.displayMessageAlert(mContext,
                    mContext.getString(R.string.description_too_short));
        } else {
            Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
            IFeedbackService feedbackService = retrofit.create(IFeedbackService.class);

            RequestBody titleBody = RequestBody.create(MediaType.parse(
                    mContext.getString(R.string.text_plain_type)), title);
            RequestBody locationBody = RequestBody.create(MediaType.parse(
                    mContext.getString(R.string.text_plain_type)), location);
            RequestBody campusIdBody = RequestBody.create(MediaType.parse(
                    mContext.getString(R.string.text_plain_type)), Integer.toString(campusId));
            RequestBody categoryIdBody = RequestBody.create(MediaType.parse(
                    mContext.getString(R.string.text_plain_type)), Integer.toString(categoryId));
            RequestBody descriptionBody = RequestBody.create(MediaType.parse(
                    mContext.getString(R.string.text_plain_type)), description);

            MultipartBody.Part imageMultiPart = null;
            if(filePath != null && !filePath.isEmpty()) {
                File imageFile = new File(filePath);
                if(imageFile.exists()) {
                    RequestBody  imageBody = RequestBody.create(MediaType.parse(
                            mContext.getString(R.string.image_type)), imageFile);
                    imageMultiPart =
                            MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
                }
            }

            Call<FeedbackWrapper> call = feedbackService.createFeedback(
                    titleBody, descriptionBody, locationBody, campusIdBody, categoryIdBody, imageMultiPart);
            call.enqueue(new Callback<FeedbackWrapper>() {
                @Override
                public void onResponse(Call<FeedbackWrapper> call, Response<FeedbackWrapper> response) {
                    if(response.isSuccessful()) {
                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                    } else {
                        // TODO Display error here
                    }
                }

                @Override
                public void onFailure(Call<FeedbackWrapper> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
