package com.laluna_team.dtufeedbackv2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.laluna_team.dtufeedbackv2.databinding.ActivityAddFeedbackBinding;
import com.laluna_team.dtufeedbackv2.model.campus.Campus;
import com.laluna_team.dtufeedbackv2.model.category.Category;
import com.laluna_team.dtufeedbackv2.service.campus.CampusService;
import com.laluna_team.dtufeedbackv2.service.category.CategoryService;
import com.laluna_team.dtufeedbackv2.service.feedback.FeedbackService;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddFeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityAddFeedbackBinding mBinding;
    List<Campus> mCampusList;
    List<Category> mCategoryList;
    String mCurrentPhotoPath;
    private static final int REQUEST_PHOTO_PICKER = 123;

    int campusId;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String token = PreferenceUtils.getAuthenticationToken(this);
        if(token == null || token.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        NetworkUtils.isOnline(this);

        CampusService.getAllCampuses(this, new CampusService.CampusListLoadedCallback() {
            @Override
            public void onCampusListLoaded(List<Campus> campusList) {
                if(campusList != null) {
                    mCampusList = campusList;

                    ArrayList<String> campustNameList = new ArrayList<String>();
                    for(Campus campus: mCampusList) {
                        campustNameList.add(campus.getName());
                    }
                    ArrayAdapter<String> campusSpinnerAdapter
                            = new ArrayAdapter<String>(AddFeedbackActivity.this,
                            android.R.layout.simple_spinner_item,
                            campustNameList);
                    mBinding.feedbackCampusSpinner.setAdapter(campusSpinnerAdapter);
                } else {
                    // TODO Display error message here
                }
            }
        });
        CategoryService.getAllCategories(this, new CategoryService.CategoriesListLoadedCallback() {
            @Override
            public void onCategoriesListLoaded(List<Category> categoryList) {
                if(categoryList != null) {
                    mCategoryList = categoryList;

                    ArrayList<String> categoryNameList = new ArrayList<String>();
                    for(Category category : mCategoryList) {
                        categoryNameList.add(category.getName());
                    }

                    ArrayAdapter<String> categorySpinnerAdapter =
                            new ArrayAdapter<String>(
                                    AddFeedbackActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    categoryNameList
                            );

                    mBinding.feedbackCategorySpinner.setAdapter(categorySpinnerAdapter);
                } else {
                    // TODO Display error message here
                }
            }
        });

        mBinding.feedbackImageView.setOnClickListener(this);
        mBinding.sendFeedbackButton.setOnClickListener(this);
        mBinding.feedbackCampusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = mBinding.feedbackCampusSpinner.getSelectedItem().toString();
                for(int i=0;i<mCampusList.size();i++) {
                    if(mCampusList.get(i).getName().equals(selectedItem)) {
                        campusId = mCampusList.get(i).getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.feedbackCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = mBinding.feedbackCategorySpinner.getSelectedItem().toString();
                for(int i=0;i<mCategoryList.size();i++) {
                    if(mCategoryList.get(i).getName().equals(selectedItem)) {
                        categoryId = mCategoryList.get(i).getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.feedback_imageView:
                ImagePicker.create(this)
                        .returnAfterFirst(true)
                        .imageTitle(getString(R.string.select_photo_title))
                        .single()
                        .start(REQUEST_PHOTO_PICKER);
                break;
            case R.id.send_feedback_button:
                FeedbackService.addNewFeedback(
                        this,
                        mBinding.feedbackTitleEditText.getText().toString(),
                        mBinding.feedbackLocationEditText.getText().toString(),
                        campusId,
                        categoryId,
                        mCurrentPhotoPath,
                        mBinding.feedbackDescriptionTextArea.getText().toString());
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PHOTO_PICKER && resultCode == RESULT_OK && data != null) {
            if(ImagePicker.getImages(data).size() > 0) {
                Image image = ImagePicker.getImages(data).get(0);
                mCurrentPhotoPath = image.getPath();
                File file = new File(mCurrentPhotoPath);
                if(file.exists() && file.isFile() && file.canRead()) {
                    Picasso.with(this)
                            .load(file)
                            .fit()
                            .into(mBinding.feedbackImageView);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
