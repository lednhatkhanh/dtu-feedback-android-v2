package com.laluna_team.dtufeedbackv2.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lednh on 4/7/2017.
 */

public class CommentList {
    @SerializedName("data")
    @Expose
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
