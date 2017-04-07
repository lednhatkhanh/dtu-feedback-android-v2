package com.laluna_team.dtufeedbackv2.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laluna_team.dtufeedbackv2.model.campus.Campus;

import java.util.List;

/**
 * Created by lednh on 4/5/2017.
 */

public class CategoryList {
    @SerializedName("data")
    @Expose
    private List<Category> categoryList;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
