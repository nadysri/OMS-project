package com.example.oms.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.oms.admin.fragment.NewOrderFragment;
import com.example.oms.admin.fragment.ProcessFragment;

public class OrderAdminAdapter extends FragmentStateAdapter {
    public OrderAdminAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1:
                return new ProcessFragment();
        }
        return new NewOrderFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
