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
import com.example.oms.UserHelperClass;
import com.example.oms.admin.model.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder>{

    private Context context;
    private List<CartModel> cartModelList;

    public CheckoutAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.checkoutitem,parent,false);
        return  new CheckoutAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.name.setText(new StringBuilder().append(cartModelList.get(position).getPname()));
        holder.price.setText(new StringBuilder("RM").append(cartModelList.get(position).getPrice()));
        holder.qty.setText(new StringBuilder("Qty:").append(cartModelList.get(position).getQuantity()));

        holder.itemView.setTag(cartModelList.get(position).getKey());

    }



    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, descr, price,qty;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageprod);
            name = itemView.findViewById(R.id.nameprod);
            descr = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.prices);
            qty = itemView.findViewById(R.id.qtyy);

        }
    }
}
