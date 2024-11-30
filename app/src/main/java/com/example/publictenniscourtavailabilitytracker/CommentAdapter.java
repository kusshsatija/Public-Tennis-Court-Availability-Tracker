package com.example.publictenniscourtavailabilitytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> commentList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorText;
        private final TextView commentText;
        private final TextView dateText;
        private final RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            authorText = (TextView) view.findViewById(R.id.authorTextView);
            commentText = (TextView) view.findViewById(R.id.commentTextView);
            dateText = (TextView) view.findViewById(R.id.dateText);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBarView);

        }

        public TextView getAuthorText() {
            return authorText;
        }
        public TextView getCommentText() {
            return commentText;
        }
        public TextView getDateText() {
            return dateText;
        }
        public RatingBar getRatingBar() {
            return ratingBar;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param commentList List<Comment> containing the data to populate views to be used
     * by RecyclerView
     */

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAuthorText().setText(commentList.get(position).author);
        viewHolder.getCommentText().setText(commentList.get(position).commentText);
        viewHolder.getDateText().setText(commentList.get(position).date.toString());
        viewHolder.getRatingBar().setRating(commentList.get(position).rating);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

