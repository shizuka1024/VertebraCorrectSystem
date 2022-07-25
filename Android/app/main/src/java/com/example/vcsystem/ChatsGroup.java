package com.example.vcsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vcsystem.adapter.ChatsGroupAdapter;
import com.example.vcsystem.model.chatsGroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatsGroup extends AppCompatActivity {
    private static final String TAG = "TAG";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mrecyclerview;
    private ImageButton backbuttonofcchat;
    private ChatsGroupAdapter mAdapter;
    private List<chatsGroupModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_group);
        context = ChatsGroup.this;

        findViewAndGetInstance();

        backbuttonofcchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViewAndGetInstance() {
        backbuttonofcchat = findViewById(R.id.backbuttonofspecificchat);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        mAdapter = new ChatsGroupAdapter(list, context);
        linearLayoutManager = new LinearLayoutManager(this);

        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setAdapter(mAdapter);
        mrecyclerview.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListeningForList();
        getSupportActionBar().hide();

    }

    @Override
    protected void onStop() {
        super.onStop();
        getSupportActionBar().show();
    }

    private void startListeningForList() {
        Log.d(TAG, "startListeningForList");
        firebaseFirestore.collection("users")
                .whereEqualTo("usergroup", "doctor")
                .addSnapshotListener(this, (snapshots, e) -> {
                    list.clear();
                    if (e != null) {
                        //an error has occured
                    } else {
                        list = snapshots.toObjects(chatsGroupModel.class);
                        mAdapter.setData(list);
//                        Log.d(TAG,outputStudents(list));
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

//    public String outputStudents(List<chatsGroupModel> studentsList) {
//        StringBuilder sb = new StringBuilder();
//        for(chatsGroupModel student: studentsList) {
//            sb.append(student.username);
//            sb.append(",");
//            sb.append(student.uid);
//            sb.append(";");
//        }
//        return String.valueOf(sb);
//    }

}