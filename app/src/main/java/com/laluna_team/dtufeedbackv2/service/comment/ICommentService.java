package com.laluna_team.dtufeedbackv2.service.comment;

import com.laluna_team.dtufeedbackv2.model.comment.CommentList;
import com.laluna_team.dtufeedbackv2.model.comment.CommentWrapper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lednh on 4/7/2017.
 */

public interface ICommentService {
    @GET("feedbacks/{feedbackId}/comments")
    Call<CommentList> getAllComments(@Path("feedbackId") int feedbackId, @Query("limit") Integer limit);

    @FormUrlEncoded
    @POST("feedbacks/{feedbackId}/comments")
    Call<CommentWrapper> addComment(@Path("feedbackId") int feedbackId, @Field("content") String content);
}
