package com.example.vcsystem.model;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vcsystem.method.addArrayListMethodFHP;
import com.example.vcsystem.model.ReadModelmFHP;
import com.example.vcsystem.model.ReadModelsFHP;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class getReadModelFHP {
    private static final String TAG = "TAG";
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static String userID = firebaseAuth.getCurrentUser().getUid();
//    private static ArrayList<BarEntry> mEntryS = new ArrayList<>();
//    private static ArrayList<BarEntry> mEntryM = new ArrayList<>();
//    private static ArrayList<String> mEntryDate = new ArrayList<>();
    private static SimpleDateFormat sdfonlydate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
    private static String dateString4;

    public interface MyCallback {
        void onCallback(ArrayList<BarEntry> values1, ArrayList<BarEntry> values2, ArrayList<BarEntry> values3, ArrayList<String> date);
    }
        //給每個document的版本
//    public static void GetReadModel(MyCallback mCallback) {
//        ArrayList<ReadModelsFHP> marS = new ArrayList<>();
//        ArrayList<ReadModelmFHP> marM = new ArrayList<>();
//        ArrayList<Date> marTime = new ArrayList<>();
//
//        firebaseFirestore.collection("users").document(userID).collection("barGraph")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot querySnapshot : task.getResult()) {
//                                marS.add(new ReadModelsFHP(querySnapshot.getLong("sFHP")));
//                                marM.add(new ReadModelmFHP(querySnapshot.getLong("mFHP")));
//                                marTime.add(querySnapshot.getDate("dateSent"));
//                            }
//                            //這邊帶入mEntry12，返回來mEntry12會有值
//                            mEntryS = addArrayListMethodFHP.addArrayListS(marS);
//                            mEntryM = addArrayListMethodFHP.addArrayListM(marM);
//                            mEntryDate = addArrayListMethodFHP.addArrayListDate(marTime);
//
//                            mCallback.onCallback(mEntryS, mEntryM, mEntryDate);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

    public static void GetInsideReadModel(MyCallback mCallback) {
        ArrayList<BarEntry> mEntryN = new ArrayList<>();
        ArrayList<BarEntry> mEntryS = new ArrayList<>();
        ArrayList<BarEntry> mEntryM = new ArrayList<>();
        ArrayList<String> mEntryDate = new ArrayList<>();
        int[] marN = new int[7];
        int[] marS = new int[7];
        int[] marM = new int[7];
        int[][] output = new int[7][3];
        for (int week = -6; week <= 0; week++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, week);
            Date date = calendar.getTime();
            dateString4 = sdfonlydate.format(date);
            String dateString5 = sdf.format(date);
            mEntryDate.add(dateString5);
            int finalWeek = week + 6;
            firebaseFirestore.collection("users").document(userID).collection("barGraph").whereEqualTo("dateSent4", dateString4)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int mFHP = 0;
                                int sFHP = 0;
                                int nFHP = 0;
                                for (DocumentSnapshot querySnapshot : task.getResult()) {
                                    nFHP += querySnapshot.getLong("noFHP");
                                    sFHP += querySnapshot.getLong("sFHP");
                                    mFHP += querySnapshot.getLong("mFHP");
                                }
                                marN[finalWeek] = nFHP;
                                marS[finalWeek] = sFHP;
                                marM[finalWeek] = mFHP;
                                for (int i = 0; i < 7; i++) {
                                    output[i][0] = marN[i];
                                    output[i][1] = marS[i];
                                    output[i][2] = marM[i];
                                }
                                if (finalWeek == 6) {
                                    for (int i = 0; i < 7; i++) {
                                        mEntryN.add(new BarEntry(i, output[i][0]));
                                        Log.d(TAG, "nEntry" + mEntryN);
                                        mEntryS.add(new BarEntry(i, output[i][1]));
                                        Log.d(TAG, "sEntry" + mEntryS);
                                        mEntryM.add(new BarEntry(i, output[i][2]));
                                        Log.d(TAG, "mEntry" + mEntryM);
                                    }
                                    mCallback.onCallback(mEntryN, mEntryS, mEntryM, mEntryDate);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}

