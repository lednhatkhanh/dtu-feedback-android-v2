package com.laluna_team.dtufeedbackv2.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lednh on 4/7/2017.
 */

public class FeedbackWrapper {
    @SerializedName("data")
    @Expose
    private Feedback feedback;

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
