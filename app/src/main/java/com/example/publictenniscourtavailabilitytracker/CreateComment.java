package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

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

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        CommentDao commentDao = db.commentDao();
        Button button = findViewById(R.id.submitReviewButton);
        Comment comment = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        if(comment!=null){

            button.setText("Edit Comment");
        }






    }

    public void createComment(View v){
        EditText author = findViewById(R.id.enterAuthor);
        EditText commentInput = findViewById(R.id.enterComment);
        RatingBar ratingbar = findViewById(R.id.ratingBar);

        Comment comment = new Comment(MainActivity.userId, author.getText().toString(), ratingbar.getRating(), commentInput.getText().toString(), courtName);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        CommentDao commentDao = db.commentDao();

        Comment comment2 = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        if(comment2!=null){
            commentDao.update(comment);
        }else {
            commentDao.insertAll(comment);
        }


        finish();
    }

    public void back(View v){
        finish();
    }

}