package com.laluna_team.dtufeedbackv2.service.comment;

import android.content.Context;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.model.comment.Comment;
import com.laluna_team.dtufeedbackv2.model.comment.CommentList;
import com.laluna_team.dtufeedbackv2.model.comment.CommentWrapper;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lednh on 4/7/2017.
 */

public class CommentService {

    public interface OnCommentListLoadedCallback {
        public void onCommentListLoaded(List<Comment> commentList);
    }

    public static void getAllComments(final Context mContext,int limit, int feedbackId,
                                      final OnCommentListLoadedCallback onCommentListLoadedCallback) {
        NetworkUtils.isOnline(mContext);

        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        ICommentService commentService = retrofit.create(ICommentService.class);
        Call<CommentList> call = commentService.getAllComments(feedbackId,limit);
        call.enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                if(response.isSuccessful()) {
                    onCommentListLoadedCallback.onCommentListLoaded(response.body().getComments());
                } else {
                    onCommentListLoadedCallback.onCommentListLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                onCommentListLoadedCallback.onCommentListLoaded(null);
            }
        });
    }

    public interface OnCommentAddedCallback {
        public void onCommentAdded(Comment comment);
    }

    public static void addNewComment(final Context mContext, final int feedbackId, String content,
                                     final OnCommentAddedCallback callback) {
        NetworkUtils.isOnline(mContext);

        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        ICommentService commentService = retrofit.create(ICommentService.class);
        Call<CommentWrapper> call = commentService.addComment(feedbackId, content);
        call.enqueue(new Callback<CommentWrapper>() {
            @Override
            public void onResponse(Call<CommentWrapper> call, Response<CommentWrapper> response) {
                if(response.isSuccessful()) {
                    callback.onCommentAdded(response.body().getComment());
                } else {
                    // TODO Display error message here
                }
            }

            @Override
            public void onFailure(Call<CommentWrapper> call, Throwable t) {
                callback.onCommentAdded(null);
            }
        });
    }
}
