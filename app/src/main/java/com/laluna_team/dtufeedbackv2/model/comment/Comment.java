package com.laluna_team.dtufeedbackv2.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laluna_team.dtufeedbackv2.model.user.User;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;

import java.util.Date;

/**
 * Created by lednh on 4/7/2017.
 */

public class Comment {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("feedback")
    @Expose
    private Feedback feedback;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    private Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
