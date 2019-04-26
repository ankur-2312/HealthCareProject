package com.healthcareproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcareproject.R;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;

public class HomeDoctorActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivDoctorimage;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private Switch switchAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_doctor);
        init();
        Log.i(Constants.TAG,"445"+ SharedPref.getInstance().getString(Constants.USER_IMAGE));
        Glide.with(this).load(SharedPref.getInstance().getString(Constants.USER_IMAGE)).placeholder(R.drawable.ic_user_image_placeholder).into(ivDoctorimage);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("users");
    }

    private void init(){
        ivDoctorimage=findViewById(R.id.ivDp);
        ImageView ivChat = findViewById(R.id.ivChat);
        ImageView ivQuery=findViewById(R.id.ivQuery);
        ImageView ivShowMore=findViewById(R.id.ivShowMore);
        ivShowMore.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        ivQuery.setOnClickListener(this);
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(SharedPref.getInstance().getString(Constants.DOC_NAME));
        switchAvailability=findViewById(R.id.switchAvailability);
        switchAvailability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setAvailabilityTrue();
                }
                else {
                    setAvailabilityFalse();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivShowMore:
                showMessageOKCancel1("Do you want to Logout",
                        new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAvailabilityFalse();
                                SharedPref.getInstance().delete();
                                SharedPref.getInstance().setBoolean(Constants.LOGIN_CHECK, false);
                                startActivity(new Intent(HomeDoctorActivity.this, LoginActivity.class));
                                auth.signOut();
                                finish();
                            }
                        });
                break;

            case R.id.ivChat:
                startActivity(new Intent(HomeDoctorActivity.this, ChatListActivity.class));
                break;

            case R.id.ivQuery:
                Intent in=new Intent(HomeDoctorActivity.this,HealthGuru.class);
                in.putExtra("asd",1);
                startActivity(in);
                break;
        }

    }
    private void showMessageOKCancel1(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeDoctorActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void setAvailabilityTrue(){
        DatabaseReference current_user_id = database.child(SharedPref.getInstance().getString(Constants.USER_ID));
        current_user_id.child("is_available").setValue("1");

    }

    private void setAvailabilityFalse(){
        DatabaseReference current_user_id = database.child(SharedPref.getInstance().getString(Constants.USER_ID));
        current_user_id.child("is_available").setValue("0");

    }
}
