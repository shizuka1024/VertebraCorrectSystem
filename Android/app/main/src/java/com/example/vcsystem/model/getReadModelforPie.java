package com.example.vcsystem.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vcsystem.method.addArrayListMethodforPie;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class getReadModelforPie {
    private static final String TAG = "TAG";
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static String userID = firebaseAuth.getCurrentUser().getUid();
    private static ArrayList<PieEntry> mEntry = new ArrayList<>();

    public interface MyCallback {
        void onCallback(ArrayList<PieEntry> values);
    }

    public static void GetReadModel(String time, MyCallback mCallback) {
        ArrayList<ReadModel12> mar12 = new ArrayList<>();
        ArrayList<ReadModel27> mar27 = new ArrayList<>();
        ArrayList<ReadModel40> mar40 = new ArrayList<>();
        ArrayList<ReadModel49> mar49 = new ArrayList<>();
        ArrayList<ReadModel60> mar60 = new ArrayList<>();

        Log.d(TAG,"select time"+time);

        firebaseFirestore.collection("users").document(userID).collection("pieGraph").whereEqualTo("dateSent3", time)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                mar12.add(new ReadModel12(querySnapshot.getLong("lbs12")));
                                mar27.add(new ReadModel27(querySnapshot.getLong("lbs27")));
                                mar40.add(new ReadModel40(querySnapshot.getLong("lbs40")));
                                mar49.add(new ReadModel49(querySnapshot.getLong("lbs49")));
                                mar60.add(new ReadModel60(querySnapshot.getLong("lbs60")));
                            }
                            mEntry = addArrayListMethodforPie.addArrayList(mar12,mar27,mar40,mar49,mar60);
                            mCallback.onCallback(mEntry);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

