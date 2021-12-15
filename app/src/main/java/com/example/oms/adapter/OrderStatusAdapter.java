package com.example.oms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.R;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    String data [];
    private int row_index;


    public OrderStatusAdapter(Context context ,String[] data) {
        this.data = data;
        this.context = context;
        row_index=0;
    }

    Context context;

    @NonNull
    @Override
    public OrderStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.statusparcel,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusAdapter.ViewHolder holder, int position) {
        holder.textView.setText(data[position]);
        holder.imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index=position;
                notifyItemChanged(position);
            }
        });
        if(row_index==position){
            holder.imageView.setColorFilter(Color.parseColor("#55E70D"));
            holder.textView.setTextColor(Color.parseColor("#2D2B2B"));
        }
        else
        {
            holder.imageView.setColorFilter(Color.parseColor("#7C7C7C"));
            holder.textView.setTextColor(Color.parseColor("#7C7C7C"));
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.statuscheck);
            textView=itemView.findViewById(R.id.pname1);
        }
    }
}
