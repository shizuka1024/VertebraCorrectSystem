package com.example.vcsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ArticleReader extends AppCompatActivity {

    private static final String TAG = "TAG";

    private TextView title_view, content_view, titleView_inside_bar;
    private ImageView image_view;
    private String title, content ,picture;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_reader);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        picture = intent.getStringExtra("photo");

        title_view = findViewById(R.id.title);
        titleView_inside_bar = findViewById(R.id.titleinsidetoolbar);
        content_view = findViewById(R.id.content);
        image_view = findViewById(R.id.photo);
        backbtn = findViewById(R.id.backbutton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_view.setText(title);
        titleView_inside_bar.setText(title);
        content_view.setText(content);

        if(picture == null){
        }else{
            Glide.with(this)
                    .load(picture)
                    .into(image_view);
        }
    }
}