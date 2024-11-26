package com.example.publictenniscourtavailabilitytracker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final List<Conversation> conversationList;
    private final OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ConversationAdapter(List<Conversation> conversationList, OnItemClickListener listener) {
        this.conversationList = conversationList;
        this.listener = listener;
    }

    // ViewHolder defines views in a single list item
    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView recipientNameTextView, recentMessageTextView;

        public ConversationViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
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
        Conversation conversation = conversationList.get(position);
        Message mostRecentMessage = conversation.getMostRecentMessage();

        // Set the recipient's name (always show the name of the conversation partner)
        holder.recipientNameTextView.setText(conversation.getRecipient());

        // Display the most recent message content
        if (mostRecentMessage != null) {
            holder.recentMessageTextView.setText(mostRecentMessage.getContent());
        } else {
            holder.recentMessageTextView.setText("No messages yet");
        }

        // Example profile image setting (adjust as needed)
        holder.profileImageView.setImageResource(conversation.getRecipientPic()); // Placeholder
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
