package com.laluna_team.dtufeedbackv2.service.campus;

import com.laluna_team.dtufeedbackv2.model.campus.CampusList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lednh on 4/5/2017.
 */

public interface ICampusService {
    @GET("campuses")
    Call<CampusList> getAllCampuses();
}
