package com.example.vcsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private TextView mSignupBtn, mForgetpasswordBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private CheckBox remember_key, automatic_login;
    private SharedPreferences sp;
    private Boolean rem_isCheck = false;
    private Boolean auto_isCheck = false;

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //開啟Preferences，名稱為userInfo，如果存在則開啟它，否則建立新的Preferences
        //Context.MODE_PRIVATE：指定該SharedPreferences資料只能被本應用程式讀、寫
        //Context.MODE_WORLD_READABLE：指定該SharedPreferences資料能被其他應用程式讀，但不能寫
        //Context.MODE_WORLD_WRITEABLE：指定該SharedPreferences資料能被其他應用程式讀寫。
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginbutton);
        mSignupBtn = findViewById(R.id.signup);
        mForgetpasswordBtn = findViewById(R.id.forgotpass);
        remember_key = (CheckBox) findViewById(R.id.remember_key);
        automatic_login = (CheckBox) findViewById(R.id.automatic_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_loader);

        Log.d(TAG, "Login Ready");

        remember_key.setChecked(true);//設定記住密碼初始化為true

        //判斷記住密碼多選框的狀態
        if (sp.getBoolean("rem_isCheck", false)) {
            //設定預設是記錄密碼狀態
            remember_key.setChecked(true);
            mEmail.setText(sp.getString("USER_NAME", ""));
            mPassword.setText(sp.getString("PASSWORD", ""));
            Log.e("自動恢復儲存的賬號密碼", "自動恢復儲存的賬號密碼");

            //判斷自動登陸多選框狀態
            if (sp.getBoolean("auto_isCheck", false)) {
                //設定預設是自動登入狀態
                automatic_login.setChecked(true);
                //跳轉介面
                Intent intent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(intent);
                Toast toast1 = Toast.makeText(getApplicationContext(), "已自動登入", Toast.LENGTH_SHORT);
                Log.e("自動登陸", "自動登陸");
            }
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();
                rem_isCheck = remember_key.isChecked();
                auto_isCheck = automatic_login.isChecked();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >=6.");
                    return;
                }

                if (remember_key.isChecked()) {
                    //記住使用者名稱、密碼、
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", email);
                    editor.putString("PASSWORD", password);
                    editor.putBoolean("rem_isCheck", rem_isCheck);
                    editor.putBoolean("auto_isCheck", auto_isCheck);
                    editor.commit();

                    Log.d(TAG, "賬號：" + email +
                            "\n" + "密碼：" + password +
                            "\n" + "是否記住密碼：" + rem_isCheck +
                            "\n" + "是否自動登陸：" + auto_isCheck);
                    editor.commit();
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        mForgetpasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetEmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "ERROR! Reset Link is net Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
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
}