package com.laluna_team.dtufeedbackv2.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laluna_team.dtufeedbackv2.R;
import com.laluna_team.dtufeedbackv2.databinding.FeedbackListItemBinding;
import com.laluna_team.dtufeedbackv2.model.feedback.Feedback;
import com.laluna_team.dtufeedbackv2.utils.DataUtils;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lednh on 3/27/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackAdapterViewHolder> {

    private static final String LOG_TAG = FeedbackAdapter.class.getSimpleName();
    private Context mContext;
    FeedbackAdapterOnClickHandler mClickHandler;

    List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, FeedbackAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public FeedbackAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.feedback_list_item, parent, false);
        view.setFocusable(false);
        return new FeedbackAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackAdapterViewHolder holder, int position) {
        try {
            final FeedbackListItemBinding itemBinding = holder.getBinding();

            final Feedback currFeedback = feedbackList.get(position);

            Log.d("LENGTH", currFeedback.getTitle().length() + "");
            if(currFeedback.getTitle().length() > 48) {
                String result = currFeedback.getTitle().substring(0, 45);
                result = result.substring(0, result.lastIndexOf(" "));
                result = result + "...";
                itemBinding.feedbackTitleTextView.setText(result);
            } else {
                itemBinding.feedbackTitleTextView.setText(currFeedback.getTitle());
            }

            itemBinding.feedbackUpdatedAtTextView
                    .setText(DataUtils.formatTimeToDisplay(mContext, currFeedback.getUpdatedAt()));

            itemBinding.feedbackStatusAndCategoryTextView
                    .setText(currFeedback.getCategory().getName());
            if(currFeedback.getSolved()) {
                itemBinding.feedbackStatusAndCategoryTextView
                        .setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
            } else {
                itemBinding.feedbackStatusAndCategoryTextView
                        .setBackgroundColor(mContext.getResources().getColor(R.color.colorYellow));
            }

            if(currFeedback.getImage() != null) {
                Picasso.with(mContext)
                        .load(NetworkUtils.createImageUrl(currFeedback.getImage()))
                        .fetch(new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.with(mContext)
                                        .load(NetworkUtils.createImageUrl(currFeedback.getImage()))
                                        .fit()
                                        .into(itemBinding.feedbackImageView);
                            }

                            @Override
                            public void onError() {
                                itemBinding.feedbackImageView
                                        .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                            }
                        });
            } else {
                itemBinding.feedbackImageView
                        .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return feedbackList == null ? 0 : feedbackList.size();
    }

    public interface FeedbackAdapterOnClickHandler {
        void onClick(int Id);
    }

    class FeedbackAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FeedbackListItemBinding feedbackListItemBinding;

        public FeedbackAdapterViewHolder(View view) {
            super(view);
            feedbackListItemBinding = DataBindingUtil.bind(view);

            // ----- Same onClick handler -----
            feedbackListItemBinding.feedbackTitleTextView.setOnClickListener(this);
            feedbackListItemBinding.feedbackImageView.setOnClickListener(this);
            // ----- Same onClick handler -----
        }

        @Override
        public void onClick(View v) {

        }

        public FeedbackListItemBinding getBinding() {
            return feedbackListItemBinding;
        }
    }
}
