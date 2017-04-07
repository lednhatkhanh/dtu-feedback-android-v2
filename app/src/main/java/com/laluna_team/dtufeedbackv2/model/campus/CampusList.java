package com.laluna_team.dtufeedbackv2.model.campus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lednh on 4/5/2017.
 */

public class CampusList
{
    @SerializedName("data")
    @Expose
    private List<Campus> campusList;

    public List<Campus> getCampusList() {
        return campusList;
    }

    public void setCampusList(List<Campus> campusList) {
        this.campusList = campusList;
    }
}
