package com.example.vcsystem.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vcsystem.method.addArrayListMethodFHP;
import com.example.vcsystem.method.addArrayListMethodPitch;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class getReadModelPitch {
    private static final String TAG = "TAG";
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static String userID = firebaseAuth.getCurrentUser().getUid();
    private static ArrayList<Entry> mEntry = new ArrayList<>();
    private static ArrayList<String> mEntryDate = new ArrayList<>();
    private static SimpleDateFormat sdfonlydate = new SimpleDateFormat("yyyy-MM-dd");
    private static Date date = new Date();
    //進行轉換
    private static String dateString4 = sdfonlydate.format(date);

    public interface MyCallback {
        void onCallback(ArrayList<Entry> values, ArrayList<String> date);
    }

    public static void GetReadModel(MyCallback mCallback) {
        ArrayList<ReadModelPitch> mar = new ArrayList<>();
        ArrayList<Date> marTime = new ArrayList<>();

        firebaseFirestore.collection("users").document(userID).collection(dateString4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                mar.add(new ReadModelPitch(querySnapshot.getDouble("pitch")));
                                marTime.add(querySnapshot.getDate("dateSent"));
                                if(mar.size()>20){
                                    mar.remove(0);
                                }
                            }
                            //這邊帶入mEntry12，返回來mEntry12會有值
                            mEntry = addArrayListMethodPitch.addArrayList(mar);
                            mEntryDate = addArrayListMethodPitch.addArrayListDate(marTime);

                            mCallback.onCallback(mEntry, mEntryDate);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

