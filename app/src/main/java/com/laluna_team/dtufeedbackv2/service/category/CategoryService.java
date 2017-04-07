package com.laluna_team.dtufeedbackv2.service.category;

import android.content.Context;

import com.laluna_team.dtufeedbackv2.AppRetrofit;
import com.laluna_team.dtufeedbackv2.model.category.Category;
import com.laluna_team.dtufeedbackv2.model.category.CategoryList;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lednh on 4/5/2017.
 */

public class CategoryService {

    public interface CategoriesListLoadedCallback {
        public void onCategoriesListLoaded(List<Category> categoryList);
    }

    public static void getAllCategories(final Context mContext,
                                        final CategoriesListLoadedCallback categoriesListLoadedCallback) {
        if(!NetworkUtils.isOnline(mContext)) {
            categoriesListLoadedCallback.onCategoriesListLoaded(null);
            return;
        }

        Retrofit retrofit = AppRetrofit.getRetrofitInstance(mContext);
        ICategoryService categoryService = retrofit.create(ICategoryService.class);
        Call<CategoryList> call = categoryService.getAllCategories();
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if(!response.isSuccessful()) {
                    categoriesListLoadedCallback.onCategoriesListLoaded(null);
                } else {
                    categoriesListLoadedCallback.onCategoriesListLoaded(response.body().getCategoryList());
                }
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                t.printStackTrace();
                categoriesListLoadedCallback.onCategoriesListLoaded(null);
            }
        });
    }
}
