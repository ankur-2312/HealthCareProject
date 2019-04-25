package com.healthcareproject.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcareproject.MyApplication;
import com.healthcareproject.R;
import com.healthcareproject.activity.ChatActivity;
import com.healthcareproject.activity.OnlineConsultationList;
import com.healthcareproject.model.DoctorPojo;
import com.healthcareproject.utilities.Constants;

import java.util.ArrayList;


public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.MyViewHolder> {

    private ArrayList<DoctorPojo> arrayList;
    private OnlineConsultationList listener;

    public HorizontalListAdapter(ArrayList<DoctorPojo> arrayList, OnlineConsultationList listener) {

        this.arrayList=arrayList;
        this.listener=listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflateView = layoutInflater.inflate(R.layout.inflate_vertical_recycler, parent, false);
        return new MyViewHolder(inflateView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvDocNAme.setText(arrayList.get(position).getDocName());
        holder.tvSpeciality.setText(arrayList.get(position).getSpeciality());
        holder.tvQualification.setText(arrayList.get(position).getQualification());
        holder.tvRegistrationNo.setText(arrayList.get(position).getRegistrationNo());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         private ImageView ivDocImage;
         private TextView tvDocNAme,tvSpeciality,tvQualification,tvRegistrationNo;
         private OnlineConsultationList listener;
         private CardView parentContainer;

         MyViewHolder(View itemView, OnlineConsultationList listener) {
             super(itemView);
             this.listener=listener;
             tvDocNAme=itemView.findViewById(R.id.tvDocNAme);
             tvSpeciality=itemView.findViewById(R.id.tvSpeciality);
             tvQualification=itemView.findViewById(R.id.tvqualification);
             tvRegistrationNo=itemView.findViewById(R.id.tvRegistrationNo);
             parentContainer=itemView.findViewById(R.id.parentContainer);
             parentContainer.setOnClickListener(this);
         }


        @Override
        public void onClick(View v) {

             switch (v.getId()){

                 case R.id.parentContainer:
                    Intent intent =new Intent(MyApplication.getContext(), ChatActivity.class);
                     intent.putExtra(Constants.INTENT_DOCTOR_ID_KEY,arrayList.get(getAdapterPosition()).getId());
                     listener.startActivity(intent);
                     break;
             }

        }
    }
    }




