package com.healthcareproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class ChatDataAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageData> msgData;

    ChatDataAdapter(ArrayList<MessageData> msgData) {
        this.msgData = msgData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 1) {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatesender, parent, false);
            return (new SenderViewHolder(listItem));
        } else {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatereceiver, parent, false);
            return (new ReceiverViewHolder(listItem));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).textView1.setText(msgData.get(position).getMessage());
        } else {
            ((ReceiverViewHolder) holder).textView2.setText(msgData.get(position).getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return msgData.size();
    }


    @Override
    public int getItemViewType(int position) {
        String id = msgData.get(position).getMsg_type();

        if (id.equals("sender")) {
            return 1;
        } else {
            return 2;
        }

    }

    /* viewHolder to hold the sender
     views*/
    static class SenderViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;

        SenderViewHolder(View itemView) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.textview1);
        }
    }

    /*viewHolder to hold the
           receiver views*/
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        private TextView textView2;

        ReceiverViewHolder(View itemView) {
            super(itemView);
            this.textView2 = itemView.findViewById(R.id.textview2);
        }
    }
}