package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import io.github.muddz.styleabletoast.StyleableToast;


public class Chat extends AppCompatActivity {

    private String conversationName;  // To store the conversation name
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private MessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize DAO
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        messageDao = db.messageDao();

        // Retrieve the conversation name passed from the previous activity
        conversationName = getIntent().getStringExtra("conversationName");
        if (conversationName == null || conversationName.isEmpty()) {
            StyleableToast.makeText(this, "Conversation not found", Toast.LENGTH_SHORT, R.style.exampleToast).show();
            finish();  // Close the activity if no conversation name is passed
            return;
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the message input field
        messageInput = findViewById(R.id.messageEditText);

        // Load messages for the conversation
        loadMessages();
    }

    // Load messages for the conversation from the database
    private void loadMessages() {
        new Thread(() -> {
            List<Message> messages = messageDao.getMessagesByConversation(conversationName);
            if (messages == null || messages.isEmpty()) {
                runOnUiThread(() -> {
                    StyleableToast.makeText(Chat.this, "No messages found", Toast.LENGTH_SHORT, R.style.exampleToast).show();
                });
                return;
            }

            // Initialize the adapter with the messages (no need to reverse)
            runOnUiThread(() -> {
                chatAdapter = new ChatAdapter(messages);
                recyclerView.setAdapter(chatAdapter);
                // Scroll to the bottom to show the latest message initially
                recyclerView.scrollToPosition(messages.size() - 1);
            });
        }).start();
    }

    // Handle sending a new message
    public void sendMessage(View view) {
        String messageContent = messageInput.getText().toString();

        if (!messageContent.isEmpty()) {
            // Retrieve the recipientPic from the existing messages in this conversation
            new Thread(() -> {
                List<Message> messages = messageDao.getMessagesByConversation(conversationName);
                if (messages == null || messages.isEmpty()) {
                    runOnUiThread(() -> {
                        StyleableToast.makeText(Chat.this, "Cannot send message without conversation context", Toast.LENGTH_SHORT, R.style.exampleToast).show();
                    });
                    return;
                }

                int recipientPic = messages.get(0).getRecipientPic();  // Get the recipientPic from the first message

                // Create a new message and associate it with the conversation
                Message newMessage = new Message(conversationName, messageContent, "You", recipientPic, false);

                // Update the database
                messageDao.insertAll(newMessage);
                List<Message> updatedMessages = messageDao.getMessagesByConversation(conversationName);

                // Update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    chatAdapter.updateMessages(updatedMessages);
                    recyclerView.scrollToPosition(updatedMessages.size() - 1);
                });
            }).start();

            // Clear the input field
            messageInput.setText("");
        }
    }

    public void finish(View view) {
        Intent intent = new Intent(Chat.this, Messages.class);
        startActivity(intent);
        finish();
    }
}
