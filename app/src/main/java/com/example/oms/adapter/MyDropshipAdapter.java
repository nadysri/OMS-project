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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        Glide.with(context).load(list.get(position).getImage())
                .into(holder.imageView);
        UserHelperClass userHelperClass = list.get(position);
        holder.name.setText(userHelperClass.getName());
        holder.username.setText(userHelperClass.getUsername());
        holder.address.setText(userHelperClass.getAddress());
        holder.phone.setText(userHelperClass.getPhone());
        holder.dates.setText("Joined on "+userHelperClass.getStartDate());


        if(userHelperClass.getUsername().equalsIgnoreCase("admin"))
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        if(userHelperClass.getStartDate()!=null) {
            String dateStr="";
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = null, date2 = null;
            try {
                date1 = sdf.parse(userHelperClass.getStartDate());
                date2 = sdf.parse("14-1-2022");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            System.out.println("date1 : " + sdf.format(date1));
            System.out.println("date2 : " + sdf.format(date2));

            int result = date1.compareTo(date2);
            System.out.println("result: " + result);

            if (result == 0) {
                dateStr = "Today";
            } else if (result < 0) {
                dateStr = printDifference(date1, date2);
                dateStr+= " days";
            }
            holder.timeago.setText(dateStr);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, username, address, phone, dates, timeago;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.dsname);
            dates = itemView.findViewById(R.id.datestart);
            username = itemView.findViewById(R.id.username);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            timeago = itemView.findViewById(R.id.timeago);

        }


    }

    public String printDifference(Date startDate, Date endDate) {
        String dateret="";
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        dateret = String.valueOf(elapsedDays);
        return dateret;
    }
}
