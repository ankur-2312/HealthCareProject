package com.healthcareproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.healthcareproject.R;

import java.util.Objects;

public class Admin extends AppCompatActivity implements View.OnClickListener{


    private TextInputEditText et1,et2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();

    }

    private void initViews(){
        Button b1=findViewById(R.id.butNext);
        et1=findViewById(R.id.etName);
        et2=findViewById(R.id.etpass);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(Objects.requireNonNull(et1.getText()).toString().equals("ankur")&& Objects.requireNonNull(et2.getText()).toString().equals("ankur")){
            startActivity(new Intent(Admin.this, SignUpDoctor.class));
            finish();
        }
        else{
            Toast.makeText(this, "Credentials are Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
