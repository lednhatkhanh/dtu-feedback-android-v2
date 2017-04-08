package com.laluna_team.dtufeedbackv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.laluna_team.dtufeedbackv2.adapter.FeedbackAdapter;
import com.laluna_team.dtufeedbackv2.databinding.ActivityMainBinding;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.service.feedback.FeedbackService;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements FeedbackAdapter.FeedbackAdapterOnClickHandler, View.OnClickListener {

    private ActivityMainBinding mainBinding;
    private static int limit = 15;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (PreferenceUtils.getAuthenticationToken(this) == null) {
            mainBinding.addFeedbackButton.setVisibility(View.GONE);
        }
        mainBinding.addFeedbackButton.setOnClickListener(this);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainBinding.feedbackListRecyclerView.setLayoutManager(layoutManager);
        mainBinding.feedbackListRecyclerView.setHasFixedSize(false);

        final FeedbackAdapter feedbackAdapter = new FeedbackAdapter(this, this);
        mainBinding.feedbackListRecyclerView.setAdapter(feedbackAdapter);

        getAllFeedback(feedbackAdapter);

        mainBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllFeedback(feedbackAdapter);
            }
        });
        mainBinding.feedbackListRecyclerView
                .addOnScrollListener(createInfiniteScrollListener(feedbackAdapter));
    }

    private InfiniteScrollListener createInfiniteScrollListener(final FeedbackAdapter feedbackAdapter) {
        return new InfiniteScrollListener(limit, layoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {

                limit += 10;
                mainBinding.bottomLoadingIndicator.setVisibility(View.VISIBLE);

                FeedbackService.getAllFeedback(
                        MainActivity.this,
                        limit,
                        "all", null, null, null, new FeedbackService.FeedbackListLoadedCallback() {
                    @Override
                    public void onFeedbackListLoaded(List<Feedback> feedbackList) {
                        feedbackAdapter.updateFeedbackList(feedbackList);
                        mainBinding.bottomLoadingIndicator.setVisibility(View.GONE);
                    }
                });
            }
        };
    }

    private void getAllFeedback(final FeedbackAdapter feedbackAdapter) {
        if (feedbackAdapter.getItemCount() == 0) {
            mainBinding.loadingIndicator.setVisibility(View.VISIBLE);
        }
        FeedbackService.getAllFeedback(this, 15, "all", null, null, null, new FeedbackService.FeedbackListLoadedCallback() {
            @Override
            public void onFeedbackListLoaded(List<Feedback> feedbackList) {
                feedbackAdapter.updateFeedbackList(feedbackList);
                mainBinding.swipeRefresh.setRefreshing(false);
                mainBinding.loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(int Id) {
        Toast.makeText(this, "ID: " + Id, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("feedback_id", Id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_feedback_button:
                startActivity(new Intent(this, AddFeedbackActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                }).setNegativeButton("No", null)
                .show();
    }
}
