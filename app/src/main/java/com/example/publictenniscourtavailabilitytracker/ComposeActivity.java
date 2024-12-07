package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.HashMap;

public class ComposeActivity extends AppCompatActivity {

    private AppDatabase database;
    private HashMap<String, Integer> recipientProfiles;
    private String selectedRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // Initialize the database
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_database").allowMainThreadQueries().build();

        // Set up recipient profiles with drawable resource IDs
        recipientProfiles = new HashMap<>();
        recipientProfiles.put("Bruce", R.drawable.bruce);
        recipientProfiles.put("Jackson", R.drawable.jackson);
        recipientProfiles.put("Sophia", R.drawable.sophia);

        // Set up UI elements
        Spinner recipientSpinner = findViewById(R.id.recipientSpinner);
        EditText messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);

        // Populate the spinner with recipient names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, recipientProfiles.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipientSpinner.setAdapter(adapter);

        // Handle spinner selection
        recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRecipient = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRecipient = null;
            }
        });

        // Handle the send button click
        sendButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString().trim();

            if (selectedRecipient == null || messageText.isEmpty()) {
                Toast.makeText(this, "Please select a recipient and enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the new conversation and message
            saveNewConversation(selectedRecipient, messageText);

            // Navigate back to Messages
            Intent intent = new Intent(ComposeActivity.this, Messages.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveNewConversation(String recipient, String messageText) {
        new Thread(() -> {
            // Get the profile image resource for the selected recipient
            int profileImageResId = recipientProfiles.getOrDefault(recipient, R.drawable.alice);

            // Create a new message
            Message newMessage = new Message(
                    recipient,
                    messageText,
                    "You",
                    profileImageResId,
                    false // Mark as unread
            );

            // Insert the message into the database
            database.messageDao().insert(newMessage);
        }).start();
    }

    public void finish(View v){
        finish();
    }
}
