package com.example.vcsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    EditText mUsername, mRealId, mPhone, mAddress;
    TextView finishbtn, cancelbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mUsername = findViewById(R.id.username);
        mRealId = findViewById(R.id.realId);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        finishbtn = findViewById(R.id.finishbtn);
        cancelbtn = findViewById(R.id.cancelbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        knownInformation();

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
                Intent intent = new Intent();
                intent.setClass(EditProfile.this, MainActivity.class);
                intent.putExtra("id", 5);
                startActivity(intent);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EditProfile.this, MainActivity.class);
                intent.putExtra("id", 5);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getSupportActionBar().show();
    }

    private void knownInformation(){
        firebaseFirestore.collection("users").document(userID).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                mUsername.setText(documentSnapshot.getString("username"));
                mRealId.setText(documentSnapshot.getString("realId"));
                mPhone.setText(documentSnapshot.getString("phone"));
                mAddress.setText(documentSnapshot.getString("address"));
            }
        });
    }


    private void addDetails(){
        String username = mUsername.getText().toString();
        String realId = mRealId.getText().toString();
        String phone = mPhone.getText().toString();
        String address = mAddress.getText().toString();

        Map<String, Object> userDetails = new HashMap<>();
        if(isEmpty(mUsername)){
            userDetails.put("username", username);
        }
        if(isEmpty(mRealId)){
            userDetails.put("realId", realId);
        }
        if(isEmpty(mPhone)){
            userDetails.put("phone", phone);
        }
        if(isEmpty(mAddress)){
            userDetails.put("address", address);
        }
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.update(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess");
                Toast.makeText(EditProfile.this, "更新成功", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure" + e.toString());
                Toast.makeText(EditProfile.this, "更新失敗", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0){
            return true;
        }
        return false;
    }

}