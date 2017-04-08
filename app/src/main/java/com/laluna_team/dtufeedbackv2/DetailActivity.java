package com.laluna_team.dtufeedbackv2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.laluna_team.dtufeedbackv2.adapter.CommentAdapter;
import com.laluna_team.dtufeedbackv2.databinding.ActivityDetailBinding;
import com.laluna_team.dtufeedbackv2.model.comment.Comment;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.model.user.User;
import com.laluna_team.dtufeedbackv2.service.comment.CommentService;
import com.laluna_team.dtufeedbackv2.service.feedback.FeedbackService;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.laluna_team.dtufeedbackv2.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;
    int feedbackId;
    Feedback mFeedback;
    LinearLayoutManager linearLayoutManager;
    CommentAdapter commentAdapter;
    List<Comment> mCommentList;
    private static int limit = 10;
    User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent == null || !intent.hasExtra("feedback_id")) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            feedbackId = intent.getIntExtra("feedback_id", 0);
        }

        NetworkUtils.isOnline(this);

        String token = PreferenceUtils.getAuthenticationToken(this);
        if (token == null || token.isEmpty()) {
            mBinding.commentContentEditText.setVisibility(View.GONE);
            mBinding.addCommentButton.setVisibility(View.GONE);
        }

        mCurrentUser = PreferenceUtils.getCurrentUser(this);

        linearLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mBinding.commentRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.commentRecyclerView.setHasFixedSize(false);

        commentAdapter = new CommentAdapter(DetailActivity.this, feedbackId);
        mBinding.commentRecyclerView.setAdapter(commentAdapter);

        doFetch();

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doFetch();
            }
        });
        mBinding.commentRecyclerView
                .addOnScrollListener(createInfiniteScrollListener(commentAdapter));

        mBinding.addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mBinding.commentContentEditText.getText().toString();
                CommentService.addNewComment(DetailActivity.this, feedbackId, content, new CommentService.OnCommentAddedCallback() {
                    @Override
                    public void onCommentAdded(Comment comment) {
                        if(comment != null) {
                            Collections.reverse(mCommentList);
                            mCommentList.add(comment);
                            Collections.reverse(mCommentList);
                            commentAdapter.swapCommentList(mCommentList);
                        }
                    }
                });
            }
        });

        mBinding.feedbackToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackService.toggleFeedback(DetailActivity.this, feedbackId, new FeedbackService.OnSingleFeedbackLoadedCallback() {
                    @Override
                    public void onLoaded(Feedback feedback) {
                        if(feedback != null) {
                            mFeedback = feedback;
                            displayUIBasedOnFeedbackSatus();
                        }
                    }
                });
            }
        });
    }

    private InfiniteScrollListener createInfiniteScrollListener(final CommentAdapter commentAdapter) {
        return new InfiniteScrollListener(limit, linearLayoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                limit+=10;

                mBinding.bottomLoadingIndicator.setVisibility(View.VISIBLE);

                CommentService.getAllComments(DetailActivity.this, limit, feedbackId, new CommentService.OnCommentListLoadedCallback() {
                    @Override
                    public void onCommentListLoaded(List<Comment> commentList) {
                        if(commentList != null) {
                            commentAdapter.swapCommentList(commentList);
                        }
                        mBinding.bottomLoadingIndicator.setVisibility(View.GONE);
                    }
                });
            }
        };
    }

    private void doFetch() {
        FeedbackService.getFeedbackById(this, feedbackId, new FeedbackService.OnSingleFeedbackLoadedCallback() {
            @Override
            public void onLoaded(Feedback feedback) {
                if(feedback == null) {
                    Log.e("Feedback is null", "Feedback is null");
                    mBinding.swipeRefresh.setRefreshing(false);
                } else {
                    mFeedback = feedback;

                    mBinding.feedbackTitleTextView.setText(mFeedback.getTitle());
                    mBinding.feedbackCategoryTextView.setText("Category: " + mFeedback.getCategory().getName());
                    mBinding.feedbackLocationTextView.setText(mFeedback.getLocation() + ", " + mFeedback.getCampus().getName());
                    Picasso.with(DetailActivity.this)
                            .load(NetworkUtils.createImageUrl(mFeedback.getImage()))
                            .fit()
                            .into(mBinding.feedbackImageView);
                    mBinding.feedbackDescriptionTextView.setText(mFeedback.getDescription());

                    displayUIBasedOnFeedbackSatus();
                }
            }
        });
        CommentService.getAllComments(DetailActivity.this, 10, feedbackId, new CommentService.OnCommentListLoadedCallback() {
            @Override
            public void onCommentListLoaded(List<Comment> commentList) {
                if(commentList != null) {
                    mCommentList = commentList;

                    commentAdapter.swapCommentList(mCommentList);

                    mBinding.swipeRefresh.setRefreshing(false);
                } else {
                    mBinding.swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void displayUIBasedOnFeedbackSatus() {

        if(mFeedback.getSolved()) {
            mBinding.feedbackStatusImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_24dp));
            mBinding.feedbackStatusImageView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        } else {
            mBinding.feedbackStatusImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_24dp));
            mBinding.feedbackStatusImageView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
        }

        if (mCurrentUser.getId() != mFeedback.getUser().getId()) {
            mBinding.feedbackToggleButton.setVisibility(View.GONE);
            mBinding.feedbackUpdateButton.setVisibility(View.GONE);
        } else if(!mFeedback.getSolved()) {
            mBinding.feedbackToggleButton.setTextColor(getResources().getColor(R.color.colorGray));
            mBinding.feedbackToggleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_gray_24dp, 0, 0, 0);
        } else {
            mBinding.feedbackToggleButton.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.feedbackToggleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_blue_24dp, 0, 0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
