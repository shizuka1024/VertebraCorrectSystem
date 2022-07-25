package com.example.vcsystem.method;

import com.example.vcsystem.model.ReadModel12;
import com.example.vcsystem.model.ReadModel27;
import com.example.vcsystem.model.ReadModel40;
import com.example.vcsystem.model.ReadModel49;
import com.example.vcsystem.model.ReadModel60;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class addArrayListMethodforPie {
    private static final String TAG = "TAG";

    public static ArrayList<PieEntry> addArrayList(ArrayList<ReadModel12> mar12, ArrayList<ReadModel27> mar27, ArrayList<ReadModel40> mar40, ArrayList<ReadModel49> mar49, ArrayList<ReadModel60> mar60) {
        ArrayList<PieEntry> mEntry = new ArrayList<>();
        int i = 0,j = 0,k = 0,l = 0,m = 0;
        for(ReadModel12 e: mar12){
            i +=e.getLbs12();
        }
        for(ReadModel27 e: mar27){
            j +=e.getLbs27();
        }
        for(ReadModel40 e: mar40){
            k +=e.getLbs40();
        }
        for(ReadModel49 e: mar49){
            l +=e.getLbs49();
        }
        for(ReadModel60 e: mar60){
            m +=e.getLbs60();
        }
        mEntry.add(new PieEntry((i*3)/60, "lbs12"));
        mEntry.add(new PieEntry((j*3)/60, "lbs27"));
        mEntry.add(new PieEntry((k*3)/60, "lbs40"));
        mEntry.add(new PieEntry((l*3)/60, "lbs49"));
        mEntry.add(new PieEntry((m*3)/60, "lbs60"));

        return mEntry; // modified 2021/05/23
    }
}

