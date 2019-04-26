package com.healthcareproject.activity;

import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthcareproject.R;
import com.healthcareproject.adapter.HealthGuruAdapter;
import com.healthcareproject.adapter.HorizontalListAdapter;
import com.healthcareproject.model.DoctorPojo;
import com.healthcareproject.model.TipData;
import com.healthcareproject.utilities.Constants;
import com.healthcareproject.utilities.SharedPref;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class HealthGuru extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etTip;
    private ImageView ivsend;
    private FrameLayout frameLayout;
    private DatabaseReference databaseReference;
    private Firebase ref;
    private ArrayList<TipData> arrayList;
    private HealthGuruAdapter healthGuruAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_guru);
        initViews();
        int check=getIntent().getIntExtra("asd",3);
        if(check==1){
            frameLayout.setVisibility(View.VISIBLE);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tips");
        loadData();
    }

    private void initViews(){
        etTip=findViewById(R.id.editText);
        ivsend=findViewById(R.id.ivSend);
        ivsend.setOnClickListener(this);
        etTip.addTextChangedListener(this);
        frameLayout=findViewById(R.id.bottomParent);
        recyclerView = findViewById(R.id.tipRecyclerView);
        arrayList = new ArrayList<>();
        healthGuruAdapter = new HealthGuruAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(healthGuruAdapter);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSend:
                TipData tipData=new TipData();
                tipData.setMsg(etTip.getText().toString());
                tipData.setName(SharedPref.getInstance().getString(Constants.DOC_NAME));
                databaseReference.push().setValue(tipData);
                break;
        }



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!etTip.getText().toString().trim().isEmpty()) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int) (60 * Resources.getSystem().getDisplayMetrics().density);
            etTip.setLayoutParams(params);
            ivsend.setVisibility(View.VISIBLE);

        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            etTip.setLayoutParams(params);
            ivsend.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void loadData(){
        ref = new Firebase("https://healthcareproject-4e970.firebaseio.com/tips");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                   TipData tipData=new TipData();
                   tipData.setName(map.get("name"));
                   tipData.setMsg(map.get("msg"));



                    arrayList.add(tipData);
                    healthGuruAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(arrayList.size()-1);
                }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
