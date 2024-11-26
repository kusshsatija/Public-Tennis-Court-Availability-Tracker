package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;

public class CreateComment extends AppCompatActivity {
    private String courtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        courtName = intent.getStringExtra("courtName");

        TextView textview = findViewById(R.id.courtNameText);
        textview.setText(courtName);




    }

    public void createComment(View v){
        EditText author = findViewById(R.id.enterAuthor);
        EditText commentInput = findViewById(R.id.enterComment);
        RatingBar ratingbar = findViewById(R.id.ratingBar);

        Comment comment = new Comment(author.getText().toString(), ratingbar.getNumStars(), commentInput.getText().toString());
        if(ReadComments.courtComments.containsKey(courtName)){
            ReadComments.courtComments.get(courtName).add(comment);
        }else{
            LinkedList<Comment> list = new LinkedList<>();
            list.add(comment);
            ReadComments.courtComments.put(courtName, list);
        }

        finish();
    }

}