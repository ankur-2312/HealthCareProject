package com.healthcareproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<String> arrayList;
    private ChatListActivity listener;

    public ChatListAdapter(ArrayList<String> arrayList, ChatListActivity listener) {

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

        holder.tvDocNAme.setText(arrayList.get(position));
//        holder.tvSpeciality.setText(arrayList.get(position).getSpeciality());
//        holder.tvQualification.setText(arrayList.get(position).getQualification());
//        holder.tvRegistrationNo.setText(arrayList.get(position).getRegistrationNo());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivDocImage;
        private TextView tvDocNAme,tvSpeciality,tvQualification,tvRegistrationNo;
        private ChatListActivity listener;
        private Button butAvailable;

        MyViewHolder(View itemView, ChatListActivity listener) {
            super(itemView);
            this.listener=listener;
            tvDocNAme=itemView.findViewById(R.id.tvDocNAme);
            tvSpeciality=itemView.findViewById(R.id.tvSpeciality);
            tvQualification=itemView.findViewById(R.id.tvqualification);
            tvRegistrationNo=itemView.findViewById(R.id.tvRegistrationNo);
            butAvailable=itemView.findViewById(R.id.butAvailable);
            butAvailable.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.butAvailable:
                   Intent intent =new Intent(MyApplication.getContext(),ChatActivity.class);
                    intent.putExtra(Constants.INTENT_DOCTOR_ID_KEY,arrayList.get(getAdapterPosition()));
                    listener.startActivity(intent);
                    break;
            }

        }
    }
}




