package com.example.vcsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vcsystem.adapter.MessagesAdapter;
import com.example.vcsystem.model.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class ChatActivity extends AppCompatActivity {

    private ImageButton backbuttonofcchat;
    private ImageView telemedicinebtn;
    private TextView nameofuser;
    private String mrecievername, sendername, mrecieveruid, msenderuid;
    String senderroom, recieverroom;
    String mAccount;

    private EditText edtInput;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private InputMethodManager imm;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    MessagesAdapter mAdapter;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = ChatActivity.this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        backbuttonofcchat = findViewById(R.id.backbuttonofspecificchat);
        nameofuser = findViewById(R.id.Nameofspecificuser);
        telemedicinebtn = findViewById(R.id.telemedicine_btn);

        telemedicinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Telemedicine.class));
            }
        });

        backbuttonofcchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        msenderuid = firebaseAuth.getUid();
        Log.d(TAG, "uid"+msenderuid);
        mrecieveruid = getIntent().getStringExtra("uid");
        mrecievername = getIntent().getStringExtra("name");

        nameofuser.setText(mrecievername);

        senderroom = msenderuid + mrecieveruid;

        DocumentReference documentReference = firebaseFirestore.collection("users").document(msenderuid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( @Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                mAccount = documentSnapshot.getString("username");

                Log.d(TAG, "ma"+mAccount);

                Map<String, Object> senderInformation = new HashMap<>();
                senderInformation.put("senderName", mAccount);
                senderInformation.put("senderUid", msenderuid);
                senderInformation.put("recieverName", mrecievername);
                senderInformation.put("recieverUid", mrecieveruid);

                firebaseFirestore.collection("messages").document(senderroom)
                        .set(senderInformation)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });


        findViewAndGetInstance();//綁定各種view與實體化

        //訊息框
        edtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(edtInput.getText())) {
                        sendMsg();
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//送出後鍵盤收起
                    }
                }
                return true;
            }
        });

    }

    private void findViewAndGetInstance() {

        mAdapter = new MessagesAdapter(firebaseAuth.getCurrentUser().getUid());
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        edtInput = findViewById(R.id.edtInput);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //送出訊息
    public void fabSend(View v) {
        try {
            if (!TextUtils.isEmpty(edtInput.getText())) {
                sendMsg();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//送出後鍵盤收起
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg() {
        String msg = edtInput.getText().toString();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String username = documentSnapshot.getString("username");
                ChatMessage message = new ChatMessage(username, msg, firebaseAuth.getUid());
                firebaseFirestore.collection("messages").document(senderroom).collection("message")
                        .add(message)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {
                                    Log.d(TAG, "send failed");
                                } else {
                                    edtInput.setText("");
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListeningForMessages();
        getSupportActionBar().hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getSupportActionBar().show();
    }

    private void startListeningForMessages() {
        Log.d(TAG, "startListeningForMessages");

        firebaseFirestore.collection("messages").document(senderroom).collection("message")
                .orderBy("dateSent")
                .addSnapshotListener(this, (snapshots, e) -> {
                    if (e != null) {
                        //an error has occured
                    } else {
                        List<ChatMessage> messages = snapshots.toObjects(ChatMessage.class);
                        Log.d("chat", "" + messages);
                        mAdapter.setData(messages);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

}