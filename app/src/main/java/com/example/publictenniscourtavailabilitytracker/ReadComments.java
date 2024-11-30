package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
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
    }

    public void addCommentPage(View v){
        Intent intent = new Intent(this, CreateComment.class);
        intent.putExtra("courtName", courtName);
        startActivity(intent);
    }

    public void updateComments(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "comments").allowMainThreadQueries().build();
        CommentDao commentDao = db.userDao();

        List<Comment> commentList = commentDao.listByCourt(courtName);
        if(commentList==null||commentList.isEmpty()){
            TextView textView = findViewById(R.id.noCommentsText);
            textView.setText(R.string.no_comment);
        }else {
            RecyclerView recyclerView = findViewById(R.id.commentsView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new CommentAdapter(commentList));
        }

    }


    /*
    public void updateComments(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "comments").allowMainThreadQueries().build();
        CommentDao userDao = db.userDao();

        LinearLayout linearLayout = findViewById(R.id.commentsHolder);
        linearLayout.removeAllViewsInLayout();

        try{
            List<Comment> comments = userDao.listByCourt(courtName);
            if(comments==null || comments.isEmpty()){
                TextView text = new TextView(this);
                text.setText(R.string.no_comment);


                linearLayout.addView(text);
            }else {
                linearLayout.removeAllViewsInLayout();
                for (Comment comment : comments) {
                    TextView author = new TextView(this);
                    author.setText(comment.author);
                    Space space = new Space(this);
                    space.setMinimumHeight(10);
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);


                    RatingBar ratingBar = new RatingBar(this);
                    ratingBar.setRating(comment.rating);
                    ratingBar.setIsIndicator(true);
                    ratingBar.setScaleX((float) 0.3);
                    ratingBar.setScaleY((float) 0.3);
                    ratingBar.setNumStars(5);
                    ratingBar.setProgressDrawable(@drawable/ratingbar_small_material);
                    ratingBar.setIndeterminateDrawable(R.drawable.ratingbar_small_materials);
                    layout.addView(ratingBar);


                    TextView dateText = new TextView(this);
                    dateText.setText(comment.date.toString());
                    layout.addView(dateText);

                    TextView commentText = new TextView(this);
                    commentText.setText(comment.commentText);
                    Space space2 = new Space(this);
                    space2.setMinimumHeight(30);


                    linearLayout.addView(author);
                    linearLayout.addView(space);
                    linearLayout.addView(layout);
                    linearLayout.addView(commentText);
                    linearLayout.addView(space2);
                }
            }

        } catch (NoSuchElementException e){
            TextView text = new TextView(this);
            text.setText(R.string.no_comment);

            linearLayout.addView(text);
        }



    }
    */

    public void back(View v){
        finish();
    }
}