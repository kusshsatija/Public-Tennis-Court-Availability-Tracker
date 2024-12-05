package com.example.publictenniscourtavailabilitytracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Messages extends AppCompatActivity {

    private List<ConversationSummary> conversationList;
    private AppDatabase database;
    private ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        FloatingActionButton composeButton = findViewById(R.id.composeMessageBtn);
        composeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Messages.this, ComposeActivity.class);
            startActivity(intent);
        });

        // Initialize Room database
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        // Populate the database with sample data if empty
        populateDatabaseIfEmpty();

        // Setup RecyclerView
        RecyclerView convoRecycler = findViewById(R.id.conversationsRecyclerView);
        convoRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Load conversations from the database
        loadConversations(convoRecycler);

        // Register the BroadcastReceiver to listen for new messages
        registerReceiver(newMessageReceiver, new IntentFilter("com.example.publictenniscourtavailabilitytracker.NEW_MESSAGE_ADDED"), Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver when the activity is destroyed
        unregisterReceiver(newMessageReceiver);
    }

    private void populateDatabaseIfEmpty() {
        new Thread(() -> {
            // Check if the database is empty
            if (database.messageDao().getAllMessages().isEmpty()) {
                // Insert sample messages into the database
                Message[] messages = createSampleMessages();
                database.messageDao().insertAll(messages);
            }
        }).start();
    }

    private void loadConversations(RecyclerView convoRecycler) {
        new Thread(() -> {
            // Fetch conversation summaries from the database
            conversationList = database.messageDao().getConversationSummaries();

            // Update the UI on the main thread
            runOnUiThread(() -> {
                // Create the adapter with a listener and context
                conversationAdapter = new ConversationAdapter(conversationList, new ConversationAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Get the selected conversation
                        String conversationId = conversationList.get(position).getConversation();

                        // Mark messages as read in the database
                        database.messageDao().markMessagesAsRead(conversationId);

                        // Open the Chat activity and pass the conversation name
                        Intent intent = new Intent(Messages.this, Chat.class);
                        intent.putExtra("conversationName", conversationId);
                        startActivity(intent);

                        // After going to chat, we will update the conversation list (mark as read)
                        conversationList.get(position).setUnread(false);
                        conversationAdapter.notifyItemChanged(position); // Refresh this item
                    }
                }, database, Messages.this); // Passing context

                // Set the adapter to the RecyclerView
                convoRecycler.setAdapter(conversationAdapter);
            });
        }).start();
    }

    // BroadcastReceiver to listen for new messages
    private final BroadcastReceiver newMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Reload conversations when a new message is added
            loadConversations((RecyclerView) findViewById(R.id.conversationsRecyclerView));
        }
    };

    // Override to handle result from Chat activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String updatedConversation = data.getStringExtra("updatedConversation");
            if (updatedConversation != null) {
                // Reload the conversation list after a message is sent
                loadConversations((RecyclerView) findViewById(R.id.conversationsRecyclerView));
            }
        }
    }

    private Message[] createSampleMessages() {
        // Define sample data
        return new Message[]{
                new Message("Alice", "Play tennis much?", "Alice", R.drawable.alice, false),
                new Message("Alice", "better than u", "You", R.drawable.alice, false),
                new Message("Bob", "Suck at tennis much", "Bob", R.drawable.bob, false),
                new Message("Bob", "Just give up and play pickleball NERD", "You", R.drawable.bob, false),
                new Message("Charlie", "I bet u my best racket", "Charlie", R.drawable.charlie, false),
                new Message("Dominic", "U suck lol!", "Dominic", R.drawable.dominic, true),
        };
    }

    public void finish(View view) {
        // This will clear all the activities above this one in the stack and bring the MainActivity to the front
        Intent intent = new Intent(Messages.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Clear the activity stack
        startActivity(intent);
        finish();  // Finish the current activity
    }

}
