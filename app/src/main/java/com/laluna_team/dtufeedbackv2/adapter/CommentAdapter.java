package com.laluna_team.dtufeedbackv2.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laluna_team.dtufeedbackv2.R;
import com.laluna_team.dtufeedbackv2.databinding.CommentListItemBinding;
import com.laluna_team.dtufeedbackv2.model.comment.Comment;
import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lednh on 4/7/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentAdapterViewHolder> {

    List<Comment> commentList;
    final Context mContext;

    public CommentAdapter(Context context, int feedbackId) {
        mContext = context;
    }

    @Override
    public CommentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.comment_list_item, parent, false);
        return new CommentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapterViewHolder holder, int position) {
        final CommentListItemBinding listItemBinding = holder.getBinding();
        Comment currentComment = commentList.get(position);

        listItemBinding.usernameTextView.setText(currentComment.getUser().getName());
        listItemBinding.commentContentTextView.setText(currentComment.getContent());

        CharSequence timeStringToDisplay = DateUtils.getRelativeDateTimeString(
                mContext, currentComment.getUpdatedAt().getTime(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_SHOW_TIME);
        listItemBinding.commentUpdatedAtTextView.setText(timeStringToDisplay);
        Picasso.with(mContext)
                .load(NetworkUtils.createImageUrl(currentComment.getUser().getAvatar()))
                .into(listItemBinding.userAvatarImageView);
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    public void swapCommentList(List<Comment> newCommentList) {
        commentList = newCommentList;
        notifyDataSetChanged();
    }

    class CommentAdapterViewHolder extends RecyclerView.ViewHolder {

        CommentListItemBinding listItemBinding;

        public CommentAdapterViewHolder(View itemView) {
            super(itemView);
            listItemBinding = DataBindingUtil.bind(itemView);
        }

        CommentListItemBinding getBinding() {
            return listItemBinding;
        }
    }
}
