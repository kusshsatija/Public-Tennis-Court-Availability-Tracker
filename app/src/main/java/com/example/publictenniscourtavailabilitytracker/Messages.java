package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    private List<Conversation> conversationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages);

        RecyclerView convoRecycler = findViewById(R.id.conversationsRecyclerView);
        convoRecycler.setLayoutManager(new LinearLayoutManager(this));

        conversationList = createSampleConversations();

        ConversationAdapter conversationAdapter = new ConversationAdapter(conversationList, position -> {
            // Handle click event for the selected conversation
            Conversation selectedConversation = conversationList.get(position);

            // Open the Chat activity and pass the selected conversation
            Intent intent = new Intent(Messages.this, Chat.class);
            intent.putExtra("conversation", selectedConversation); // Pass the whole conversation
            startActivity(intent);
        });

        convoRecycler.setAdapter(conversationAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private List<Conversation> createSampleConversations() {
        List<Conversation> conversationList = new ArrayList<>();

        // Example Conversation 1
        List<Message> aliceMessages = new ArrayList<>();
        aliceMessages.add(new Message("Play tennis much?", "Alice"));
        aliceMessages.add(new Message("Ya against ur mom last night", "You"));
        conversationList.add(new Conversation("Alice", R.drawable.alice, aliceMessages));

        // Example Conversation 2
        List<Message> bobMessages = new ArrayList<>();
        bobMessages.add(new Message("Suck at tennis much", "Bob"));
        bobMessages.add(new Message("Just give up and play pickleball NERD", "You"));
        conversationList.add(new Conversation("Bob", R.drawable.bob, bobMessages));

        // Example Conversation 3
        List<Message> charlieMessages = new ArrayList<>();
        charlieMessages.add(new Message("I bet u my best racket", "Charlie"));
        conversationList.add(new Conversation("Charlie", R.drawable.charlie, charlieMessages));

        // Example Conversation 4
        List<Message> dominicMessages = new ArrayList<>();
        dominicMessages.add(new Message("U suck lol!", "Dominic"));
        dominicMessages.add(new Message("This aint badminton this is real shit", "You"));
        conversationList.add(new Conversation("Dominic", R.drawable.dominic, dominicMessages));

        return conversationList;
    }
}
