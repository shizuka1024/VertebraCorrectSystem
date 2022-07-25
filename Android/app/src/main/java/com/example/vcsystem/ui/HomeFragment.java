package com.example.vcsystem.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vcsystem.ChatsGroup;
import com.example.vcsystem.R;
import com.example.vcsystem.adapter.BulletinBoardAdapter;
import com.example.vcsystem.adapter.ChatsGroupAdapter;
import com.example.vcsystem.model.BulletinBoardModel;
import com.example.vcsystem.model.chatsGroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG";

    private RecyclerView bulletinboard;
    private BulletinBoardAdapter mAdapter;
    private List<BulletinBoardModel> list;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        bulletinboard = root.findViewById(R.id.bulletinBoard);
        list = new ArrayList<>();
        mAdapter = new BulletinBoardAdapter(list, context);
        linearLayoutManager = new LinearLayoutManager(context);

        bulletinboard.setLayoutManager(linearLayoutManager);
        bulletinboard.setHasFixedSize(true);
        bulletinboard.setAdapter(mAdapter);
        bulletinboard.scrollToPosition(mAdapter.getItemCount() - 1);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        startListeningForList();
    }

    private void startListeningForList() {
        Log.d(TAG, "startListeningForList");
        firebaseFirestore.collection("guides")
                .orderBy("dateSent", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        if (error != null) {
                            //an error has occured
                        } else {
                            list = value.toObjects(BulletinBoardModel.class);
                            mAdapter.setData(list);
//                        Log.d(TAG,outputStudents(list));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}