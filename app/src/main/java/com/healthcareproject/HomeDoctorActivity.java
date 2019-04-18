package com.healthcareproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class HomeDoctorActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivDoctorimage,ivChat;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_doctor);
        init();
        Log.i(Constants.TAG,"445"+SharedPref.getInstance().getString(Constants.USER_IMAGE));
        Glide.with(this).load(SharedPref.getInstance().getString(Constants.USER_IMAGE)).placeholder(R.drawable.ic_user_image_placeholder).into(ivDoctorimage);
        auth= FirebaseAuth.getInstance();
    }

    private void init(){
        ivDoctorimage=findViewById(R.id.ivDp);
        ivChat=findViewById(R.id.ivChat);
        ImageView ivShowMore=findViewById(R.id.ivShowMore);
        ivShowMore.setOnClickListener(this);
        ivChat.setOnClickListener(this);

    }

    private void getdata(){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivShowMore:
                showMessageOKCancel1("Do you want to Logout",
                        new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPref.getInstance().delete();
                                SharedPref.getInstance().setBoolean(Constants.LOGIN_CHECK, false);
                                startActivity(new Intent(HomeDoctorActivity.this, LoginActivity.class));
                                auth.signOut();
                                finish();
                            }
                        });
                break;

            case R.id.ivChat:
                startActivity(new Intent(HomeDoctorActivity.this,ChatListActivity.class));
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
}
