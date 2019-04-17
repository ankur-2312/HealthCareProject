package com.healthcareproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHandler();

    }

    private void getHandler() {
        Handler hand = new Handler(getMainLooper());
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPref.getInstance().getBoolean(Constants.LOGIN_CHECK)) {
                    Log.i(Constants.TAG,"main lcheck true");
                    if (SharedPref.getInstance().getString(Constants.USER_TYPE).equals(Constants.USER_PATIENT)) {
                        Log.i(Constants.TAG,"main lcheck true p");
                        startActivity(new Intent(MainActivity.this, HomePatientActivity.class));
                        finish();
                    } else {
                        Log.i(Constants.TAG,"main lcheck true d");
                        startActivity(new Intent(MainActivity.this, HomeDoctorActivity.class));
                        finish();
                    }
                } else {
                    Log.i(Constants.TAG,"main lcheck false");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }


            }
        }, Constants.SPLASH_TIME);
    }
}
