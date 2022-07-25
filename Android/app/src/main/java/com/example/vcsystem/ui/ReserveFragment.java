package com.example.vcsystem.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vcsystem.EditProfile;
import com.example.vcsystem.R;
import com.example.vcsystem.adapter.BookedAdapter;
import com.example.vcsystem.adapter.ReserveAdapter;
import com.example.vcsystem.model.ReserveModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReserveFragment extends Fragment {

    private static final String TAG = "TAG";
    private FirebaseAuth firebaseAuth;
    private static FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static String userID;
    private Spinner sp_hospital, sp_outpatient, sp_doctor;
    ArrayList<Contact> al_hospital = new ArrayList<>();
    ArrayList<Contact> al_outpatient = new ArrayList<>();
    ArrayList<Contact> al_doctor = new ArrayList<>();
    private RecyclerView reserve_list;
    private RecyclerView booked_list;
    private ReserveAdapter mAdapter;
    private BookedAdapter nAdapter;
    private List<ReserveModel> list;
    private List<ReserveModel> listforbooked;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManagerforbooked;
    String[] al_search = new String[3];

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reserve, container, false);
        sp_hospital = root.findViewById(R.id.sp_hospital);
        sp_outpatient = root.findViewById(R.id.sp_outpatient);
        sp_doctor = root.findViewById(R.id.sp_doctor);
        reserve_list = root.findViewById(R.id.reserve_list);
        booked_list = root.findViewById(R.id.booked_list);
        list = new ArrayList<>();
        listforbooked = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManagerforbooked = new LinearLayoutManager(getContext());
        mAdapter = new ReserveAdapter(list, getContext());
        nAdapter = new BookedAdapter(listforbooked, getContext());
        reserve_list.setLayoutManager(linearLayoutManager);
        reserve_list.setHasFixedSize(true);
        reserve_list.setAdapter(mAdapter);
        reserve_list.scrollToPosition(mAdapter.getItemCount() - 1);
        booked_list.setLayoutManager(linearLayoutManagerforbooked);
        booked_list.setHasFixedSize(true);
        booked_list.setAdapter(nAdapter);
        booked_list.scrollToPosition(nAdapter.getItemCount() - 1);

        initSpinnerHospital();

        sp_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = parent.getItemAtPosition(position).toString();
                initSpinnerOutpatient(msg);
                al_search[0] = msg;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_outpatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = parent.getItemAtPosition(position).toString();
                initSpinnerDoctor(msg);
                al_search[1] = msg;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = parent.getItemAtPosition(position).toString();
                al_search[2] = msg;
                if (reserve_list.getChildCount() > 0) {
                    reserve_list.removeAllViews();
                }
                startListeningForList(al_search);
                startListeningForBookedList(al_search);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return root;
    }

    private void initSpinnerHospital() {
        firebaseFirestore.collection("users")
                .whereEqualTo("usergroup", "doctor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            arrayList.add(querySnapshot.getString("hospital"));
                        }
                        al_hospital = getSingle(arrayList);
                        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, al_hospital);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_hospital.setAdapter(adapter);
                    }
                });
    }

    private void initSpinnerOutpatient(String msg) {
        firebaseFirestore.collection("users")
                .whereEqualTo("hospital", msg)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            arrayList.add(querySnapshot.getString("outpatient"));
                        }
                        al_outpatient = getSingle(arrayList);
                        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, al_outpatient);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_outpatient.setAdapter(adapter);
                    }
                });
    }

    private void initSpinnerDoctor(String msg) {
        firebaseFirestore.collection("users")
                .whereEqualTo("outpatient", msg)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            arrayList.add(querySnapshot.getString("username"));
                        }
                        al_doctor = getSingle(arrayList);
                        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, al_doctor);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_doctor.setAdapter(adapter);
                    }
                });
    }

    private void startListeningForList(String[] arrayList) {
        Log.d(TAG, "startListeningForList");
        firebaseFirestore.collection("users")
                .whereEqualTo("hospital", arrayList[0])
                .whereEqualTo("outpatient", arrayList[1])
                .whereEqualTo("username", arrayList[2])
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("uid");
                                firebaseFirestore.collection("Calendar")
                                        .whereEqualTo("uid", uid)
                                        .whereEqualTo("看診", "null")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                list.clear();
                                                if (error != null) {
                                                    //an error has occured
                                                } else {
                                                    list = value.toObjects(ReserveModel.class);
                                                    mAdapter.setData(list);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void startListeningForBookedList(String[] arrayList) {
        Log.d(TAG, "startListeningForList");
        firebaseFirestore.collection("users")
                .whereEqualTo("hospital", arrayList[0])
                .whereEqualTo("outpatient", arrayList[1])
                .whereEqualTo("username", arrayList[2])
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("uid");
                                firebaseFirestore.collection("Calendar")
                                        .whereEqualTo("uid", uid)
                                        .whereEqualTo("看診", userID)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                listforbooked.clear();
                                                if (error != null) {
                                                    //an error has occured
                                                } else {
                                                    listforbooked = value.toObjects(ReserveModel.class);
                                                    nAdapter.setData(listforbooked);
                                                    nAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static ArrayList getSingle(ArrayList list) {
        ArrayList tempList = new ArrayList();          //1,创建新集合
        Iterator it = list.iterator();              //2,根据传入的集合(老集合)获取迭代器
        while (it.hasNext()) {                  //3,遍历老集合
            Object obj = it.next();                //记录住每一个元素
            if (!tempList.contains(obj)) {            //如果新集合中不包含老集合中的元素
                tempList.add(obj);                //将该元素添加
            }
        }
        return tempList;
    }

    private class Contact {
        private String contact_name;
        private String contact_id;

        public Contact() {
        }

        public Contact(String contact_name) {
            this.contact_name = contact_name;
            this.contact_id = contact_name;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getContact_id() {
            return contact_id;
        }

        public void setContact_id(String contact_id) {
            this.contact_id = contact_id;
        }

        /**
         * Pay attention here, you have to override the toString method as the
         * ArrayAdapter will reads the toString of the given object for the name
         *
         * @return contact_name
         */
        @Override
        public String toString() {
            return contact_name;
        }
    }

    public static void onYesRequested(ReserveModel reserveModel) {
        firebaseFirestore.collection("Calendar")
                .whereEqualTo("uid", reserveModel.getUid())
                .whereEqualTo("dateSent3", reserveModel.getDateSent3())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                firebaseFirestore.collection("Calendar")
                                        .document(document.getId())
                                        .update("看診", userID)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public static void onNoRequested(ReserveModel reserveModel) {
        firebaseFirestore.collection("Calendar")
                .whereEqualTo("dateSent3", reserveModel.getDateSent3())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                firebaseFirestore.collection("Calendar")
                                        .document(document.getId())
                                        .update("看診", "null")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                            }
                        }
                    }
                });
    }

}