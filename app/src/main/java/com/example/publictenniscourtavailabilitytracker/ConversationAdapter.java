package com.example.publictenniscourtavailabilitytracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<ConversationSummary> conversationList;
    private final OnItemClickListener listener;
    private final AppDatabase database;
    private final Context context;  // Added Context

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public ConversationAdapter(List<ConversationSummary> conversationList, OnItemClickListener listener, AppDatabase database, Context context) {
        this.conversationList = conversationList;
        this.listener = listener;
        this.database = database;
        this.context = context; // Save the context
        sortConversationsByMostRecentMessage();
    }

    // Method to sort conversations based on the most recent message position
    private void sortConversationsByMostRecentMessage() {
        if (conversationList != null) {
            Collections.sort(conversationList, (c1, c2) -> {
                int c1LastMessagePosition = database.messageDao().getLastMessagePosition(c1.getConversation());
                int c2LastMessagePosition = database.messageDao().getLastMessagePosition(c2.getConversation());
                return Integer.compare(c2LastMessagePosition, c1LastMessagePosition);
            });
        }
    }

    // ViewHolder defines views in a single list item
    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView, unreadBadge;
        TextView recipientNameTextView, recentMessageTextView;

        public ConversationViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            unreadBadge = itemView.findViewById(R.id.unreadBadge);
            recipientNameTextView = itemView.findViewById(R.id.recipientNameTextView);
            recentMessageTextView = itemView.findViewById(R.id.recentMessageTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_item, parent, false);
        return new ConversationViewHolder(view, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ConversationSummary conversation = conversationList.get(position);

        // Set recipient name (conversation title or identifier)
        holder.recipientNameTextView.setText(conversation.getConversation());

        // Set the most recent message
        if (conversation.getMostRecentMessage() != null && !conversation.getMostRecentMessage().isEmpty()) {
            holder.recentMessageTextView.setText(conversation.getMostRecentMessage());
        } else {
            holder.recentMessageTextView.setText("No messages yet");
        }

        // Set profile image
        holder.profileImageView.setImageResource(conversation.getRecipientPic());

        // Show/hide unread badge
        if (conversation.isUnread()) {
            holder.unreadBadge.setVisibility(View.VISIBLE);
        } else {
            holder.unreadBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return conversationList == null ? 0 : conversationList.size();
    }

    // Method to update the list of conversations
    public void updateConversations(List<ConversationSummary> conversations) {
        this.conversationList = conversations;
        sortConversationsByMostRecentMessage(); // Ensure the list is sorted
        notifyDataSetChanged();
    }

    public void markMessagesAsRead(String conversation) {
        new Thread(() -> {
            // Update all messages in the conversation to be read
            database.messageDao().markMessagesAsRead(conversation);

            // Update the conversation status in the list and notify the item
            for (ConversationSummary summary : conversationList) {
                if (summary.getConversation().equals(conversation)) {
                    summary.setUnread(false); // Mark as read
                    break;
                }
            }

            // Notify the adapter to refresh the item
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
            }
        }).start();
    }

    public void addNewMessageAndSort(String conversation) {
        new Thread(() -> {
            // Add a new message to the conversation
            // This should include database insertion logic for the new message.

            // After inserting the new message, re-sort the conversation list
            Collections.sort(conversationList, (c1, c2) -> {
                int c1LastMessagePosition = database.messageDao().getLastMessagePosition(c1.getConversation());
                int c2LastMessagePosition = database.messageDao().getLastMessagePosition(c2.getConversation());
                return Integer.compare(c2LastMessagePosition, c1LastMessagePosition);
            });

            // Notify the adapter that the list has changed
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
            }
        }).start();
    }

}
