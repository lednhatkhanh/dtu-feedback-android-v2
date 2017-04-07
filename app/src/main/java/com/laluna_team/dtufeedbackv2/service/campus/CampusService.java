package com.laluna_team.dtufeedbackv2.service.campus;

import android.content.Context;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.model.campus.Campus;
import com.laluna_team.dtufeedbackv2.model.campus.CampusList;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lednh on 4/5/2017.
 */

public class CampusService {

    public interface CampusListLoadedCallback {
        public void onCampusListLoaded(List<Campus> campusList);
    }

    public static void getAllCampuses(final Context mContext,
                                      final CampusListLoadedCallback campusListLoadedCallback) {
        if(!NetworkUtils.isOnline(mContext)) {
            campusListLoadedCallback.onCampusListLoaded(null);
            return;
        }

        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        ICampusService campusService = retrofit.create(ICampusService.class);
        Call<CampusList> call = campusService.getAllCampuses();
        call.enqueue(new Callback<CampusList>() {
            @Override
            public void onResponse(Call<CampusList> call, Response<CampusList> response) {
                if(!response.isSuccessful()) {
                    campusListLoadedCallback.onCampusListLoaded(null);
                } else {
                    List<Campus> campusList = response.body().getCampusList();
                    campusListLoadedCallback.onCampusListLoaded(campusList);
                }
            }

            @Override
            public void onFailure(Call<CampusList> call, Throwable t) {
                campusListLoadedCallback.onCampusListLoaded(null);
            }
        });
    }
}
