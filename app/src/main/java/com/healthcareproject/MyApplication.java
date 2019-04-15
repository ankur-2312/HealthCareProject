package com.healthcareproject;

import android.app.Application;

import com.firebase.client.Firebase;

import database.MyDatabase;

@SuppressWarnings("WeakerAccess")
public class MyApplication extends Application {
    static private MyApplication instance;

    static MyApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
        SharedPref.contextHandler(getApplicationContext());
        MyDatabase.contextHandler(getApplicationContext());
        instance = this;


    }


}
