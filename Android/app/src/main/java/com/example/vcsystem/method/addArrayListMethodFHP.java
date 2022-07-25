package com.example.vcsystem.method;

import com.example.vcsystem.model.ReadModelNoFHP;
import com.example.vcsystem.model.ReadModelmFHP;
import com.example.vcsystem.model.ReadModelsFHP;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class addArrayListMethodFHP {
    private static final String TAG = "TAG";

    static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

    public static ArrayList<BarEntry> addArrayListNo(ArrayList<ReadModelNoFHP> mar) {
        ArrayList<BarEntry> mEntry = new ArrayList<>();
        int i = 0;
        for(ReadModelNoFHP e: mar){
            mEntry.add(new BarEntry(i, e.getNoFHP()));
            i++;
        }
        return mEntry;
    }

    public static ArrayList<BarEntry> addArrayListS(ArrayList<ReadModelsFHP> mar) {
        ArrayList<BarEntry> mEntry = new ArrayList<>();
        int i = 0;
        for(ReadModelsFHP e: mar){
            mEntry.add(new BarEntry(i, e.getsFHP()));
            i++;
        }
        return mEntry;
    }

    public static ArrayList<BarEntry> addArrayListM(ArrayList<ReadModelmFHP> mar) {
        ArrayList<BarEntry> mEntry = new ArrayList<>();
        int i = 0;
        for(ReadModelmFHP e: mar){
            mEntry.add(new BarEntry(i, e.getmFHP()));
            i++;
        }
        return mEntry;
    }

    public static ArrayList<String> addArrayListDate(ArrayList<Date> mar) {
        ArrayList<String> mEntry = new ArrayList<>();
        for(Date e: mar){
            mEntry.add(String.valueOf(sdf.format(e.getTime())));
        }
        return mEntry;
    }
}

