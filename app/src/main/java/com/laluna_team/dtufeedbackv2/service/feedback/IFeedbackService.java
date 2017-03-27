package com.laluna_team.dtufeedbackv2.service.feedback;

import com.laluna_team.dtufeedbackv2.model.feedback.FeedbackList;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
