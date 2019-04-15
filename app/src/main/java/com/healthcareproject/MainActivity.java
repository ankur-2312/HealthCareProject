package com.healthcareproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHandler();

    }

    private void getHandler(){
        Handler hand=new Handler(getMainLooper());
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!SharedPref.getInstance().getBoolean(Cons.LOGIN_CHECK)){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }

                if(SharedPref.getInstance().getString(Cons.USER_TYPE).equals(Cons.USER_DOCTOR)){

                    startActivity(new Intent(MainActivity.this, HomePatientActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(MainActivity.this,HomeDoctorActivity.class));
                    finish();
                }
            }
        },Cons.SPLASH_TIME);
    }
}
