package com.laluna_team.dtufeedbackv2.service.feedback;

import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackList;
import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackWrapper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lednh on 3/27/2017.
 */

public interface IFeedbackService {
    @GET("feedbacks")
    Call<FeedbackList> getAllFeedback(@Query("limit") Integer limit,
                                      @Query("solved") String solved,
                                      @Query("campus_id") Integer campusId,
                                      @Query("user_id") Integer userId,
                                      @Query("category_id") Integer categoryId);

    @Multipart
    @POST("feedbacks")
    Call<FeedbackWrapper> createFeedback(@Part("title") RequestBody title,
                                         @Part("description") RequestBody description,
                                         @Part("location") RequestBody location,
                                         @Part("campus_id") RequestBody campusId,
                                         @Part("category_id") RequestBody categoryId,
                                         @Part MultipartBody.Part image);
    @GET("feedbacks/{id}")
    Call<FeedbackWrapper> getFeedbackById(@Path("id") int feedbackId);

    @GET("feedbacks/{id}/toggleSolved")
    Call<FeedbackWrapper> toggleFeedback(@Path("id") int feedbackId);
}
