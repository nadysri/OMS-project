package com.example.oms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.OrdersHelperClass;
import com.example.oms.R;

import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    Context context;

    ArrayList<OrdersHelperClass> list;

    public MyOrderAdapter(Context context, ArrayList<OrdersHelperClass> list) {
        this.context = context;
        this.list = list;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname, qtys,tprice,datesOr,tracks,id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.orderID);
            pname = itemView.findViewById(R.id.nametext);
            datesOr = itemView.findViewById(R.id.dateorder);
            tracks = itemView.findViewById(R.id.tracking2);
            qtys = itemView.findViewById(R.id.qty);
            tprice = itemView.findViewById(R.id.tprice);
        }
    }

}
