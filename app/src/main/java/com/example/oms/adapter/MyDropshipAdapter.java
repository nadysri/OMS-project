package com.example.oms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oms.OrdersHelperClass;
import com.example.oms.R;
import com.example.oms.UserHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDropshipAdapter extends RecyclerView.Adapter<MyDropshipAdapter.MyViewHolder>{

    Context context;

    ArrayList<UserHelperClass> list;

    public MyDropshipAdapter(Context context, ArrayList<UserHelperClass> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dropshipitem,parent,false);
        return  new MyDropshipAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserHelperClass userHelperClass = list.get(position);
        holder.name.setText(userHelperClass.getName());
        holder.username.setText(userHelperClass.getUsername());
        holder.address.setText(userHelperClass.getAddress());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, username, address, phone, dates;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            //imageView = itemView.findViewById(R.id.imgprod);
            name = itemView.findViewById(R.id.dsname);
            dates = itemView.findViewById(R.id.datestart);
            username = itemView.findViewById(R.id.username);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);

        }
    }
}
