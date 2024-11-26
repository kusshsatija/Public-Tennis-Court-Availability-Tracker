package com.example.publictenniscourtavailabilitytracker;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;

public class ReadComments extends AppCompatActivity {
    public static HashMap<String, LinkedList<Comment>> courtComments = new HashMap<String, LinkedList<Comment>>();
    private LinkedList<Comment> comments;
    private String courtName;

    @Override
    protected void onResume(){
        super.onResume();
        if(comments==null){
            comments = courtComments.get(courtName);
        }
        if(comments!=null && !comments.isEmpty()){
            updateComments();
        }
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

        LinkedList<Comment> list1 = new LinkedList<>();
        list1.add(new Comment("author1", 5,"comment1"));
        list1.add(new Comment("author2", 3, "comment2"));
        list1.add(new Comment("author3", 3, "comment3"));
        courtComments.put("Kinsmen Park", list1);
        LinkedList<Comment> list2 = new LinkedList<>();
        list2.add(new Comment("author1", 2, "comment1"));
        list2.add(new Comment("author2", 3,"comment2"));
        list2.add(new Comment("author3",2, "comment3"));
        courtComments.put("Summerside Park", list2);

        Intent intent = getIntent();
        courtName = intent.getStringExtra("courtName");

        TextView textView = findViewById(R.id.courtNameText);
        textView.setText(courtName);

        comments = courtComments.get(courtName);

        if(comments==null || comments.isEmpty()){
            TextView text = new TextView(this);
            text.setText(R.string.no_comment);

            ScrollView scrollView = findViewById(R.id.commentsView);

            ((LinearLayout)scrollView.getChildAt(0)).addView(text);
        } else{
            updateComments();
        }



    }

    public void addCommentPage(View v){
        Intent intent = new Intent(this, CreateComment.class);
        intent.putExtra("courtName", courtName);
        startActivity(intent);

        if(comments!=null && !comments.isEmpty()){
            updateComments();
        }
    }
    public void updateComments(){
        if(comments==null){
            comments = courtComments.get(courtName);
        }
        ScrollView scrollView = findViewById(R.id.commentsView);
        LinearLayout linearLayout = (LinearLayout) scrollView.getChildAt(0);
        linearLayout.removeAllViewsInLayout();
        for(Comment comment : comments){
            TextView author = new TextView(this);
            author.setText(comment.author);
            TextView commentText = new TextView(this);
            commentText.setText(comment.comment);


            linearLayout.addView(author);
            linearLayout.addView(commentText);
            linearLayout.addView(new Space(this));
        }
    }

    public void back(View v){
        finish();
    }
}