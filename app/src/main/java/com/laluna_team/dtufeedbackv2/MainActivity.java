package com.laluna_team.dtufeedbackv2;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.laluna_team.dtufeedbackv2.adapter.FeedbackAdapter;
import com.laluna_team.dtufeedbackv2.databinding.ActivityMainBinding;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.service.feedback.FeedbackService;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements FeedbackAdapter.FeedbackAdapterOnClickHandler {

    private ActivityMainBinding mainBinding;
    private static int limit = 15;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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

    }
}
