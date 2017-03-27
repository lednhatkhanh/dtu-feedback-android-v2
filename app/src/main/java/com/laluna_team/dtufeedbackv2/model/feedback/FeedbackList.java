package com.laluna_team.dtufeedbackv2.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lednh on 3/27/2017.
 */

public class FeedbackList {
    @SerializedName("data")
    @Expose
    private List<Feedback> feedbackList;

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }
}
