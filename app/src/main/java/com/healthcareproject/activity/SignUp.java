package com.healthcareproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.healthcareproject.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init(){
        Button butPatient = findViewById(R.id.butPatient);
        Button butDoctor = findViewById(R.id.butDoctor);
        butPatient.setOnClickListener(this);
        butDoctor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.butPatient:
                startActivity(new Intent(SignUp.this, SignUpPatient.class));
                break;

            case R.id.butDoctor:
                startActivity(new Intent(SignUp.this, Admin.class));
                break;



        }

    }
}
