package com.example.publictenniscourtavailabilitytracker;

import android.app.Dialog;
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
        RatingDao ratingDao = db.ratingDao();
        Button button = findViewById(R.id.submitReviewButton);
        Button deleteButton = findViewById(R.id.deleteReviewButton);
        Comment comment = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
        Rating rating = ratingDao.findByUserIdAndCourt(MainActivity.userId, courtName);
        if(comment!=null){

            button.setText(R.string.edit_comment);

            EditText name = findViewById(R.id.enterAuthor);
            EditText commentText = findViewById(R.id.enterComment);
            RatingBar ratingBar = findViewById(R.id.ratingBar);

            ratingBar.setRating(comment.rating);
            name.setText(comment.author);
            commentText.setText(comment.commentText);


            ratingBar.setRating(rating.rating);
            button.setText(R.string.edit_rating);

            deleteButton.setOnClickListener(view -> {

                Dialog dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.dialog_delete_comment);
                dialog2.show();
                Button deleteCommentButton = dialog2.findViewById(R.id.deleteCommentButton);
                deleteCommentButton.setOnClickListener(view2 -> {
                    Comment comment2 = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
                    commentDao.delete(comment2);

                    finish();
                });
                Button deleteRatingButton = dialog2.findViewById(R.id.deleteRatingButton);
                deleteRatingButton.setOnClickListener(view2 -> {
                    float ratingFloat = ratingBar.getRating();
                    Rating rating2 = new Rating(MainActivity.userId, ratingFloat, courtName);

                    ratingDao.delete(rating2);
                    dialog2.dismiss();

                    Comment comment2 = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
                    commentDao.delete(comment2);
                    finish();
                });
                Button cancelButton = dialog2.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(view2 -> dialog2.dismiss());
            });

        } else {
            if (rating!=null) {
                RatingBar ratingBar = findViewById(R.id.ratingBar);

                ratingBar.setRating(rating.rating);
            }
            deleteButton.setVisibility(View.GONE);
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
        RatingDao ratingDao = db.ratingDao();

        Comment comment2 = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        if(comment2!=null){
            commentDao.update(comment);
        }else {
            commentDao.insertAll(comment);
        }

        Rating rating = ratingDao.findByUserIdAndCourt(MainActivity.userId, courtName);
        if(rating!=null){
            ratingDao.update(new Rating(MainActivity.userId, comment.rating, courtName));

        } else{
            ratingDao.insert(new Rating(MainActivity.userId, comment.rating, courtName));
        }


        finish();
    }

    public void back(View v){
        finish();
    }

}