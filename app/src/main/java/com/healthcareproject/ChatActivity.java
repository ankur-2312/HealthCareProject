package com.healthcareproject;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private String receiver_id;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseReceive;
    private Button butsend;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        receiver_id=getIntent().getStringExtra(Constants.INTENT_DOCTOR_ID_KEY);
        databaseChat = FirebaseDatabase.getInstance().getReference().child("chats");
        databaseReceive=FirebaseDatabase.getInstance().getReference().child("chats");


    }

    private void initViews(){
        butsend=findViewById(R.id.butsend);
        editText=findViewById(R.id.editText);
        butsend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.i(Constants.TAG,"click");

        DoctorPojo db=new DoctorPojo();
        db.setId("sldlsmd");
        DatabaseReference db1=databaseChat.child(SharedPref.getInstance().getString(Constants.LOGIN_ID));
        DatabaseReference db2=db1.child(receiver_id);
        db2.push().child("msg").setValue(editText.getText().toString());
        db2.push().child("msg_type").setValue(db);

        DatabaseReference db3=databaseReceive.child(receiver_id);
        DatabaseReference db4=db3.child(SharedPref.getInstance().getString(Constants.LOGIN_ID));
        db4.push().child("msg").setValue(editText.getText().toString());
        db4.push().child("msg_type").setValue(db);


    }
}
