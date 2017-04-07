package com.laluna_team.dtufeedbackv2.model.campus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;

import java.util.List;

/**
 * Created by lednh on 4/5/2017.
 */

public class Campus
{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
