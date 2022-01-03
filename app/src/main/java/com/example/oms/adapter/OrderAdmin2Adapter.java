package com.example.oms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.OrdersHelperClass;
import com.example.oms.R;
import com.example.oms.StatusParcel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderAdmin2Adapter extends RecyclerView.Adapter<OrderAdmin2Adapter.MyViewHolder> {

    Context context;

    ArrayList<OrdersHelperClass> list;

    public OrderAdmin2Adapter(Context context, ArrayList<OrdersHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orderadmin,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrdersHelperClass ordersHelperClass = list.get(position);
        holder.pname.setText(ordersHelperClass.getPname());
        holder.dsname.setText(ordersHelperClass.getName());
        holder.qty.setText("Qty: " +ordersHelperClass.getQuantity());
        holder.address.setText(ordersHelperClass.getAddress());
        holder.phone.setText(ordersHelperClass.getPhone());
        holder.orderId.setText("Order Id:"+ ordersHelperClass.getOrderId());
        holder.tprice.setText(ordersHelperClass.getTotalPrice());
        holder.dateorder.setText("Order date: " + ordersHelperClass.getDate());
        holder.tracking.setText("Tracking No:"+ ordersHelperClass.getTrackNo());
        holder.status.setTag(ordersHelperClass.getKey());
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.statusinputdialog,null);
                AutoCompleteTextView autoCompleteTextView;
                Button btns;
                autoCompleteTextView=view.findViewById(R.id.autoCompleteTextView);
                btns = view.findViewById(R.id.okbtn);
                autoCompleteTextView.setText(ordersHelperClass.getStatus());
                builder.setTitle("Status shipment");
                builder.setView(view);
                builder.setCancelable(true);
                builder.show();

                List<String> list = new ArrayList<String>();
                list.add("Sender is preparing your parcel");
                list.add("Ready to ship");
                list.add("Received by the courier");
                list.add("Out for delivery");
                list.add("Parcel has been delivered");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(v.getContext(),
                        android.R.layout.select_dialog_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
                autoCompleteTextView.setAdapter(dataAdapter);

                btns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference stsRef = database.getReference();

                        stsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Object status = snapshot.getValue();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Calendar calForDate =  Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        String saveDate = currentDate.format(calForDate.getTime());

                        stsRef.child("ViewOrders/"+ordersHelperClass.getKeyName()).child(ordersHelperClass.getKey()).child("StatusDelivery").push();
                        String id = stsRef.child("ViewOrders/"+ordersHelperClass.getKeyName()).child(ordersHelperClass.getKey()).child("StatusDelivery").push().getKey();
                        stsRef.child("ViewOrders/"+ordersHelperClass.getKeyName()).child(ordersHelperClass.getKey()).child("StatusDelivery").child(id).child("status").setValue(autoCompleteTextView.getText().toString());
                        stsRef.child("ViewOrders/"+ordersHelperClass.getKeyName()).child(ordersHelperClass.getKey()).child("StatusDelivery").child(id).child("date").setValue(saveDate);
                        Intent intent = new Intent(context,StatusParcel.class);
                        intent.putExtra("stat", ordersHelperClass.getStatus());
                        intent.putExtra("keyName", ordersHelperClass.getKeyName());
                        intent.putExtra("key", ordersHelperClass.getKey());
                        context.startActivity(intent);

                    }
                });

            }
        });


        // tracking

        holder.btn.setTag(ordersHelperClass.getKey());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.input_dialog,null);
                EditText editText;
                Button button;
                editText=view.findViewById(R.id.trackingNo);
                button = view.findViewById(R.id.submitbtn);
                editText.setText(ordersHelperClass.getTrackNo());
                builder.setTitle("Shipment Arrangement");
                builder.setView(view);
                builder.setCancelable(true);

                builder.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Object value = snapshot.getValue();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                builder.setCancelable(true);
                            }
                        });
                        myRef.child("ViewOrders/"+ordersHelperClass.getKeyName()).child(ordersHelperClass.getKey()).child("trackNo").setValue(editText.getText().toString());

                    }
                });

            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname, qty, orderId, tprice,tracking,dateorder,dsname, address, phone;
        ImageView imageView;
        Button btn,status;
        AutoCompleteTextView autoCompleteTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pname = itemView.findViewById(R.id.prodname);
            dsname = itemView.findViewById(R.id.dsname);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            qty = itemView.findViewById(R.id.prodqty);
            orderId = itemView.findViewById(R.id.prodid);
            tprice = itemView.findViewById(R.id.prodtprice);
            tracking = itemView.findViewById(R.id.prodtracking);
            btn = itemView.findViewById(R.id.arrangebtn);
            dateorder = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.statusbtn);

        }
    }
}
