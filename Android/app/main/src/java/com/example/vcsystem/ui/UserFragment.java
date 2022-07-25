package com.example.vcsystem.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vcsystem.EditProfile;
import com.example.vcsystem.Login;
import com.example.vcsystem.MainActivity;
import com.example.vcsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserFragment extends Fragment {
    private static final String TAG = "TAG";

    private TextView mUsername, mAccount, mPassword, mRealId, mPhone, mAddress;
    private ImageView profile_image, logout;
    private Button edit_profile;
    private String userID, fileName, ImageUrl, profilePicture;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SimpleDateFormat sdf;
    private Date date;
    private Uri imageUri;
    private SharedPreferences sp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
        date = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        mUsername = root.findViewById(R.id.yourName);
        mAccount = root.findViewById(R.id.yourAccount);
        mPassword = root.findViewById(R.id.yourPassword);
        mRealId = root.findViewById(R.id.yourRealId);
        mPhone = root.findViewById(R.id.yourPhone);
        mAddress = root.findViewById(R.id.yourAddress);
        edit_profile = root.findViewById(R.id.AddDetailsButton);

        profile_image = root.findViewById(R.id.profile_image);
        logout = root.findViewById(R.id.logout);
        fileName = sdf.format(date);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    mUsername.setText(document.getString("username"));
                    mAccount.setText(document.getString("email"));
                    mPassword.setText(document.getString("password"));
                    mRealId.setText(document.getString("realId"));
                    mPhone.setText(document.getString("phone"));
                    mAddress.setText(document.getString("address"));
                    profilePicture = document.getString("photo");
                    Log.d(TAG, "Cached document data: " + profilePicture);
                    if(profilePicture == null){

                    }else{
                        Glide.with(getActivity())
                                .load(profilePicture)
                                .into(profile_image);
                    }
                    Log.d(TAG, "Cached document data: " + document.getData());
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    selectImage();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), EditProfile.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout(v);
            }
        });

        return root;
    }

    private void selectImage() {
        Log.d(TAG, "selectImage");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void uploadImage() {
        Log.d(TAG, "uploadImage");
        storageReference = FirebaseStorage.getInstance().getReference("imagesUsersProfile/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                ImageUrl = task.getResult().toString();
                                Log.d(TAG, "URL" + ImageUrl);

                                Map<String, Object> picture = new HashMap<>();
                                picture.put("photo", ImageUrl);
                                firebaseFirestore.collection("users")
                                        .document(userID)
                                        .update(picture)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "加入照片成功", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        });
                        //上傳完後有照片，但一兩秒照片又變空白
//                        profile_image.setImageURI(null);
                        Toast.makeText(getActivity(), "上傳成功", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "上傳失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void Logout(View view) {
        startActivity(new Intent(getActivity(), Login.class));
        sp.edit().putBoolean("automatic_login",false).commit();
        sp.edit().putBoolean("rem_isCheck",false).commit();
        firebaseAuth.signOut();
    }
}