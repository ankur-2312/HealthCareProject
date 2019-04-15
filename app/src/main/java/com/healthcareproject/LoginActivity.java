package com.healthcareproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etEmail; //EditText for Name
    private TextInputLayout tilEmail;
    private EditText etPassword; //EditText for Email
    private Button butLogin;  //Button for Login
    private FirebaseAuth auth;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        auth=FirebaseAuth.getInstance();
        ref =new Firebase("https://healthcareproject-4e970.firebaseio.com/");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            // Button for Login
            case R.id.butLogin:
                login();
                break;


            // Button for SignUpPatient
            case R.id.tvSignUp:
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                break;
        }


    }

    //Method to initialize all the globally declare views
    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        butLogin = findViewById(R.id.butLogin);
        tilEmail=findViewById(R.id.tilEmail);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        butLogin.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        tvSignUp.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.login));
    }

    private void login(){
        auth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("my1",""+"enter login");
               if(task.isSuccessful()) {
                   Log.i("my1",""+"login succcess   "+"https://healthcareproject-4e970.firebaseio.com/users/"+auth.getCurrentUser().getUid()+"/");

                   SharedPref.getInstance().setString(Cons.LOGIN_ID, Objects.requireNonNull(auth.getCurrentUser()).getUid());
                   ref =new Firebase("https://healthcareproject-4e970.firebaseio.com/users/"+auth.getCurrentUser().getUid()+"/");
                   ref.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           Map<String,String> map= dataSnapshot.getValue(Map.class);
                           if(Objects.equals(map.get("user_type"), Cons.USER_PATIENT)) {
                               SharedPref.getInstance().setString(map.get("name"),Cons.PAT_NAME);
                               SharedPref.getInstance().setString(map.get("phone_no"),Cons.PAT_PHONE);
                               SharedPref.getInstance().setString(map.get("user_type"),Cons.USER_TYPE);

                               startActivity(new Intent(LoginActivity.this, HomePatientActivity.class));
                               finish();
                           }
                           else{
                               Log.i("my1","doctor");
                               SharedPref.getInstance().setString(map.get("name"),Cons.DOC_NAME);
                               SharedPref.getInstance().setString(map.get("phone_no"),Cons.DOC_PHONE);
                               SharedPref.getInstance().setString(map.get("speciality"),Cons.DOC_SPECIALITY);
                               SharedPref.getInstance().setString(map.get("qualification"),Cons.DOC_QUALIFICATION);
                               SharedPref.getInstance().setString(map.get("registration_no"),Cons.DOC_REGISTRATION_NO);
                               SharedPref.getInstance().setString(map.get("user_type"),Cons.USER_TYPE);
                               SharedPref.getInstance().setBoolean(Cons.LOGIN_CHECK,true);


                               startActivity(new Intent(LoginActivity.this, HomeDoctorActivity.class));
                               finish();

                           }

                       }

                       @Override
                       public void onCancelled(FirebaseError firebaseError) {

                       }
                   });


                }
                else{
                   Log.i("my1",""+task.getResult())  ;
               }
            }
        });
    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String email= etEmail.getText().toString();
        String password=etPassword.getText().toString();

        boolean flagEmail, flagEmpty;
        flagEmail=validateEmail(email);
        flagEmpty=validateEmpty(email,password);


        if (flagEmpty && flagEmail) {
            butLogin.setEnabled(true);
        } else {
            butLogin.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private boolean validateEmail(String email) {

        if (!email.trim().matches(getString(R.string.emailRegex)) && email.length() > 0) {
            tilEmail.setError(getString(R.string.emailValidation));
            return false;
        } else {

            tilEmail.setError(null);
            return true;
        }

    }


    private boolean validateEmpty(String email, String password) {
        return !password.equals("") && !email.equals("");
    }
}


