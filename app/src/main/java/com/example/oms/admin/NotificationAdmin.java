package com.example.oms.admin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;


import com.example.oms.R;
import com.example.oms.adapter.OrderAdminAdapter;
import com.google.android.material.tabs.TabLayout;


public class NotificationAdmin extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager;
    OrderAdminAdapter orderAdminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_admin);

        tabLayout = findViewById(R.id.tab_layout);
        pager = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        orderAdminAdapter = new OrderAdminAdapter(fm, getLifecycle());
        pager.setAdapter(orderAdminAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("NEW ORDER"));
        tabLayout.addTab(tabLayout.newTab().setText("PROCESS"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }
}