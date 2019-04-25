package com.healthcareproject.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcareproject.MyApplication;
import com.healthcareproject.R;
import com.healthcareproject.activity.ChatActivity;
import com.healthcareproject.activity.ChatListActivity;
import com.healthcareproject.model.model;
import com.healthcareproject.utilities.Constants;

import java.util.ArrayList;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<model> arrayList;
    private ChatListActivity listener;

    public ChatListAdapter(ArrayList<model> arrayList, ChatListActivity listener) {

        this.arrayList=arrayList;
        this.listener=listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflateView = layoutInflater.inflate(R.layout.inflate_chat_list, parent, false);
        return new MyViewHolder(inflateView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(arrayList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivDocImage;
        private TextView tvName;
        private ChatListActivity listener;
        private CardView parentContainer;

        MyViewHolder(View itemView, ChatListActivity listener) {
            super(itemView);
            this.listener=listener;
            tvName=itemView.findViewById(R.id.tvName);
            parentContainer=itemView.findViewById(R.id.parentContainer);
            parentContainer.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.parentContainer:
                   Intent intent =new Intent(MyApplication.getContext(), ChatActivity.class);
                    intent.putExtra(Constants.INTENT_DOCTOR_ID_KEY,arrayList.get(getAdapterPosition()).getKey());
                    listener.startActivity(intent);
                    break;
            }

        }
    }
}




