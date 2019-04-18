package com.healthcareproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity  {

    Firebase ref;
    private ArrayList<String> arrayList;
    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initviews();
        getData();
    }

    private void initviews(){
        recyclerView = findViewById(R.id.recyclerList);
        arrayList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(arrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatListAdapter);



    }

    private void getData(){
        ref =new Firebase("https://healthcareproject-4e970.firebaseio.com/chats/"+SharedPref.getInstance().getString(Constants.LOGIN_ID));



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                     arrayList.add(dataSnapshot.getKey());
                     chatListAdapter.notifyDataSetChanged();



//                MessageData messageData = new MessageData();
//                messageData.setMsg_type(map.get("msg_type"));
//                messageData.setTime_stamp(map.get("time_stamp"));
//                messageData.setMessage(map.get("message"));
//                arrayList.add(messageData);
//                chatDataAdapter.notifyDataSetChanged();
//                rv.scrollToPosition(arrayList.size() - 1);
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

