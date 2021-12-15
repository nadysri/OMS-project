package com.example.oms;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHolder extends RecyclerView.ViewHolder {

    public TextView pname, qty, orderId, tprice,tracking,dateorder,timeorder,name;
    public Button btn;
    public OrderHolder(@NonNull View itemView) {
        super(itemView);

        pname = itemView.findViewById(R.id.prodname);
        qty = itemView.findViewById(R.id.prodqty);
        orderId = itemView.findViewById(R.id.prodid);
        tprice = itemView.findViewById(R.id.prodtprice);
        tracking = itemView.findViewById(R.id.prodtracking);
        btn = itemView.findViewById(R.id.arrangebtn);
        dateorder = itemView.findViewById(R.id.date);
        timeorder = itemView.findViewById(R.id.times);
        name = itemView.findViewById(R.id.dsname);
    }
}
