package com.healthcareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class OnlineConsultationList extends AppCompatActivity implements HorizontalRecyclerViewClickListener {

    private ArrayList<DoctorPojo> arrayList;
    private Firebase ref;
    private HorizontalListAdapter horizontalListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_consultation_list);
        init();
        loadData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.docRecyclerView);
        arrayList = new ArrayList<>();
        horizontalListAdapter = new HorizontalListAdapter(arrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(horizontalListAdapter);


    }

    private void loadData() {
        ref = new Firebase("https://healthcareproject-4e970.firebaseio.com/users");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                if (Objects.equals(map.get("user_type"), Constants.USER_DOCTOR)) {
                    DoctorPojo doctorPojo = new DoctorPojo();
                    doctorPojo.setDocName(map.get("name"));
                    doctorPojo.setQualification(map.get("qualification"));
                    doctorPojo.setSpeciality(map.get("speciality"));
                    doctorPojo.setRegistrationNo(map.get("registration_no"));
                    doctorPojo.setId(map.get("user_id"));

                    arrayList.add(doctorPojo);
                    horizontalListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    @Override
    public void onClick(View view, int position, String user_id) {
        Intent intent = new Intent(OnlineConsultationList.this, ChatActivity.class);
        intent.putExtra("key", user_id);
        startActivity(intent);


    }
}
