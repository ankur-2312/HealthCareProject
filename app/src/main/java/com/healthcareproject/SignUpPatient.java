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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignUpPatient extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etName, etEmail, etPhoneNo, etPassword; //EditText for Name,Email and PhoneNo
    private Button butSignUp;                  //Button for SignUp Up
    private TextInputLayout tilPhone, tilEmail;//TextInputLayout for Phone NO and Email
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference database;
    private DatabaseReference databaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.sign_up));
        initId();
        butSignUp.setText(getString(R.string.sign_up));
        butSignUp.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        etPhoneNo.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("users");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("chats");


    }

    private void signUp() {

        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("my1", "signup" + mAuth.getUid());

                if (task.isSuccessful()) {
                    String uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    Log.i("my1", "signup success");
                    DatabaseReference current_user_id = database.child(uId);

                    current_user_id.child("name").setValue(etName.getText().toString());
                    current_user_id.child("phone_no").setValue(etPhoneNo.getText().toString());
                    current_user_id.child("user_type").setValue(Constants.USER_PATIENT);



                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        signUp();
        startActivity(new Intent(SignUpPatient.this, LoginActivity.class));
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        String phoneNo = etPhoneNo.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean flagEmail = false, flagPhoneNo, flagEmpty, flagSpace, flagEmailCheck;


        flagEmail = validateEmail(email);
        flagPhoneNo = validatePhoneNo(phoneNo);
        flagEmpty = validateEmpty(name, email, phoneNo, password);
        flagSpace = validateSpace(name, email, phoneNo, password);

        if (flagPhoneNo && flagEmpty && flagSpace  && flagEmail) {
            butSignUp.setEnabled(true);
        } else {
            butSignUp.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //Method to initialize the globally declare views
    private void initId() {
        butSignUp = findViewById(R.id.butSignUp);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        tilPhone = findViewById(R.id.tilPhone);
        tilEmail = findViewById(R.id.tilEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    //Method to check the valid Email Address
    private boolean validateEmail(String email) {

        if (!email.trim().matches(getString(R.string.emailRegex)) && email.length() > 0) {
            tilEmail.setError(getString(R.string.emailValidation));
            return false;
        } else {

            tilEmail.setError(null);
            return true;
        }

    }

    //Method to check whether phone No is of 10 digits or not
    private boolean validatePhoneNo(String phoneNO) {
        if (phoneNO.length() < 10 && phoneNO.length() > 0) {
            tilPhone.setError(getString(R.string.phoneNoMinLength));
            return false;
        } else {
            tilPhone.setError(null);
            return true;
        }
    }

    //Method to check whether any field is empty or not
    private boolean validateEmpty(String name, String email, String phoneNo, String password) {
        return !name.equals("") && !email.equals("") && !phoneNo.equals("") && !password.equals("");
    }

    //Method to validate all EditText fields for space
    private boolean validateSpace(String name, String email, String phoneNo, String password) {
        return !name.trim().equals("") && !email.trim().equals("") && !phoneNo.trim().equals("") && !password.trim().equals("");
    }




}

