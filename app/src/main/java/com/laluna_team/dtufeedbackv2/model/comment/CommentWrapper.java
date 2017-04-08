package com.laluna_team.dtufeedbackv2.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lednh on 4/7/2017.
 */

public class CommentWrapper {
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @SerializedName("data")
    @Expose
    private Comment comment;
}
