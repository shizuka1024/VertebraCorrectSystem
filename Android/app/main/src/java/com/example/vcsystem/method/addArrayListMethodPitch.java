package com.example.vcsystem.method;

import com.example.vcsystem.model.ReadModelPitch;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class addArrayListMethodPitch {
    private static final String TAG = "TAG";

    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static ArrayList<Entry> addArrayList(ArrayList<ReadModelPitch> mar) {
        ArrayList<Entry> mEntry = new ArrayList<>();
        mEntry.add(new Entry(0, 0));
        int i = 0;
        for(ReadModelPitch e: mar){
            mEntry.add(new Entry(i, e.getPitchInt()));
            i++;
        }
        return mEntry;
    }

    public static ArrayList<String> addArrayListDate(ArrayList<Date> mar) {
        ArrayList<String> mEntry = new ArrayList<>();
        int i = 0;
        for(Date e: mar){
            mEntry.add(String.valueOf(sdf.format(e.getTime())));
            i++;
        }
        return mEntry;
    }

}

