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
        View v = LayoutInflater.from(context).inflate(R.layout.orderitem,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrdersHelperClass ordersHelperClass = list.get(position);
        Glide.with(context).load(list.get(position).getImage())
                .into(holder.imageView);
        holder.pname.setText(ordersHelperClass.getPname());
        holder.qty.setText("Qty: "+ ordersHelperClass.getQuantity());
        holder.orderId.setText("Order id:"+ ordersHelperClass.getOrderId());
        holder.tprice.setText(ordersHelperClass.getTotalPrice());
        holder.tracking.setText("Tracking No:"+ ordersHelperClass.getTrackNo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname, qty, orderId, tprice,tracking;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgproduct);
            pname = itemView.findViewById(R.id.prod_name);
            qty = itemView.findViewById(R.id.prodqty);
            orderId = itemView.findViewById(R.id.prodid);
            tprice = itemView.findViewById(R.id.prodtprice);
            tracking = itemView.findViewById(R.id.prodtracking);
        }
    }

}
