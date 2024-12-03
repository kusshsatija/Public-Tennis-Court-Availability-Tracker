package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

        List<Comment> commentList = commentDao.listByCourt(courtName);
        TextView textView = findViewById(R.id.noCommentsText);

        if(commentList==null||commentList.isEmpty()){
            textView.setText(R.string.no_comment);
        }else {
            textView.setText("");
            RecyclerView recyclerView = findViewById(R.id.commentsView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new CommentAdapter(commentList));
        }

        RatingDao ratingDao = db.ratingDao();
        Rating rating = ratingDao.findByUserIdAndCourt(MainActivity.userId, courtName);

        if(rating!=null){
            Button ratingButton = findViewById(R.id.addRatingButton);
            ratingButton.setText("Edit Rating");
        }

    }


    public void back(View v){
        finish();
    }
}