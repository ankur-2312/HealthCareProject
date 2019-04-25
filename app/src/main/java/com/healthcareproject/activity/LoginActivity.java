package com.healthcareproject.activity;


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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.healthcareproject.R;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;

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
                Log.i("my1",""+"enter login"+task.isSuccessful());
               if(task.isSuccessful()) {
                   if(auth.getCurrentUser().isEmailVerified()) {
                       Log.i("my1", "" + "login succcess   " + "https://healthcareproject-4e970.firebaseio.com/users/" + auth.getCurrentUser().getUid() + "/");

                       SharedPref.getInstance().setString(Constants.LOGIN_ID, Objects.requireNonNull(auth.getCurrentUser()).getUid());
                       ref = new Firebase("https://healthcareproject-4e970.firebaseio.com/users/" + auth.getCurrentUser().getUid() + "/");
                       ref.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {

                               Map<String, String> map = dataSnapshot.getValue(Map.class);
                               if (Objects.equals(Constants.USER_PATIENT, map.get("user_type"))) {
                                   SharedPref.getInstance().setString(Constants.PAT_NAME, map.get("name"));
                                   SharedPref.getInstance().setString(Constants.PAT_PHONE, map.get("phone_no"));
                                   SharedPref.getInstance().setString(Constants.USER_TYPE, map.get("user_type"));
                                   SharedPref.getInstance().setBoolean(Constants.LOGIN_CHECK, true);

                                   startActivity(new Intent(LoginActivity.this, HomePatientActivity.class));
                                   finish();
                               } else {
                                   SharedPref.getInstance().setString(Constants.DOC_NAME, (map.get("name")));
                                   SharedPref.getInstance().setString(Constants.DOC_PHONE, map.get("phone_no"));
                                   SharedPref.getInstance().setString(Constants.DOC_SPECIALITY, map.get("speciality"));
                                   SharedPref.getInstance().setString(Constants.DOC_QUALIFICATION, map.get("qualification"));
                                   SharedPref.getInstance().setString(Constants.DOC_REGISTRATION_NO, map.get("registration_no"));
                                   SharedPref.getInstance().setString(Constants.USER_TYPE, map.get("user_type"));
                                   SharedPref.getInstance().setString(Constants.USER_IMAGE, map.get("user_image"));
                                   SharedPref.getInstance().setString(Constants.USER_ID,map.get("user_id"));
                                   SharedPref.getInstance().setBoolean(Constants.LOGIN_CHECK, true);


                                   startActivity(new Intent(LoginActivity.this, HomeDoctorActivity.class));
                                   finish();

                               }

                           }

                           @Override
                           public void onCancelled(FirebaseError firebaseError) {

                           }
                       });
                   }
                   else {
                       Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                   }


                }
                if(!task.isSuccessful()){
                   Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_SHORT).show();
                   Log.i("my1","lkdsmds,"+task.getResult())  ;
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


