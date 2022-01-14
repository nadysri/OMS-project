package com.example.oms.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oms.Prevalent.Prevalent;
import com.example.oms.R;
import com.example.oms.StatusParcel;
import com.example.oms.admin.editProduct;
import com.example.oms.admin.eventBus.MyUpdateCart;
import com.example.oms.admin.listener.CartLoadListener;
import com.example.oms.admin.listener.RecyclerViewClickListener;
import com.example.oms.admin.model.CartModel;
import com.example.oms.admin.model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.MyDrinkViewHolder> {

    Context context;
    private List<ProductModel> productModelList;
    CartLoadListener cartLoadListener;

    public ProductAdapterAdmin(Context context, List<ProductModel> productModelList, CartLoadListener cartLoadListener) {
        this.context = context;
        this.productModelList = productModelList;
        this.cartLoadListener = cartLoadListener;
    }

    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDrinkViewHolder(LayoutInflater.from(context).inflate(R.layout.productitemadmin,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtpname.setText(new StringBuilder().append(productModelList.get(position).getPname()));
        holder.txtprice.setText(new StringBuilder("RM").append(productModelList.get(position).getPrice()));
        holder.itemView.setTag(productModelList.get(position).getKey());
        holder.txtstock.setText(new StringBuilder().append(productModelList.get(position).getStock()));
        holder.delete.setTag(productModelList.get(position).getKey());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE")
                        .setMessage("Sure want to delete product "+ "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notifyItemRemoved(position);

                                deleteFromFirebase(productModelList.get(position));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        holder.edit.setTag(productModelList.get(position).getKey());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, editProduct.class);
                intent.putExtra("key", productModelList.get(position).getKey());
                context.startActivity(intent);

            }
        });


    }

    private void deleteFromFirebase(ProductModel productModel) {
        FirebaseDatabase.getInstance()
                .getReference("Product")
                .child(productModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCart()));



    }


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyDrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.editProd)
        ImageButton edit;
        @BindView(R.id.deleteProd)
        ImageButton delete;
        @BindView(R.id.txtpname)
        TextView txtpname;
        @BindView(R.id.txtprice)
        TextView txtprice;
        @BindView(R.id.txtstock)
        TextView txtstock;

        RecyclerViewClickListener listener;

        public void setListener(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyDrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v,getBindingAdapterPosition());

        }
    }
}
