package com.example.oms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.LoginTesting;
import com.example.oms.OrdersHelperClass;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.RegisterActivity;
import com.example.oms.StatusParcel;
import com.example.oms.admin.RankDs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    Context context;

    ArrayList<OrdersHelperClass> list;

    public MyOrderAdapter(Context context, ArrayList<OrdersHelperClass> list) {
        this.context = context;
        this.list = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singlerow,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrdersHelperClass ordersHelperClass = list.get(position);
        holder.tracks.setText(ordersHelperClass.getTrackNo());
        holder.datesOr.setText("Order on: " + ordersHelperClass.getDate());
        holder.pname.setText(ordersHelperClass.getPname());
        holder.qtys.setText("Qty: "+ ordersHelperClass.getQuantity());
        holder.id.setText(ordersHelperClass.getOrderId());
        holder.tprice.setText(ordersHelperClass.getTotalPrice());
        holder.receive.setTag(ordersHelperClass.getKey());
        holder.receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                holder.receive.setEnabled(false);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference stsRef = database.getReference();

                stsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                String status = "Received";

                stsRef.child("ViewOrders").child(Prevalent.currentUser.getUsername()).child(ordersHelperClass.getOrderId()).child("statusReceived").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            holder.receive.setClickable(false);
                            holder.receive.setEnabled(false);

                        }
                        return;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname, qtys,tprice,datesOr,tracks,id;
        Button receive;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.orderID);
            pname = itemView.findViewById(R.id.nametext);
            datesOr = itemView.findViewById(R.id.dateorder);
            tracks = itemView.findViewById(R.id.tracking2);
            qtys = itemView.findViewById(R.id.qty);
            tprice = itemView.findViewById(R.id.tprice);
            receive = itemView.findViewById(R.id.received);
        }
    }

}
