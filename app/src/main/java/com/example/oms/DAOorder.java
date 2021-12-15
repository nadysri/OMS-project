package com.example.oms;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOorder {

    private DatabaseReference databaseReference;

    public  DAOorder(){

        FirebaseDatabase db =FirebaseDatabase.getInstance();
        databaseReference = db.getReference("ViewOrders");
    }

    public Task<Void> add(OrdersHelperClass ordersHelperClass)
    {
        return databaseReference.push().setValue(ordersHelperClass);
    }

    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key)
    {
        if(key == null)
        {
            return databaseReference.orderByKey().limitToFirst(50);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(50);
    }

    public Query get()
    {
        return databaseReference;
    }
}
