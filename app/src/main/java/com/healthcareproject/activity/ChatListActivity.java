package com.healthcareproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.healthcareproject.R;
import com.healthcareproject.adapter.ChatListAdapter;
import com.healthcareproject.model.model;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;
import java.util.ArrayList;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity  {

    Firebase ref;
    Firebase ref1;
    private ArrayList<model> arrayList;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initviews();
        getData();
    }

    private void initviews(){
        RecyclerView recyclerView = findViewById(R.id.recyclerList);
        arrayList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(arrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatListAdapter);
        ref1 =new Firebase("https://healthcareproject-4e970.firebaseio.com/");
    }

    private void getData(){
        ref =new Firebase("https://healthcareproject-4e970.firebaseio.com/chats/"+ SharedPref.getInstance().getString(Constants.LOGIN_ID));



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                     ref1=new Firebase("https://healthcareproject-4e970.firebaseio.com/users/" + dataSnapshot.getKey() + "/");
                     ref1.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot1) {
                             Map<String, String> map = dataSnapshot1.getValue(Map.class);
                             arrayList.add(new model(dataSnapshot.getKey(),map.get("name")));
                             chatListAdapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onCancelled(FirebaseError firebaseError) {

                         }
                     });
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


}

