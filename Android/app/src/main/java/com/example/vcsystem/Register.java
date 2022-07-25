package com.example.vcsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mUsername, mEmail, mPassword;
    Button mSignupBtn;
    CheckBox checkBox;
    TextView maAlreadyhaveaccountBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;
    String userID;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignupBtn = findViewById(R.id.signupbutton);
        maAlreadyhaveaccountBtn = findViewById(R.id.alreadyHaveAccount);
        checkBox = findViewById(R.id.checkbox);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_loader);
//         如果有帳戶就直接進入MA
//        if(firebaseAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= mUsername.getText().toString();
                String email= mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    mUsername.setError("Username is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >=6.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            user.put("password", password);
                            user.put("uid", userID);
                            user.put("usergroup", "patient");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for"+ userID);
                                }
                            });
                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure" + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        maAlreadyhaveaccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }

    /**
     * 判斷是否為身份證字號格式 (
     */
    public boolean isIdNoFormat(String word) {
        boolean check = true;

        for(int i = 0 ; i < word.length();i++) {
            String s = word.substring(i,i+1);
            if(i==0) {
                if(!isEnglish(s)) {
                    check = false;
                    break;
                }
            } else {
                if(!isDigit(s)) {
                    check = false;
                    break;
                }
            }
        }

        return check;
    }


    /**
     * 英文判斷
     */
    public boolean isEnglish(String word){
        boolean check = true;
        for(int i=0;i<word.length();i++) {
            int c = (int)word.charAt(i);
            if(c>=65 && c<=90 || c>=97 && c<=122) {
                //是英文
            } else {
                check = false;
            }
        }
        return check;
    }

    /**
     * 數字判斷
     */
    public boolean isDigit(String word) {
        boolean check = true;
        for(int i=0;i<word.length();i++) {
            int c = (int)word.charAt(i);
            if(c>=48 && c<=57 ) {
                //是數字
            } else {
                check = false;
            }
        }
        return check;
    }
}