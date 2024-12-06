package com.example.publictenniscourtavailabilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static String userId = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createFile();
        addReviews();
    }
    //just using for testing
    public void map (View view){
        Intent intent = new Intent(MainActivity.this, Map.class);
        startActivity(intent);
    }

    //just using for testing
    public void messages (View view){
        Intent intent = new Intent(MainActivity.this, Messages.class);
        startActivity(intent);
    }


    private void createFile(){
        String data = "Kinsmen Park|1|00:00\n" +
                "Kinsmen Park|2|30:10\n" +
                "Kinsmen Park|3|05:40\n" +
                "Kinsmen Park|4|00:00\n" +
                "Hartwick Park|1|00:00\n" +
                "Hartwick Park|2|10:00\n" +
                "Parkinson Rec|1|15:32\n" +
                "Parkinson Rec|2|00:00\n" +
                "Birkdale Park|1|00:00\n" +
                "Birkdale Park|2|20:00\n" +
                "City Park|1|00:00\n" +
                "City Park|2|45:03\n" +
                "City Park|3|00:00\n" +
                "City Park|4|31:05\n" +
                "City Park|5|00:00\n" +
                "City Park|6|04:20\n" +
                "Blair Pond Park|1|08:21\n" +
                "Blair Pond Park|2|00:00\n" +
                "Gerstmar Park|1|21:00\n" +
                "Summerside Park|1|00:00\n" +
                "Summerside Park|2|00:00\n" +
                "Summerside Park|3|00:00\n" +
                "Summerside Park|4|55:30\n";

        // Create or write to a file
        FileOutputStream fos = null;
        try {
            // Create a file in the app's internal storage
            File file = new File(getFilesDir(), "courts_availability_data.txt");
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addReviews() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        CommentDao commentDao = db.commentDao();

        Comment[] comments = {
                new Comment("user1", "Tom", 3, "Honestly a very mid tennis court like it is mid in every way. It is not a bad tennis court but it is no a good tennis court just very mid.", "Gerstmar Park"),
                new Comment("user2", "Bobby", 5, "Best tennis court I have every played on. I have never seen a better tennis court in my whole entire life.", "Summerside Park"),
                new Comment("user1", "Tom", 4, "Decent tennis court, unlike the Gerstmar tennis court this tennis court is good instead of mid.","Summerside Park"),
                new Comment("user3", "Jess", 1, "Horrible tennis court. I refuse to elaborate.", "City Park"),
                new Comment("user3", "Jess", 5, "Best tennis court ever!!1!!!111!!!", "Blair Pond Park")
        };
        for(Comment comment:comments){
            if(commentDao.findByUserIdAndCourt(comment.userId,comment.court)==null){
                commentDao.insertAll(comment);
            }
        }
    }
}