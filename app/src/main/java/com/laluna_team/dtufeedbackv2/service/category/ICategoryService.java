package com.laluna_team.dtufeedbackv2.service.category;

import com.laluna_team.dtufeedbackv2.model.category.CategoryList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lednh on 4/5/2017.
 */

public interface ICategoryService {
    @GET("categories")
    Call<CategoryList> getAllCategories();
}
