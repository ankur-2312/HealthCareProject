package com.healthcareproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthcareproject.R;
import com.healthcareproject.model.TipData;

import java.util.ArrayList;


public class HealthGuruAdapter extends RecyclerView.Adapter<HealthGuruAdapter.MyViewHolder> {

    private ArrayList<TipData> arrayList;

    public HealthGuruAdapter(ArrayList<TipData> arrayList) {

        this.arrayList=arrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflateView = layoutInflater.inflate(R.layout.inflate_tip, parent, false);
        return new MyViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(arrayList.get(position).getName());
       holder.tvMsg.setText(arrayList.get(position).getMsg());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvMsg;

        MyViewHolder(View itemView) {
            super(itemView);
            tvMsg =itemView.findViewById(R.id.tvMsg);
            tvName =itemView.findViewById(R.id.tvName);
        }



    }
}




