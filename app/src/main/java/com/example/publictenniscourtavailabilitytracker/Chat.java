package com.example.publictenniscourtavailabilitytracker;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Chat extends AppCompatActivity {

    private Conversation conversation;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private EditText messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        // Retrieve the conversation passed from the Messages activity
        conversation = (Conversation) getIntent().getSerializableExtra("conversation");

        if (conversation == null) {
            // Handle the case where the conversation is null
            finish(); // Or display an error message
            return;
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Message> messages = conversation.getMessages();
        chatAdapter = new ChatAdapter(messages); // Pass the conversation's messages to the adapter
        recyclerView.setAdapter(chatAdapter);

        // Set up the message input field
        messageInput = findViewById(R.id.messageEditText);

        // Set up the window insets for padding (status bars, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Handle sending a new message
    public void sendMessage(View view) {
        String messageContent = messageInput.getText().toString();

        if (!messageContent.isEmpty()) {
            // Create a new message and add it to the conversation
            Message newMessage = new Message(messageContent, "You"); // Assuming the sender is "You"
            conversation.addMessage(newMessage);

            // Notify the adapter to update the RecyclerView
            chatAdapter.notifyItemInserted(conversation.getMessages().size() - 1); // Scroll to the new message
            recyclerView.scrollToPosition(conversation.getMessages().size() - 1);

            // Clear the input field
            messageInput.setText("");
        }
    }

    // Handle finishing the activity (going back)
    public void finish(View view) {
        finish();
    }
}
