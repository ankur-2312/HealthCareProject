package com.healthcareproject.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcareproject.R;
import com.healthcareproject.adapter.ChatDataAdapter;
import com.healthcareproject.model.MessageData;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;

import java.util.ArrayList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private String receiver_id;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseReceive;
    private ImageView ivSend;
    private EditText editText;
    private Firebase ref;
    private ArrayList<MessageData> arrayList;
    private ChatDataAdapter chatDataAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        receiver_id = getIntent().getStringExtra(Constants.INTENT_DOCTOR_ID_KEY);
        databaseChat = FirebaseDatabase.getInstance().getReference().child("chats");
        databaseReceive = FirebaseDatabase.getInstance().getReference().child("chats");
        getData();
    }

    private void initViews() {
        ivSend = findViewById(R.id.ivSend);
        editText = findViewById(R.id.editText);
        ivSend.setOnClickListener(this);
        editText.addTextChangedListener(this);
        rv = findViewById(R.id.rv);
        arrayList = new ArrayList<>();
        chatDataAdapter = new ChatDataAdapter(arrayList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(chatDataAdapter);
    }

    @Override
    public void onClick(View v) {

        Log.i(Constants.TAG, "click");

        MessageData msgData = new MessageData();
        msgData.setMessage(editText.getText().toString());
        msgData.setMsg_type(Constants.MSG_TYPE_SENDER);
        msgData.setTime_stamp("123");


        DatabaseReference db1 = databaseChat.child(SharedPref.getInstance().getString(Constants.LOGIN_ID));
        DatabaseReference db2 = db1.child(receiver_id);
        db2.push().setValue(msgData);

        msgData.setMsg_type(Constants.MSG_TYPE_RECEIVER);

        DatabaseReference db3 = databaseReceive.child(receiver_id);
        DatabaseReference db4 = db3.child(SharedPref.getInstance().getString(Constants.LOGIN_ID));
        db4.push().setValue(msgData);


    }

    private void getData() {

        ref = new Firebase("https://healthcareproject-4e970.firebaseio.com/chats/" + SharedPref.getInstance().getString(Constants.LOGIN_ID) + "/" + receiver_id);
        //ref = new Firebase("https://healthcareproject-4e970.firebaseio.com/chats/GZKyuivw0WZvPtytW2xaCUXVcRL2/I7yP2MlQcngslF3OfImehFZ8zjl2");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map = dataSnapshot.getValue(Map.class);

                Log.i(Constants.TAG, "" + map.get("message"));

                MessageData messageData = new MessageData();
                messageData.setMsg_type(map.get("msg_type"));
                messageData.setTime_stamp(map.get("time_stamp"));
                messageData.setMessage(map.get("message"));
                arrayList.add(messageData);
                chatDataAdapter.notifyDataSetChanged();
                rv.scrollToPosition(arrayList.size() - 1);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!editText.getText().toString().trim().isEmpty()) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int) (60 * Resources.getSystem().getDisplayMetrics().density);
            editText.setLayoutParams(params);
            ivSend.setVisibility(View.VISIBLE);

        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            editText.setLayoutParams(params);
            ivSend.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
