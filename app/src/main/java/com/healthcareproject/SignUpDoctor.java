package com.healthcareproject;

import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;

import database.Constants;
import database.MyDatabase;

public class SignUpDoctor extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etName, etEmail, etPhoneNo, etPassword,etSpeciality,etQualification,etRegistrationNo; //EditText for Name,Email and PhoneNo
    private Button butSignUp;                  //Button for SignUp Up
    private TextInputLayout tilPhone, tilEmail;//TextInputLayout for Phone NO and Email
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.sign_up));
        initId();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("users");

    }

    private void signUp() {

        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("my1", "signup" + mAuth.getUid());

                if (task.isSuccessful()) {
                    String uId = mAuth.getCurrentUser().getUid();

                    DatabaseReference current_user_id = database.child(uId);

                    current_user_id.child("user_type").setValue(Cons.USER_DOCTOR);
                    current_user_id.child("name").setValue(etName.getText().toString());
                    current_user_id.child("phone_no").setValue(etPhoneNo.getText().toString());
                    current_user_id.child("speciality").setValue(etSpeciality.getText().toString());
                    current_user_id.child("qualification").setValue(etQualification.getText().toString());
                    current_user_id.child("registration_no").setValue(etRegistrationNo.getText().toString());

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        signUp();
        startActivity(new Intent(SignUpDoctor.this, LoginActivity.class));
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
        String speciality=etSpeciality.getText().toString();
        String qualification=etQualification.getText().toString();
        String registrationNo=etRegistrationNo.getText().toString();
        boolean flagEmail = false, flagPhoneNo, flagEmpty, flagSpace, flagEmailCheck;

        flagEmailCheck = CheckEmail(email);

        if (flagEmailCheck) {
            flagEmail = validateEmail(email);
        }

        flagPhoneNo = validatePhoneNo(phoneNo);
        flagEmpty = validateEmpty(name, email, phoneNo, password,speciality,qualification,registrationNo);
        flagSpace = validateSpace(name, email, phoneNo, password,speciality,qualification,registrationNo);

        if (flagPhoneNo && flagEmpty && flagSpace && flagEmailCheck && flagEmail) {
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
        etQualification=findViewById(R.id.etQualification);
        etRegistrationNo=findViewById(R.id.etRegistrationNo);
        etSpeciality=findViewById(R.id.etSpeciality);
        butSignUp.setText(getString(R.string.sign_up));
        butSignUp.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        etPhoneNo.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etSpeciality.addTextChangedListener(this);
        etRegistrationNo.addTextChangedListener(this);
        etQualification.addTextChangedListener(this);
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
    private boolean validateEmpty( String name, String email, String phoneNo, String password,String speciality, String qualification, String registrationNo) {
        return !name.equals("") && !email.equals("") && !phoneNo.equals("") && !password.equals("") &&!speciality.equals("")&& !qualification.equals("")&&!registrationNo.equals("");
    }

    //Method to validate all EditText fields for space
    private boolean validateSpace( String name, String email, String phoneNo, String password,String speciality, String qualification, String registrationNo) {
        return !name.trim().equals("") && !email.trim().equals("") && !phoneNo.trim().equals("") && !password.trim().equals("")&&!speciality.equals("")&& !qualification.equals("")&&!registrationNo.equals("");
    }

    //Method to Validate whether email exist already or not
    private boolean CheckEmail(String email) {

        String[] str = new String[]{Constants.EMAIL};
        ArrayList<String> emailList = new ArrayList<>();
        Cursor cursor = MyDatabase.getInstance().UserDao().fetchEmailAll();

        while (cursor.moveToNext()) {
            emailList.add(cursor.getString(cursor.getColumnIndexOrThrow(Constants.EMAIL)));
        }

        for (int i = 0; i < emailList.size(); i++) {

            int j = emailList.get(i).indexOf(email);
            if (j == 0 && email.length() > 0) {

                tilEmail.setError(getString(R.string.alreadyExists));
                return false;
            }
        }
        tilEmail.setError(null);
        return true;
    }


}


