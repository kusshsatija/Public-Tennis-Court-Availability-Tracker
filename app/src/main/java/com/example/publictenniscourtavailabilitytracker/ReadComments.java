package com.example.publictenniscourtavailabilitytracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class ReadComments extends AppCompatActivity {
    private String courtName;


    @Override
    protected void onResume(){
        super.onResume();
        updateComments();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_comments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        courtName = intent.getStringExtra("courtName");

        TextView textView = findViewById(R.id.courtNameText);
        textView.setText(courtName);

        updateComments();

        Button ratingButton = findViewById(R.id.addRatingButton);
        ratingButton.setOnClickListener(this::addRatingDialog);


    }

    public void addCommentPage(View v){
        Intent intent = new Intent(this, CreateComment.class);
        intent.putExtra("courtName", courtName);
        startActivity(intent);
    }

    public void updateComments(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        CommentDao commentDao = db.commentDao();
        RatingDao ratingDao = db.ratingDao();

        List<Comment> commentList = commentDao.listByCourt(courtName);
        TextView textView = findViewById(R.id.noCommentsText);

        if(commentList==null||commentList.isEmpty()){
            textView.setText(R.string.no_comment);
            RecyclerView recyclerView = findViewById(R.id.commentsView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new CommentAdapter(commentList));

        }else {
            textView.setText("");
            RecyclerView recyclerView = findViewById(R.id.commentsView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new CommentAdapter(commentList));
        }

        Rating rating = ratingDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        if(rating!=null){
            Button ratingButton = findViewById(R.id.addRatingButton);
            ratingButton.setText(R.string.edit_rating);
        }else{
            Button ratingButton = findViewById(R.id.addRatingButton);
            ratingButton.setText(R.string.add_rating);
        }
        Comment comment = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
        if(comment != null){
            Button commentButton = findViewById(R.id.addCommentButton);
            commentButton.setText(R.string.edit_comment);
        }else{
            Button commentButton = findViewById(R.id.addCommentButton);
            commentButton.setText(R.string.add_comment);
        }

        Float avgRating = ratingDao.getAvgByCourt(courtName);
        RatingBar ratingBar = findViewById(R.id.ratingBar2);
        if (avgRating==null){
            ratingBar.setRating(0);
        }else {
            ratingBar.setRating(avgRating);
        }

    }

    public void addRatingDialog(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_rating);

        Button button = dialog.findViewById(R.id.addRatingButton);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        RatingDao ratingDao = db.ratingDao();

        Rating rating = ratingDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        Button deleteButton = dialog.findViewById(R.id.deleteRatingButton);

        if(rating!=null){
            ratingBar.setRating(rating.rating);
            button.setText(R.string.edit_rating);

            deleteButton.setOnClickListener(view -> {

                Dialog dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.dialog_delete_rating);
                dialog2.show();
                Button confirm = dialog2.findViewById(R.id.deleteRatingButton);
                confirm.setOnClickListener(view2 -> {
                    float ratingFloat = ratingBar.getRating();
                    Rating rating2 = new Rating(MainActivity.userId, ratingFloat, courtName);

                    ratingDao.delete(rating2);

                    CommentDao commentDao = db.commentDao();
                    Comment comment = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
                    if(comment!=null) {
                        commentDao.delete(comment);
                    }
                    dialog2.dismiss();
                    updateComments();
                    dialog.dismiss();

                });
                Button cancelButton = dialog2.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(view2 ->{
                    dialog2.dismiss();
                });
                updateComments();
            });

        }else{
            deleteButton.setVisibility(View.GONE);
        }

        button.setOnClickListener(view -> {
            float ratingFloat = ratingBar.getRating();
            Rating rating2 = new Rating(MainActivity.userId, ratingFloat, courtName);



            if (rating != null) {
                ratingDao.update(rating2);
            } else {
                ratingDao.insert(rating2);
            }

            CommentDao commentDao = db.commentDao();
            Comment comment = commentDao.findByUserIdAndCourt(MainActivity.userId, courtName);
            if (comment != null) {
                comment.rating = rating2.rating;
                commentDao.update(comment);
            }

            updateComments();

            dialog.dismiss();
        });

        dialog.show();
    }


    public void back(View v){
        finish();
    }
}